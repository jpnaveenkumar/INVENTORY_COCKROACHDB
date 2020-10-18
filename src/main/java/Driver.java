import Transaction.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class TestCaseManager extends Thread
{
    String testCaseFileName;
    Integer serverId;

    TestCaseManager(String testCaseFileName, Integer serverId)
    {
        this.testCaseFileName = testCaseFileName;
        this.serverId = serverId;
    }

    public void run()
    {
        System.out.println(this.testCaseFileName + " has started executing....");
        parseInputFile(this.testCaseFileName);
    }

    void parseInputFile(String fileURI)
    {
        try{
            InputStream inputStream = Driver.class.getResourceAsStream(fileURI);
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
                        List<T1Input> t1Inputs = new ArrayList<T1Input>();
                        for(int currentItem=0; currentItem < numberOfItems; currentItem++){
                            String itemInfoAsString = bufferedReader.readLine();
                            String itemInfo[] = itemInfoAsString.split(",");
                            t1Inputs.add(new T1Input(Integer.parseInt(itemInfo[0]),Integer.parseInt(itemInfo[1]),
                                    Integer.parseInt(itemInfo[2])));
                        }
                        TransactionOne transactionOne = new TransactionOne();
                        transactionOne.printOutput(transactionOne.transactionOne(Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]), numberOfItems, t1Inputs, this.serverId));
                        break;
                    }
                    case "P":{
                        TransactionTwo transactionTwo = new TransactionTwo();
                        transactionTwo.printOutput(transactionTwo.transactionTwo(Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]), Double.parseDouble(input[4]), this.serverId));
                        break;
                    }
                    case "D":{
                        TransactionThree transactionThree = new TransactionThree();
                        transactionThree.transactionThree(Integer.parseInt(input[1]), Integer.parseInt(input[2]), this.serverId);
                        break;
                    }
                    case "O":{
                        OrderStatusTransaction orderStatusTransaction = new OrderStatusTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                this.serverId
                        );
                        orderStatusTransaction.getOrderStatus();
                        break;
                    }
                    case "S":{
                        StockLevelTransaction stockLevelTransaction = new StockLevelTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                Integer.parseInt(input[4]),
                                this.serverId
                        );
                        stockLevelTransaction.performStockLevelTransaction();
                        break;
                    }
                    case "I":{
                        PopularItemTransaction popularItemTransaction = new PopularItemTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                this.serverId
                        );
                        popularItemTransaction.findPopulartItemsInLastLOrders();
                        break;
                    }
                    case "T":{
                        TransactionSeven transactionSeven = new TransactionSeven();
                        transactionSeven.printOutPut(transactionSeven.transactionSeven(this.serverId));
                        break;
                    }
                    case "R":{
                        RelatedCustomerTransaction relatedCustomerTransaction = new RelatedCustomerTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[3]),
                                Integer.parseInt(input[2]),
                                this.serverId
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
}

public class Driver {

    final Integer numberOfClients = 5;
    final Integer numberOfServers = 5;
    Framework framework;
    Session session;
    List<List<String>> lists = new ArrayList<List<String>>();

    void init() throws IOException {

        for(int i=1; i < numberOfClients; i++){
            String fileName = "xact-files/"+i+".txt";
            Integer serverId = i % numberOfServers;
            TestCaseManager testCaseManager = new TestCaseManager( fileName, serverId);
            testCaseManager.start();
            //testCaseManager.parseInputFile(fileName);
        }

        //iterate over the number of experiments
        for(int i=1;i<=2;i++)
            lists.add(outputDatabaseState(i));


        writeToFile(lists);
    }

    public List<String> outputDatabaseState(int i){
        List<String> output = new ArrayList<>();
        output.add(String.valueOf(i));

        Query warehouseQuery  = session.createNativeQuery("select sum(W_YTD) as w_ytd from warehouse");
        double w_ytd = (double) warehouseQuery.getSingleResult();
        output.add(String.valueOf(w_ytd));

        Query districtQuery = session.createNativeQuery("select sum(D_YTD) as d_ytd, sum(D_NEXT_O_ID) as d_next_o_id from district");
        Object[] districtResult = (Object[]) districtQuery.getSingleResult();
        double d_ytd = (double) districtResult[0];
        output.add(String.valueOf(d_ytd));
        output.add(String.valueOf(districtResult[1]));

        Query customerQuery = session.createNativeQuery("select sum(C_BALANCE) as c_balance, sum(C_YTD_PAYMENT) as c_ytd_payment, sum(C_PAYMENT_CNT) as c_payment_cnt, sum(C_DELIVERY_CNT) as c_delivery_cnt from customer");
        Object[] customerResult = (Object[]) customerQuery.getSingleResult();
        double c_balance = (double) customerResult[0];
        double c_ytd_payment = (double) customerResult[1];
        output.add(String.valueOf(c_balance));
        output.add(String.valueOf(c_ytd_payment));
        output.add(String.valueOf(customerResult[2]));
        output.add(String.valueOf(customerResult[3]));

        Query orderQuery = session.createNativeQuery("select max(O_ID) as o_id, sum(O_OL_CNT) as o_ol_cnt from orders");
        Object[] orderResult = (Object[]) orderQuery.getSingleResult();
        double o_ol_cnt = (double) orderResult[1];
        output.add(String.valueOf(orderResult[0]));
        output.add(String.valueOf(o_ol_cnt));

        Query orderlineQuery = session.createNativeQuery("select sum(OL_AMOUNT) as ol_amount, sum(OL_QUANTITY) as ol_quantity from orderline");
        Object[] orderlineResult = (Object[]) orderlineQuery.getSingleResult();
        double ol_amount = (double) orderlineResult[0];
        double ol_quantity = (double) orderlineResult[1];
        output.add(String.valueOf(ol_amount));
        output.add(String.valueOf(ol_quantity));

        Query stockQuery = session.createNativeQuery("select sum(S_QUANTITY) as s_quantity, sum(S_YTD) as s_ytd, sum(S_ORDER_CNT) as s_order_cnt, sum(S_REMOTE_CNT) as s_remote_cnt from stock");
        Object[] stockResult = (Object[]) stockQuery.getSingleResult();
        double s_quantity = (double) stockResult[0];
        double s_ytd = (double) stockResult[1];
        output.add(String.valueOf(s_quantity));
        output.add(String.valueOf(s_ytd));
        output.add(String.valueOf(stockResult[2]));
        output.add(String.valueOf(stockResult[3]));

        return output;
    }

    public void writeToFile(List<List<String>> lists) throws IOException {
        FileWriter csvWriter = new FileWriter("db_state.csv");
        for(List<String> line : lists){
            csvWriter.append(String.join(",", line));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
        System.out.println("DONE");
    }

    public static void main(String args[]) throws IOException {

      Driver driver = new Driver();
      driver.framework = Framework.getInstance(0);
      driver.framework.initHibernate(); // Initializing Hibernate
      driver.session = driver.framework.getSession();
      driver.init(); // start executing test case files
      //framework.destroy(); // Graceful shutdown of Hibernate
    }
}
