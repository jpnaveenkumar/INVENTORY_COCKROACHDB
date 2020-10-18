package Transaction;

import Entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStatusTransaction {

    final private Integer warehouseId;
    final private Integer districtId;
    final private Integer customerId;
    final private Framework framework;
    final private Session session;
    final private Integer serverId;

    public OrderStatusTransaction(Integer warehouseId, Integer districtId, Integer customerId, Integer serverId)
    {
        this.serverId = serverId;
        this.warehouseId = warehouseId;
        this.districtId = districtId;
        this.customerId = customerId;
        this.framework = Framework.getInstance(serverId);
        this.session = framework.getSession();
    }

    Customer getCustomerByCustomerIdAndWarehouseIdAndDistrictId()
    {
        String hql = "SELECT C_FIRST, C_MIDDLE, C_LAST, C_BALANCE FROM Customer WHERE C_W_ID = :c_w_id AND C_D_ID = :c_d_id AND C_ID = :c_id";
        Query query = this.session.createQuery(hql);
        query.setParameter("c_w_id", this.warehouseId);
        query.setParameter("c_d_id", this.districtId);
        query.setParameter("c_id", this.customerId);
        Object object[] = (Object[]) query.uniqueResult();
        Customer customer = new Customer();
        customer.setCustomerId(new CustomerId(this.customerId, this.districtId, this.warehouseId));
        customer.setC_FIRST((String)object[0]);
        customer.setC_MIDDLE((String)object[1]);
        customer.setC_LAST((String)object[2]);
        customer.setC_BALANCE((Double)object[3]);
        return customer;
    }

    Map<String, Object> getCustomerLastOrderInfo()
    {
        String sql = "select d1.o_id, d1.o_entry_d, d1.o_carrier_id, ol_i_id, ol_supply_w_id, ol_quantity, " +
                "ol_amount, ol_delivery_d from orderline ol inner join (select o_id, o_entry_d, o_carrier_id " +
                "from orders o inner join (select max(o_entry_d) as last_order_d from orders where " +
                "o_w_id = :w_id and o_d_id = :d_id and o_c_id = :c_id)  as d on o.o_entry_d = d.last_order_d and " +
                "o_w_id = :w_id and o_d_id = :d_id and o_c_id = :c_id) d1 on ol.ol_o_id = d1.o_id and ol.ol_w_id = :w_id and ol.ol_d_id = :d_id";
        Query query = this.session.createNativeQuery(sql);
        query.setParameter("w_id", this.warehouseId);
        query.setParameter("c_id", this.customerId);
        query.setParameter("d_id", this.districtId);
        List<Object> objects = query.getResultList();
        Order lastOrder = null;
        List<OrderLine> orderLines = new ArrayList<OrderLine>();
        for(Object object : objects){
            Object[] order = (Object[]) object;
            if(lastOrder == null){
                lastOrder = new Order();
                lastOrder.setOrderId(new OrderId(this.warehouseId, this.districtId, (Integer)order[0]));
                lastOrder.setO_ENTRY_D((String)order[1]);
                lastOrder.setO_CARRIER_ID((String)order[2]);
            }
            OrderLine orderLine = new OrderLine();
            orderLine.setOL_I_ID((Integer)order[3]);
            orderLine.setOL_SUPPLY_W_ID((Integer)order[4]);
            orderLine.setOL_QUANTITY((Double)order[5]);
            orderLine.setOL_AMOUNT((Double)order[6]);
            orderLine.setOL_DELIVERY_D((String)order[7]);
            orderLines.add(orderLine);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("lastOrder", lastOrder);
        result.put("items", orderLines);
        return result;
    }

    void displayCustomer(Customer customer)
    {
        System.out.println("-----Customer Information-----");
        System.out.println("Customer name : " + customer.getC_FIRST() + " " + customer.getC_MIDDLE() + " " + customer.getC_LAST());
        System.out.println("Customer Balance : " + customer.getC_BALANCE());
        System.out.println("-------------------------------");
    }

    void displayOrder(Order order)
    {
        System.out.println("-----Last Order-----");
        System.out.println("Order Id : " + order.getOrderId().getO_ID());
        System.out.println("Order Entry Date and time : " + order.getO_ENTRY_D());
        System.out.println("Order Carrier Id : " + order.getO_CARRIER_ID());
        System.out.println("-------------------------------");
    }

    void displayOrderLine(List<OrderLine> orderLines)
    {
        System.out.println("-----Items In Customers Last Order-----");
        for(OrderLine orderLine : orderLines){
            System.out.println("Item number : " + orderLine.getOL_I_ID());
            System.out.println("Supplying Warehouse Number : " + orderLine.getOL_SUPPLY_W_ID());
            System.out.println("Quantity Ordered : " + orderLine.getOL_QUANTITY());
            System.out.println("Total Price for Ordered Item : " + orderLine.getOL_AMOUNT());
            System.out.println("Date and time of Delivery : " + orderLine.getOL_DELIVERY_D());
            System.out.println("-------------------------------");
        }

    }

    public void getOrderStatus()
    {
        Transaction transaction = this.session.beginTransaction();
        Customer customer = getCustomerByCustomerIdAndWarehouseIdAndDistrictId();
        Map<String, Object> lastorderInfo = getCustomerLastOrderInfo();
        Order lastOrder = (Order) lastorderInfo.get("lastOrder");
        List<OrderLine> orderLines = (List<OrderLine>) lastorderInfo.get("items");
        this.framework.commitTransaction(transaction);
        displayCustomer(customer);
        displayOrder(lastOrder);
        displayOrderLine(orderLines);
    }

}

