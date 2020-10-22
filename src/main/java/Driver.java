import Transaction.DatabaseState;
import Transaction.Framework;

import java.io.*;
import java.util.concurrent.CountDownLatch;

class TestCaseManager extends Thread {
    String testCaseFileName;
    Integer serverId;
    FileParser fileParser;
    Integer clientNumber;
    CountDownLatch countDownLatch;

    public static FileParserOutput[] fileParserOutputs = new FileParserOutput[41];

    TestCaseManager(String testCaseFileName, Integer serverId, Integer clientNumber, CountDownLatch countDownLatch) {
        this.testCaseFileName = testCaseFileName;
        this.serverId = serverId;
        this.fileParser = new FileParser();
        this.clientNumber = clientNumber;
        this.countDownLatch = countDownLatch;
    }
    //runs for each client
    public void run() {
        System.out.println(this.testCaseFileName + " has started executing....");
        FileParserOutput fp = this.fileParser.parseInputFile(this.testCaseFileName, this.testCaseFileName, this.serverId);
        fileParserOutputs[this.clientNumber] = fp;
        this.countDownLatch.countDown();
    }
}


public class Driver {

    Integer numberOfClients = 2;
    Integer numberOfServers = 5;
    Integer experimentNumber = 5;

    DatabaseState databaseState = new DatabaseState();

    void init() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for(int clientNumber = 2; clientNumber <= numberOfClients; clientNumber++){
            String fileName = "xact-files/"+41+".txt";
            Integer serverId = clientNumber % numberOfServers;
            TestCaseManager testCaseManager = new TestCaseManager(fileName, serverId, clientNumber, countDownLatch);
            testCaseManager.start();
        }
        countDownLatch.await();
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.writeToFile(databaseState.outputDatabaseState(experimentNumber));
        csvWriter.writeClientOutputToCSV(TestCaseManager.fileParserOutputs);
    }

    public static void main(String args[]) throws IOException, InterruptedException {
      Framework framework = Framework.getInstance(0);
      framework.initHibernate();
      Driver driver = new Driver();
      driver.init();
      framework.destroy();
    }
}
