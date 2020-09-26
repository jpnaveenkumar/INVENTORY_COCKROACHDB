package Transaction;

import Entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.*;

public class PopularItemTransaction {

    private Integer warehouseId;
    private Integer districtId;
    private Integer lastLOrders;
    private Framework framework;
    private Set<Integer> listOfAllPopularItems;
    private Session session;

    public PopularItemTransaction(Integer warehouseId, Integer districtId, Integer lastLOrders){
        this.warehouseId = warehouseId;
        this.districtId = districtId;
        this.lastLOrders = lastLOrders;
        this.framework = Framework.getInstance();
        this.session = this.framework.getSession();
    }

    Integer getNextOrderId()
    {
        String sql = "SELECT D_NEXT_O_ID FROM DISTRICT WHERE D_W_ID = :w_id AND D_ID = :d_id";
        Query query = this.session.createNativeQuery(sql);
        query.setParameter("w_id", this.warehouseId);
        query.setParameter("d_id", this.districtId);
        Integer nextOrderId = (Integer)query.uniqueResult();
        return nextOrderId;
    }

    void getItemsForGivenItemIds(Set<Integer> itemIdSet, Map<Integer, Order> orders)
    {
        String itemIdsAsString = "(";
        for(Integer itemId : itemIdSet){
            itemIdsAsString += itemId + ",";
        }
        itemIdsAsString = itemIdsAsString.substring(0, itemIdsAsString.length() -1 ) + ")";
        String sql = "SELECT I_ID, I_NAME FROM ITEM WHERE I_ID IN " + itemIdsAsString;
        Query query = this.session.createNativeQuery(sql);
        List<Objects> objects = query.getResultList();
        HashMap<Integer, String> itemIdVsItemName = new HashMap<>();
        for(Object itr : objects){
            Object object[] = (Object[]) itr;
            Integer itemId = (Integer) object[0];
            String itemName = (String) object[1];
            itemIdVsItemName.put(itemId, itemName);
        }
        for(Map.Entry<Integer, Order> entry : orders.entrySet()){
            Order order = entry.getValue();
            Map<Integer, Item> itemIdVsItem = order.getPopularItems();
            Set<Integer> itemIds = itemIdVsItem.keySet();
            for(Integer itemId : itemIds){
                itemIdVsItem.get(itemId).setI_NAME(itemIdVsItemName.get(itemId));
            }
        }
        //System.out.println(objects);
    }

    Map<Integer,Order> getPopularItemsInOrders(Integer startingOrderId, Integer endingOrderId)
    {
        String sql = "select c.c_first, c.c_middle, c.c_last, d1.entryDateTime, d1.orderId, d1.customerId, d1.maxQuantity," +
                " d1.itemIds from customer c inner join (select o.o_entry_d as entryDateTime, o.o_id as orderId, o.o_c_id" +
                " as customerId, d.maxQuantity as maxQuantity, d.itemIds as itemIds from orders o inner join " +
                "(select ol.ol_o_id as orderId, max(ol.ol_quantity) as maxQuantity, string_agg(CAST(ol.ol_i_id as string),',')" +
                " as itemIds from orderline ol where ol.ol_d_id = :d_id and ol.ol_w_id = :w_id and ol.ol_o_id >= :s_o_id " +
                "and ol.ol_o_id < :e_o_id group by ol.ol_o_id) as d on o.o_id = d.orderId and o.o_w_id = :w_id and o.o_d_id = :d_id) " +
                "d1 on c.c_id = d1.customerId and c.c_w_id = :w_id and c.c_d_id = :d_id";
        Query query = this.session.createNativeQuery(sql);
        query.setParameter("d_id", this.districtId);
        query.setParameter("w_id", this.warehouseId);
        query.setParameter("s_o_id", startingOrderId);
        query.setParameter("e_o_id", endingOrderId);
        List<Object> objects = query.getResultList();
        Map<Integer,Order> orders = new HashMap<>();
        Set<Integer> itemIdSet = new HashSet<>();
        System.out.println(objects);
        //Map<Integer, Integer> verification = new HashMap<>();
        for(Object itr : objects){
            Object object[] = (Object[]) itr;
            Order order = new Order();
            String firstName = (String) object[0];
            String middleName = (String) object[1];
            String lastName = (String) object[2];
            String orderEntryDateTime = (String) object[3];
            Integer orderId = (Integer) object[4];
            Integer customerId = (Integer) object[5];
            Double maximumQuantityOrdered = (Double) object[6];
            String itemIdsAsString = (String) object[7];
            String[] itemIds = itemIdsAsString.split(",");
            order.setOrderId(new OrderId(this.warehouseId, this.districtId, orderId));
            order.setMaximumQuantityOrdered(maximumQuantityOrdered);
            order.setO_ENTRY_D(orderEntryDateTime);
            Customer customer = new Customer();
            customer.setCustomerId(new CustomerId(customerId, this.districtId, this.warehouseId));
            customer.setC_FIRST(firstName);
            customer.setC_MIDDLE(middleName);
            customer.setC_LAST(lastName);
            order.setOrderedCustomer(customer);
            order.setPopularItems(new HashMap<>());
            for(String itemId : itemIds){
                Item item = new Item();
                Integer itemIdAsInteger =Integer.parseInt(itemId);
                itemIdSet.add(itemIdAsInteger);
                item.setI_ID(itemIdAsInteger);
                order.getPopularItems().put(itemIdAsInteger, item);
//                if(verification.containsKey(itemIdAsInteger)){
//                    verification.put(itemIdAsInteger, verification.get(itemIdAsInteger) + 1);
//                }else{
//                    verification.put(itemIdAsInteger, 1);
//                }
            }
            orders.put(orderId, order);
        }
        this.listOfAllPopularItems = itemIdSet;
        getItemsForGivenItemIds(itemIdSet, orders);
        return orders;
    }

    void findPercentageOfOrderWithPopularItems(Map<Integer,Order> orders)
    {
        Integer ordersCount = orders.size();
        for(Integer itemId : this.listOfAllPopularItems){
            Integer presentInOrderCounter = 0;
            Set<Integer> orderIds = orders.keySet();
            for(Integer orderId : orderIds){
                if(orders.get(orderId).getPopularItems().containsKey(itemId)){
                    presentInOrderCounter++;
                }
            }
            Double percentage = ((double)presentInOrderCounter/ (double)ordersCount) * 100;
            System.out.println("Population of Item id :" + itemId + " percentage : "+ percentage + " presentIdOrders : "+ presentInOrderCounter);
        }
    }

    public void findPopulartItemsInLastLOrders()
    {
        Transaction transaction = this.framework.startTransaction();
        Integer nextOrderId = getNextOrderId();
        Integer startingOrderId = nextOrderId - this.lastLOrders;
        Map<Integer,Order> orders = getPopularItemsInOrders(startingOrderId, nextOrderId);
        this.framework.commitTransaction(transaction);
        findPercentageOfOrderWithPopularItems(orders);
    }

}
