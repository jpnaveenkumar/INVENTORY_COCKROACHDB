import Transaction.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Driver;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileParser {
    public FileParserOutput parseInputFile(String fileURI, String testCaseFileName, Integer serverId)
    {
        FileParserOutput fileParserOutput = new FileParserOutput();
        List<Double> timeList = new ArrayList<>();
        int numberOfTransactions = 0;
        Double totalExecutionTime = 0.0;
        try{
            InputStream inputStream = Driver.class.getResourceAsStream(fileURI);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine())!= null){
                String[] input =  line.split(",");
                Instant startTime = Instant.now();
                switch (input[0]){
                    case "N":{
                        System.out.println("-----New Order Transaction-----");
                        int numberOfItems = Integer.parseInt(input[4]);
                        List<NewOrderTransactionInput> newOrderTransactionInputs = new ArrayList<NewOrderTransactionInput>();
                        for(int currentItem=0; currentItem < numberOfItems; currentItem++){
                            String itemInfoAsString = bufferedReader.readLine();
                            String itemInfo[] = itemInfoAsString.split(",");
                            newOrderTransactionInputs.add(new NewOrderTransactionInput(Integer.parseInt(itemInfo[0]),Integer.parseInt(itemInfo[1]),
                                    Integer.parseInt(itemInfo[2])));
                        }
                        NewOrderTransaction newOrderTransaction = new NewOrderTransaction();
                        newOrderTransaction.printOutput(newOrderTransaction.transactionOne(Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]), numberOfItems, newOrderTransactionInputs, serverId));
                        break;
                    }
                    case "P":{
                        PaymentTransaction paymentTransaction = new PaymentTransaction();
                        paymentTransaction.printOutput(paymentTransaction.transactionTwo(Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]), Double.parseDouble(input[4]), serverId));
                        break;
                    }
                    case "D":{
                        DeliveryTransaction deliveryTransaction = new DeliveryTransaction();
                        deliveryTransaction.transactionThree(Integer.parseInt(input[1]), Integer.parseInt(input[2]), serverId);
                        break;
                    }
                    case "O":{
                        OrderStatusTransaction orderStatusTransaction = new OrderStatusTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                serverId
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
                                serverId
                        );
                        stockLevelTransaction.performStockLevelTransaction();
                        break;
                    }
                    case "I":{
                        PopularItemTransaction popularItemTransaction = new PopularItemTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                serverId
                        );
                        popularItemTransaction.findPopulartItemsInLastLOrders();
                        break;
                    }
                    case "T":{
                        TopBalanceTransaction topBalanceTransaction = new TopBalanceTransaction();
                        topBalanceTransaction.printOutPut(topBalanceTransaction.transactionSeven(serverId));
                        break;
                    }
                    case "R":{
                        RelatedCustomerTransaction relatedCustomerTransaction = new RelatedCustomerTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[3]),
                                Integer.parseInt(input[2]),
                                serverId
                        );
                        relatedCustomerTransaction.findRelatedCustomers();
                        break;
                    }
                }
                Instant endTime = Instant.now();
                Duration timeElapsed = Duration.between(startTime, endTime);
                Double timeTakenForThisTransaction = (double) timeElapsed.toMillis() / 1000;
                timeList.add(timeTakenForThisTransaction);
                totalExecutionTime += timeTakenForThisTransaction;
                numberOfTransactions++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        fileParserOutput.numberOfTransactions = numberOfTransactions;
        fileParserOutput.totalExecutionTime = totalExecutionTime;
        fileParserOutput.transactionThroughput = fileParserOutput.numberOfTransactions / fileParserOutput.totalExecutionTime;
        fileParserOutput.averageTransactionLatency = fileParserOutput.totalExecutionTime / fileParserOutput.numberOfTransactions;
        fileParserOutput.medianTransactionLatency = getMedianLatency(numberOfTransactions, timeList);
        Collections.sort(timeList); //sort the list to find percentile latency
        fileParserOutput.ninetyFifthPercentileLatency = getPercentileLatency(95, timeList);
        fileParserOutput.ninetyNinthPercentageLatency = getPercentileLatency(99, timeList);
        return fileParserOutput;
    }

    private double getPercentileLatency(int percentile, List<Double> timeList){
        int index = (int) Math.ceil(percentile / 100.0 * timeList.size());
        return timeList.get(index-1);
    }

    private double getMedianLatency(int numberOfTransactions, List<Double> timeList){
        if(numberOfTransactions / 2 == 0){
            return (timeList.get((int)numberOfTransactions / 2) + timeList.get((int)(numberOfTransactions / 2) - 1)) / 2;
        } else{
            return timeList.get((int)numberOfTransactions / 2);
        }
    }
}