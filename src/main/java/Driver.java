import Transaction.DatabaseState;
import Transaction.Framework;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
        FileParserOutput fp = this.fileParser.parseInputFile(this.testCaseFileName, this.testCaseFileName, this.serverId, this.countDownLatch);
        fileParserOutputs[this.clientNumber] = fp;
        this.countDownLatch.countDown();
    }
}


public class Driver {

    static Integer experimentNumber;
    static Integer serverId;
    Map<Integer, Integer> experimentNumberVsNumberOfClients = new HashMap<>();
    Map<Integer, Integer> experimentNumberVsNumberOfServers = new HashMap<>();

    Driver(){
        experimentNumberVsNumberOfClients.put(5, 20);
        experimentNumberVsNumberOfClients.put(6, 20);
        experimentNumberVsNumberOfClients.put(7, 40);
        experimentNumberVsNumberOfClients.put(8, 40);

        experimentNumberVsNumberOfServers.put(5, 4);
        experimentNumberVsNumberOfServers.put(6, 5);
        experimentNumberVsNumberOfServers.put(7, 4);
        experimentNumberVsNumberOfServers.put(8, 5);

    }

    DatabaseState databaseState = new DatabaseState();

    void init() throws IOException, InterruptedException {
        /* serverId from 1 to 5 */
        System.out.println("Starting to process.......");
        int numberOfFiles = this.experimentNumberVsNumberOfClients.get(experimentNumber);
        int numberOfServers = this.experimentNumberVsNumberOfServers.get(experimentNumber);
        int countDownLatchCount = numberOfFiles / numberOfServers;
        CountDownLatch countDownLatch = new CountDownLatch(countDownLatchCount);
        for(int clientNumber = serverId; clientNumber <= numberOfFiles;){
            String fileName = "xact-files/"+clientNumber+".txt";
            TestCaseManager testCaseManager = new TestCaseManager(fileName, serverId-1, clientNumber, countDownLatch);
            testCaseManager.start();
            clientNumber = clientNumber + numberOfServers;
        }
        countDownLatch.await();
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.writeToFile(databaseState.outputDatabaseState(experimentNumber));
        csvWriter.writeClientOutputToCSV(TestCaseManager.fileParserOutputs);
    }

    static void readInputFromConsole()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Experiment Number : ");
        experimentNumber = scanner.nextInt();
        System.out.println("Enter the Server Id : ");
        serverId = scanner.nextInt();
        System.out.println("Initializing Hibernate........");
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        Driver.readInputFromConsole();
        Framework framework = Framework.getInstance(0);
        System.out.println("Creating Tables and Importing Data through sql script .......");
        framework.initHibernate();
        Driver driver = new Driver();
        driver.init();
        framework.destroy();
    }
}
