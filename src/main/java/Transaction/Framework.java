package Transaction;

import Entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Framework {

    private static Framework framework;
    private SessionFactory sessionFactory;
    private Session session;

    private Framework(){};

    public static Framework getInstance()
    {
        if(Framework.framework == null){
            Framework.framework = new Framework();
        }
        return Framework.framework;
    }

    public Session getSession()
    {
        return this.session;
    }

    public Transaction startTransaction() {
        Transaction transaction = this.session.beginTransaction();
        return transaction;
    }

    public void commitTransaction(Transaction transaction) {
        transaction.commit();
    }

    public void initHibernate()
    {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Warehouse.class)
                .addAnnotatedClass(District.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(OrderLine.class)
                .addAnnotatedClass(Stock.class)
                .addAnnotatedClass(ItemByCustomer.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public void destroy()
    {
        sessionFactory.close();
    }

}
