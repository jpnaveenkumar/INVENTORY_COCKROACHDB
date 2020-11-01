import Transaction.*;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FileParser {
    static Logger log = Logger.getLogger(FileParser.class.getName());
    public FileParserOutput parseInputFile(String fileURI, String testCaseFileName, Integer serverId, CountDownLatch countDownLatch)
    {
        FileParserOutput fileParserOutput = new FileParserOutput();
        List<Double> timeList = new ArrayList<>();
        int numberOfTransactions = 0;
        Double totalExecutionTime = 0.0;
        int[] transactionCounts = new int[8];
        try{
            InputStream inputStream = FileParser.class.getResourceAsStream(fileURI);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine())!= null){
                System.out.println("line: " + line);
                String[] input =  line.split(",");
                double timeTakenForCurrentTransaction = 0.0;
                switch (input[0]){
                    case "N":{
                        System.out.println("-----New Order Transaction-----");
                        transactionCounts[0]++;
                        int numberOfItems = Integer.parseInt(input[4]);
                        List<NewOrderTransactionInput> newOrderTransactionInputs = new ArrayList<>();
                        for(int currentItem=0; currentItem < numberOfItems; currentItem++){
                            String itemInfoAsString = bufferedReader.readLine();
                            String itemInfo[] = itemInfoAsString.split(",");
                            newOrderTransactionInputs.add(new NewOrderTransactionInput(Integer.parseInt(itemInfo[0]),Integer.parseInt(itemInfo[1]),
                                    Integer.parseInt(itemInfo[2])));
                        }
                        NewOrderTransaction newOrderTransaction = new NewOrderTransaction();
                        Double timeTaken = newOrderTransaction.processNewOrderTransactionManager(Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]), numberOfItems, newOrderTransactionInputs, serverId);
                        timeList.add(timeTaken);
                        totalExecutionTime += timeTaken;
                        timeTakenForCurrentTransaction = timeTaken;
                        break;
                    }
                    case "P":{
                        System.out.println("-----Payment Transaction-----");
                        transactionCounts[1]++;
                        PaymentTransaction paymentTransaction = new PaymentTransaction();
                        Double timeTaken = paymentTransaction.processPaymentTransactionManager(Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]), Double.parseDouble(input[4]), serverId);
                        timeList.add(timeTaken);
                        totalExecutionTime += timeTaken;
                        timeTakenForCurrentTransaction = timeTaken;
                        break;
                    }
                    case "D":{
                        System.out.println("-----Delivery Transaction-----");
                        transactionCounts[2]++;
                        DeliveryTransactionRunner deliveryTransactionRunner = new DeliveryTransactionRunner();
                        Double timeTaken = deliveryTransactionRunner.processDeliveryTransaction(Integer.parseInt(input[1]), Integer.parseInt(input[2]), serverId);
                        timeList.add(timeTaken);
                        totalExecutionTime += timeTaken;
                        timeTakenForCurrentTransaction = timeTaken;
                        break;
                    }
                    case "O":{
                        System.out.println("-----Order Status Transaction-----");
                        transactionCounts[3]++;
                        Instant startTime = Instant.now();
                        OrderStatusTransaction orderStatusTransaction = new OrderStatusTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                serverId
                        );
                        try{
                            orderStatusTransaction.getOrderStatus();
                            Instant endTime = Instant.now();
                            Duration timeElapsed = Duration.between(startTime, endTime);
                            Double timeTakenForThisTransaction = (double) timeElapsed.toMillis() / 1000;
                            timeList.add(timeTakenForThisTransaction);
                            totalExecutionTime += timeTakenForThisTransaction;
                            timeTakenForCurrentTransaction = timeTakenForThisTransaction;
                        }catch (Exception e){}
                        break;
                    }
                    case "S":{
                        System.out.println("-----Stock Level Transaction-----");
                        transactionCounts[4]++;
                        Instant startTime = Instant.now();
                        StockLevelTransaction stockLevelTransaction = new StockLevelTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                Integer.parseInt(input[4]),
                                serverId
                        );
                        try{
                            stockLevelTransaction.performStockLevelTransaction();
                            Instant endTime = Instant.now();
                            Duration timeElapsed = Duration.between(startTime, endTime);
                            Double timeTakenForThisTransaction = (double) timeElapsed.toMillis() / 1000;
                            timeList.add(timeTakenForThisTransaction);
                            totalExecutionTime += timeTakenForThisTransaction;
                            timeTakenForCurrentTransaction = timeTakenForThisTransaction;
                        }catch (Exception e){}
                        break;
                    }
                    case "I":{
                        transactionCounts[5]++;
                        System.out.println("-----Popular Item Transaction-----");
                        Instant startTime = Instant.now();
                        PopularItemTransaction popularItemTransaction = new PopularItemTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[2]),
                                Integer.parseInt(input[3]),
                                serverId
                        );
                        try{
                            popularItemTransaction.findPopulartItemsInLastLOrders();
                            Instant endTime = Instant.now();
                            Duration timeElapsed = Duration.between(startTime, endTime);
                            Double timeTakenForThisTransaction = (double) timeElapsed.toMillis() / 1000;
                            timeList.add(timeTakenForThisTransaction);
                            totalExecutionTime += timeTakenForThisTransaction;
                            timeTakenForCurrentTransaction = timeTakenForThisTransaction;
                        }catch (Exception e){}
                        break;
                    }
                    case "T":{
                        transactionCounts[6]++;
                        System.out.println("-----Top Balance Transaction-----");
                        Instant startTime = Instant.now();
                        TopBalanceTransaction topBalanceTransaction = new TopBalanceTransaction();
                        try{
                            topBalanceTransaction.printOutPut(topBalanceTransaction.transactionSeven(serverId));
                            Instant endTime = Instant.now();
                            Duration timeElapsed = Duration.between(startTime, endTime);
                            Double timeTakenForThisTransaction = (double) timeElapsed.toMillis() / 1000;
                            timeList.add(timeTakenForThisTransaction);
                            totalExecutionTime += timeTakenForThisTransaction;
                            timeTakenForCurrentTransaction = timeTakenForThisTransaction;
                        }catch (Exception e){}
                        break;
                    }
                    case "R":{
                        System.out.println("-----Related Customer Transaction-----");
                        transactionCounts[7]++;
                        Instant startTime = Instant.now();
                        RelatedCustomerTransaction relatedCustomerTransaction = new RelatedCustomerTransaction(
                                Integer.parseInt(input[1]),
                                Integer.parseInt(input[3]),
                                Integer.parseInt(input[2]),
                                serverId
                        );
                        try{
                            relatedCustomerTransaction.findRelatedCustomers();
                            Instant endTime = Instant.now();
                            Duration timeElapsed = Duration.between(startTime, endTime);
                            Double timeTakenForThisTransaction = (double) timeElapsed.toMillis() / 1000;
                            timeList.add(timeTakenForThisTransaction);
                            totalExecutionTime += timeTakenForThisTransaction;
                            timeTakenForCurrentTransaction = timeTakenForThisTransaction;
                        }catch (Exception e){}
                        break;
                    }
                }
                numberOfTransactions++;
                System.out.println("Completed Transaction " + numberOfTransactions + " in " + timeTakenForCurrentTransaction + " seconds");
                System.out.println("Thread count : "+ Thread.activeCount());
                System.out.println("CountDown Latch count : "+ countDownLatch.getCount());
            }
        }catch (Exception e){
            log.error("Failed during file parsing", e);
            System.out.println("Failed during file parsing");
            e.printStackTrace();
        }
        log.info("Test case file name : "+testCaseFileName+ " count so far : "+ fileParserOutput.numberOfTransactions);
        fileParserOutput.numberOfTransactions = numberOfTransactions;
        fileParserOutput.totalExecutionTime = totalExecutionTime;
        fileParserOutput.transactionThroughput = fileParserOutput.numberOfTransactions / fileParserOutput.totalExecutionTime;
        fileParserOutput.averageTransactionLatency = fileParserOutput.totalExecutionTime / fileParserOutput.numberOfTransactions;
        Collections.sort(timeList); //sort the list to find percentile latency
        fileParserOutput.medianTransactionLatency = getMedianLatency(numberOfTransactions, timeList);
        fileParserOutput.ninetyFifthPercentileLatency = getPercentileLatency(95, timeList);
        fileParserOutput.ninetyNinthPercentageLatency = getPercentileLatency(99, timeList);
        fileParserOutput.transactionCounts = transactionCounts;
        return fileParserOutput;
    }

    private double getPercentileLatency(int percentile, List<Double> timeList){
        int index = (int) Math.ceil(percentile / 100.0 * timeList.size());
        return timeList.get(index-1);
    }

    private double getMedianLatency(int numberOfTransactions, List<Double> timeList){
        if(numberOfTransactions % 2 == 0){
            return (timeList.get((int)numberOfTransactions / 2) + timeList.get((int)(numberOfTransactions / 2) - 1)) / 2;
        } else{
            return timeList.get((int)numberOfTransactions / 2);
        }
    }
}