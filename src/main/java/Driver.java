import Transaction.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class Driver {

    void sampleTestCase1()
    {
        Framework framework = Framework.getInstance();
        Session session = framework.getSession();
        Transaction transaction = framework.startTransaction();
        String hql = "FROM Warehouse as W where W.W_ID = 1";
        Query query = session.createQuery(hql);
        List result = query.list();
        System.out.println(result);
        framework.commitTransaction(transaction);
    }

    void sampleTestCase2()
    {
        Framework framework = Framework.getInstance();
        Session session = framework.getSession();
        Transaction transaction = framework.startTransaction();
        Query query = session.createNativeQuery("select s_i_id from stock s inner join (select ol_i_id from orderline where ol_w_id = :w_id and ol_d_id = :d_id and ol_o_id >= :s_o_id and ol_o_id < :e_o_id) as d on s.s_i_id = d.ol_i_id and s.s_w_id = :w_id where s.s_quantity < :threshold");
        query.setParameter("w_id", 1);
        query.setParameter("d_id", 1);
        query.setParameter("s_o_id", 2990);
        query.setParameter("e_o_id", 3001);
        query.setParameter("threshold", 11);
        List result = query.list();
        System.out.println(result);
        framework.commitTransaction(transaction);
    }

    public static void main(String args[])
    {
        Driver driver = new Driver();
        Framework framework = Framework.getInstance();
        framework.initHibernate(); // Initializing Hibernate

        driver.sampleTestCase1();
        driver.sampleTestCase2();
//        OrderStatusTransaction orderStatusTransaction = new OrderStatusTransaction(1, 1 , 1);
//        orderStatusTransaction.getOrderStatus();
//        StockLevelTransaction stockLevelTransaction = new StockLevelTransaction(1, 1, 11, 11);
//        stockLevelTransaction.performStockLevelTransaction();
//          PopularItemTransaction popularItemTransaction = new PopularItemTransaction(1, 1, 11);
//          popularItemTransaction.findPopulartItemsInLastLOrders();
//        RelatedCustomerTransaction relatedCustomerTransaction = new RelatedCustomerTransaction(1,1,1);
//        relatedCustomerTransaction.findRelatedCustomers();

        framework.destroy(); // Graceful shutdown of Hibernate
    }
}
