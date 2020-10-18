package Transaction;

import Entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Framework {

    private static SessionFactory sessionFactory;
    private static SessionFactory sessionFactory_40;
    private static SessionFactory sessionFactory_41;
    private static SessionFactory sessionFactory_42;
    private static SessionFactory sessionFactory_43;
    private static SessionFactory sessionFactory_44;
    private Session session;
    private Integer serverId;

    private Framework(Integer serverId){
        this.serverId = serverId;
    };

    public static Framework getInstance(Integer serverId)
    {
        return new Framework(serverId);
    }

    public Session getSession()
    {
        if(this.serverId == 0){
            this.session = sessionFactory_40.openSession();
        }else if(this.serverId == 1){
            this.session = sessionFactory_41.openSession();
        }else if(this.serverId == 2){
            this.session = sessionFactory_42.openSession();
        }else if(this.serverId == 3){
            this.session = sessionFactory_43.openSession();
        }else if(this.serverId == 4){
            this.session = sessionFactory_44.openSession();
        }
        return this.session;
    }

    public Transaction startTransaction() {
        Transaction transaction = this.session.beginTransaction();
        return transaction;
    }

    public void commitTransaction(Transaction transaction) {
        transaction.commit();
        this.session.close();
    }

    public void initHibernate()
    {
//        sessionFactory = new Configuration()
//                .configure("hibernate.cfg.xml")
//                .addAnnotatedClass(Warehouse.class)
//                .addAnnotatedClass(District.class)
//                .addAnnotatedClass(Customer.class)
//                .addAnnotatedClass(Item.class)
//                .addAnnotatedClass(Order.class)
//                .addAnnotatedClass(OrderLine.class)
//                .addAnnotatedClass(Stock.class)
//                .addAnnotatedClass(ItemByCustomer.class)
//                .buildSessionFactory();

        sessionFactory_40 = new Configuration()
                .configure("hibernate_40.cfg.xml")
                .addAnnotatedClass(Warehouse.class)
                .addAnnotatedClass(District.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(OrderLine.class)
                .addAnnotatedClass(Stock.class)
                .addAnnotatedClass(ItemByCustomer.class)
                .buildSessionFactory();

        sessionFactory_41 = new Configuration()
                .configure("hibernate_41.cfg.xml")
                .addAnnotatedClass(Warehouse.class)
                .addAnnotatedClass(District.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(OrderLine.class)
                .addAnnotatedClass(Stock.class)
                .addAnnotatedClass(ItemByCustomer.class)
                .buildSessionFactory();

        sessionFactory_42 = new Configuration()
                .configure("hibernate_42.cfg.xml")
                .addAnnotatedClass(Warehouse.class)
                .addAnnotatedClass(District.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(OrderLine.class)
                .addAnnotatedClass(Stock.class)
                .addAnnotatedClass(ItemByCustomer.class)
                .buildSessionFactory();

        sessionFactory_43 = new Configuration()
                .configure("hibernate_43.cfg.xml")
                .addAnnotatedClass(Warehouse.class)
                .addAnnotatedClass(District.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(OrderLine.class)
                .addAnnotatedClass(Stock.class)
                .addAnnotatedClass(ItemByCustomer.class)
                .buildSessionFactory();

        sessionFactory_44 = new Configuration()
                .configure("hibernate_44.cfg.xml")
                .addAnnotatedClass(Warehouse.class)
                .addAnnotatedClass(District.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(OrderLine.class)
                .addAnnotatedClass(Stock.class)
                .addAnnotatedClass(ItemByCustomer.class)
                .buildSessionFactory();
    }

    public void destroy()
    {
        sessionFactory_40.close();
        sessionFactory_41.close();
        sessionFactory_42.close();
        sessionFactory_43.close();
        sessionFactory_44.close();
    }

}
