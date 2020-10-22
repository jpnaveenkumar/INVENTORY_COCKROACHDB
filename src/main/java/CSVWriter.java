import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVWriter {
    public void writeToFile(List<String> lists) throws IOException {
        FileWriter csvWriter = new FileWriter("db_state.csv");
        csvWriter.append(String.join(",", lists));
        csvWriter.append("\n");
        csvWriter.flush();
        csvWriter.close();
        System.out.println("Written to db_state.csv");
    }

    public void writeClientOutputToCSV(FileParserOutput[] fileParserOutputs) throws IOException {
        FileWriter csvWriter = new FileWriter("clients.csv");
        List<List<String>> fileParserOutputLists = new ArrayList<>();
        for(FileParserOutput fpo : fileParserOutputs){
            if(fpo != null){
                List<String> list = new ArrayList<>();
                list.add(String.valueOf(fpo.numberOfTransactions));
                list.add(String.valueOf(fpo.totalExecutionTime));
                list.add(String.valueOf(fpo.transactionThroughput));
                list.add(String.valueOf(fpo.averageTransactionLatency));
                list.add(String.valueOf(fpo.medianTransactionLatency));
                list.add(String.valueOf(fpo.ninetyFifthPercentileLatency));
                list.add(String.valueOf(fpo.ninetyNinthPercentageLatency));
                fileParserOutputLists.add(list);
                System.out.println("-----Fileparser output-----");
                System.out.println("Number of transactions: " + fpo.numberOfTransactions);
                System.out.println("Total execution time: " + fpo.totalExecutionTime);
                System.out.println("Transaction throughput: " + fpo.transactionThroughput);
                System.out.println("Average transaction latency: " + fpo.averageTransactionLatency);
                for(int transactionCount = 0; transactionCount < fpo.transactionCounts.length; transactionCount++){
                    System.out.println("Count of transaction " + (transactionCount + 1) + " = " + fpo.transactionCounts[transactionCount]);
                }
            }
        }
        for(List<String> fileParserOutputList : fileParserOutputLists){
            csvWriter.append(String.join(",", fileParserOutputList));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
        System.out.println("Written to clients.csv");
    }
}
