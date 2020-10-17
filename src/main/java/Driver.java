import Transaction.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.*;
import java.util.ArrayList;
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

    void parseInputFile()
    {
        try{
            InputStream inputStream = Driver.class.getResourceAsStream("xact-files/1.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int count=0;
            while ((line = bufferedReader.readLine())!= null){
                String[] input =  line.split(",");
                switch (input[0]){
                    case "N":{
                        System.out.println("pass count : "+count);
                        count++;
                        int numberOfItems = Integer.parseInt(input[4]);
                        List<NewOrderInput> newOrderInput = new ArrayList<NewOrderInput>();
                        for(int currentItem=0; currentItem < numberOfItems; currentItem++){
                            String itemInfoAsString = bufferedReader.readLine();
                            String itemInfo[] = itemInfoAsString.split(",");
                            newOrderInput.add(new NewOrderInput(Integer.parseInt(itemInfo[0]),Integer.parseInt(itemInfo[1]),
                                    Integer.parseInt(itemInfo[2])));
                        }
                        NewOrderTransaction newOrderTransaction = new NewOrderTransaction();
                        newOrderTransaction.printOutput(newOrderTransaction.transactionOne(Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]), numberOfItems, newOrderInput));
                        break;
                    }
                    case "P":{
                        PaymentTransaction paymentTransaction = new PaymentTransaction();
                        paymentTransaction.printOutput(paymentTransaction.processPaymentTransaction(Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]), Double.parseDouble(input[4])));
                        break;
                    }
                    case "D":{
                        DeliveryTransaction deliveryTransaction = new DeliveryTransaction();
                        deliveryTransaction.processDeliveryTransaction(Integer.parseInt(input[1]), Integer.parseInt(input[2]));
                        break;
                    }
                    case "O":{
                        OrderStatusTransaction orderStatusTransaction = new OrderStatusTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3])
                        );
                        orderStatusTransaction.getOrderStatus();
                        break;
                    }
                    case "S":{
                        StockLevelTransaction stockLevelTransaction = new StockLevelTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                Integer.parseInt(input[4])
                        );
                        stockLevelTransaction.performStockLevelTransaction();
                        break;
                    }
                    case "I":{
                        PopularItemTransaction popularItemTransaction = new PopularItemTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3])
                        );
                        popularItemTransaction.findPopulartItemsInLastLOrders();
                        break;
                    }
                    case "T":{
                        TopBalanceTransaction topBalanceTransaction = new TopBalanceTransaction();
                        topBalanceTransaction.printOutPut(topBalanceTransaction.transactionSeven());
                        break;
                    }
                    case "R":{
                        RelatedCustomerTransaction relatedCustomerTransaction = new RelatedCustomerTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[3]),
                                Integer.parseInt(input[2])
                        );
                        relatedCustomerTransaction.findRelatedCustomers();
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {

          Driver driver = new Driver();
          Framework framework = Framework.getInstance();
          framework.initHibernate(); // Initializing Hibernate
          driver.parseInputFile();

//        driver.sampleTestCase1();
//        driver.sampleTestCase2();
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
