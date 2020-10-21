import Transaction.DatabaseState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class TestCaseManager extends Thread {
    String testCaseFileName;
    Integer serverId;
    FileParser fileParser;

    TestCaseManager(String testCaseFileName, Integer serverId) {
        this.testCaseFileName = testCaseFileName;
        this.serverId = serverId;
        this.fileParser = new FileParser();
    }
    //runs for each client
    public void run() {
        System.out.println(this.testCaseFileName + " has started executing....");
        FileParserOutput fp = this.fileParser.parseInputFile(this.testCaseFileName, this.testCaseFileName, this.serverId);
    }
}


public class Driver {

    Integer numberOfClients = 5;
    Integer numberOfServers = 5;

    List<List<String>> dbStateLists = new ArrayList<List<String>>();
    DatabaseState databaseState = new DatabaseState();
    void init() throws IOException {
        for(int experimentNumber = 5; experimentNumber <= 8; experimentNumber++){
            //add logic for number of clients;
            //numberOfClients = (experimentNumber < 7) ? 20 : 40;
            //numberOfServers = (experimentNumber % 2 == 1) ? 4 : 5;
            for(int clientNumber = 1; clientNumber < numberOfClients; clientNumber++){
                String fileName = "xact-files/"+clientNumber+".txt";
                Integer serverId = clientNumber % numberOfServers;
                TestCaseManager testCaseManager = new TestCaseManager( fileName, serverId);
                testCaseManager.start();
            }
            dbStateLists.add(databaseState.outputDatabaseState(experimentNumber));
        }

        writeToFile(dbStateLists, 1);
    }


    public void writeToFile(List<List<String>> lists, int filenumber) throws IOException {
        FileWriter csvWriter = new FileWriter((filenumber == 1) ? "db_state.csv" : "clients.csv");
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
      driver.init(); // start executing test case files
    }
}
