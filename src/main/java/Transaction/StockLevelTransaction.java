package Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StockLevelTransaction {
    private Integer warehouseId;
    private Integer districtId;
    private Integer threshold;
    private Integer lastLOrders;
    Framework framework;
    Session session;

    public StockLevelTransaction(Integer warehouseId, Integer districtId, Integer threshold, Integer lastLOrders)
    {
        this.warehouseId = warehouseId;
        this.districtId = districtId;
        this.threshold = threshold;
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

    List getItemsWithQuantityBelowThreshold(int startingOrderId, int endingOrderId)
    {
        String sql = "select s_i_id from stock s inner join (select ol_i_id from orderline where ol_w_id = :w_id and ol_d_id = :d_id and ol_o_id >= :s_o_id and ol_o_id < :e_o_id ) as d on s.s_i_id = d.ol_i_id and s.s_w_id = :w_id where s.s_quantity < :threshold";
        Query query = this.session.createNativeQuery(sql);
        query.setParameter("w_id", this.warehouseId);
        query.setParameter("d_id", this.districtId);
        query.setParameter("s_o_id", startingOrderId);
        query.setParameter("e_o_id", endingOrderId);
        query.setParameter("threshold", this.threshold);
        List<Object> objects = query.getResultList();
        System.out.println(objects);
        return objects;
    }

    public void performStockLevelTransaction()
    {
        Transaction transaction = this.framework.startTransaction();
        Integer nextOrderId = getNextOrderId();
        Integer startingOrderId = nextOrderId - this.lastLOrders;
        List items = getItemsWithQuantityBelowThreshold(startingOrderId, nextOrderId);
        System.out.println(items);
        System.out.println("Count of Total items less than threshold value : " + items.size());
        this.framework.commitTransaction(transaction);
    }
}
