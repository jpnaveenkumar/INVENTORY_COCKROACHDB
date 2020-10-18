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

    void init()
    {
        for(int i=1; i < numberOfClients; i++){
            String fileName = "xact-files/"+i+".txt";
            Integer serverId = i % numberOfServers;
            TestCaseManager testCaseManager = new TestCaseManager( fileName, serverId);
            testCaseManager.start();
            //testCaseManager.parseInputFile(fileName);
        }
    }

    public static void main(String args[])
    {

      Driver driver = new Driver();
      Framework framework = Framework.getInstance(0);
      framework.initHibernate(); // Initializing Hibernate
      driver.init(); // start executing test case files
      //framework.destroy(); // Graceful shutdown of Hibernate
    }
}
