import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVWriter {

    public void writeToDBStateCSV(List<String> lists, String context) throws IOException {
        FileWriter csvWriter = new FileWriter("db_state_"+ context + ".csv");
        csvWriter.append(String.join(",", lists));
        csvWriter.append("\n");
        csvWriter.flush();
        csvWriter.close();
        System.out.println("\n Written to db_state_" + context + ".csv");
    }

    public void writeThroughputMetricsToCSV(int experimentNumber, FileParserOutput[] fileParserOutputs) throws IOException {
        FileWriter throughputWriter = new FileWriter("throughput.csv");
        Double minThroughput =  - Double.MIN_VALUE;
        Double maxThroughput =  Double.MIN_VALUE;
        Double sumThroughput = 0.0;
        Double avgThroughput = 0.0;
        int numberOfClients = 0;
        for(FileParserOutput fpo : fileParserOutputs){
            if(fpo!=null){
                numberOfClients++;
                minThroughput = Math.min(minThroughput, fpo.transactionThroughput);
                maxThroughput = Math.min(maxThroughput, fpo.transactionThroughput);
                sumThroughput += fpo.transactionThroughput;
            }
        }
        avgThroughput = sumThroughput / numberOfClients;
        List<String> throughputList = new ArrayList<>();
        throughputList.add(String.valueOf(experimentNumber));
        throughputList.add(String.valueOf(minThroughput));
        throughputList.add(String.valueOf(avgThroughput));
        throughputList.add(String.valueOf(maxThroughput));
        throughputWriter.append(String.join(",", throughputList));
        throughputWriter.append("\n");
        throughputWriter.flush();
        throughputWriter.close();
        System.out.println("\n Written to throughput.csv");
    }

    public void writeClientOutputToCSV(FileParserOutput[] fileParserOutputs) throws IOException {
        FileWriter csvWriter = new FileWriter("clients.csv");
        List<List<String>> fileParserOutputLists = new ArrayList<>();
        for(int i=1;i<41;i++){
            FileParserOutput fpo = fileParserOutputs[i];
            if(fpo != null){
                List<String> list = new ArrayList<>();
                list.add(String.valueOf(i));
                list.add(String.valueOf(fpo.numberOfTransactions));
                list.add(String.valueOf(fpo.totalExecutionTime));
                list.add(String.valueOf(fpo.transactionThroughput));
                list.add(String.valueOf(fpo.averageTransactionLatency));
                list.add(String.valueOf(fpo.medianTransactionLatency));
                list.add(String.valueOf(fpo.ninetyFifthPercentileLatency));
                list.add(String.valueOf(fpo.ninetyNinthPercentageLatency));
                fileParserOutputLists.add(list);
                System.out.println("-----Fileparser output-----");
                System.out.println("Client number: " + i);
                System.out.println("Number of transactions: " + fpo.numberOfTransactions);
                System.out.println("Total execution time: " + fpo.totalExecutionTime);
                System.out.println("Transaction throughput: " + fpo.transactionThroughput);
                System.out.println("Average transaction latency: " + fpo.averageTransactionLatency);
                /* writing xact frequency for each client in csv file*/
                List<String> transactionCountList = new ArrayList();
                transactionCountList.add(String.valueOf(i));
                for(int transactionCount = 0; transactionCount < fpo.transactionCounts.length; transactionCount++){
                    System.out.println("Count of transaction " + (transactionCount + 1) + " = " + fpo.transactionCounts[transactionCount]);
                    transactionCountList.add(String.valueOf(fpo.transactionCounts[transactionCount]));
                }
                FileWriter xactCountWriter = new FileWriter("transactionCounts.csv");
                xactCountWriter.append(String.join(",", transactionCountList));
                xactCountWriter.append("\n");
                xactCountWriter.flush();
                xactCountWriter.close();
            }
        }
        for(List<String> fileParserOutputList : fileParserOutputLists){
            csvWriter.append(String.join(",", fileParserOutputList));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
        System.out.println("\n Written to clients.csv");
    }
}
