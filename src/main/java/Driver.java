import Transaction.Framework;
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
        Query query = session.createNativeQuery("select max(i_price) from item");
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

        framework.destroy(); // Graceful shutdown of Hibernate
    }
}
