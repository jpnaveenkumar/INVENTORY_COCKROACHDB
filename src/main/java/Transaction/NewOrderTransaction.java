package Transaction;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class NewOrderTransaction {
    int queryCount = 0;
    Integer serverId;
    static Logger log = Logger.getLogger(NewOrderTransaction.class.getName());
    public Double processNewOrderTransactionManager(int C_ID, int W_ID, int D_ID, int num_items, List<NewOrderTransactionInput> inputList, Integer serverId){
        this.serverId = serverId;
        Double timeTaken = 0.0;
        int currentTransactionRetryCount = 0;
        NewOrderTransactionOutput newOrderTransactionOutput = new NewOrderTransactionOutput();
        Session session = null;
        Transaction transaction = null;
        while (true){
            Instant startTime = Instant.now();
            try{
                Framework framework = Framework.getInstance(serverId);
                session = framework.getSession();
                currentTransactionRetryCount++;
                transaction = framework.startTransaction();
                newOrderTransactionOutput = processNewOrderTransaction(C_ID, W_ID, D_ID, num_items, inputList, session);
                transaction.commit();
                Instant endTime = Instant.now();
                printOutput(newOrderTransactionOutput);
                Duration timeElapsed = Duration.between(startTime, endTime);
                timeTaken = (double) timeElapsed.toMillis() / 1000;
                System.out.println("Committing transaction successfully with retry count : "+(currentTransactionRetryCount-1));
                break;
            }catch (Exception e){
                log.error("Error occurred while committing neworder transaction retry count :"+(currentTransactionRetryCount-1) + Thread.currentThread().getName(), e);
                System.out.println("Error occurred while committing neworder transaction retry count : "+(currentTransactionRetryCount-1) + Thread.currentThread().getName());
                try {
                    //int sleepMillis = (int)(Math.pow(2, currentTransactionRetryCount) * 100) + new Random().nextInt(100);
                    int sleepMillis = (int)(Math.pow(2, Math.min(currentTransactionRetryCount,9)) * 100) + new Random().nextInt(100);
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                if(transaction != null) transaction.rollback();
            }
            finally {
                if(session != null) session.close();
            }
        }
        return timeTaken;
    }

    private NewOrderTransactionOutput processNewOrderTransaction(int C_ID, int W_ID, int D_ID, int num_items, List<NewOrderTransactionInput> inputList, Session session){

        //Note : Numbers or alphabet comments above a sequence of code lines indicate which part of problem statement it is related to. Refer section 2.1, Processing steps in project document.


        //List<T1Input> inputList = new ArrayList<T1Input>();
        NewOrderTransactionOutput newOrderTransactionOutput = new NewOrderTransactionOutput();

        //1, 6
        Query q0 = session.createNativeQuery("SELECT D_NEXT_O_ID, D_TAX, W_TAX, C_DISCOUNT, C_LAST, C_CREDIT from district, warehouse, customer where D_ID = :d_id and D_W_ID = :w_id and W_ID = :w_id and C_ID = :c_id and C_W_ID = :w_id and C_D_ID = :d_id");
        q0.setParameter("d_id", D_ID);
        q0.setParameter("w_id", W_ID);
        q0.setParameter("c_id", C_ID);
        Object[] districtList = (Object[]) q0.getSingleResult();
        int N = (int) districtList[0];
        double d_tax = (double) districtList[1];
        double w_tax = (double) districtList[2];
        double c_discount = (double) districtList[3];
        newOrderTransactionOutput.C_LAST = (String) districtList[4];
        newOrderTransactionOutput.C_CREDIT = (String) districtList[5];
        queryCount++;

        //2
        Query q1 = session.createQuery("UPDATE District SET D_NEXT_O_ID = D_NEXT_O_ID + 1 WHERE D_W_ID = :warehouseId AND D_ID = :districtId");
        q1.setParameter("warehouseId", W_ID);
        q1.setParameter("districtId", D_ID);
        q1.executeUpdate();
        queryCount++;

        //3
        int allLocal = checkAllLocal(inputList, W_ID);
        String o_entry_d = String.valueOf(java.time.LocalTime.now());
        String s2 = String.format("INSERT INTO orders(O_W_ID, O_D_ID, O_ID, O_C_ID, O_CARRIER_ID, O_OL_CNT, O_ALL_LOCAL, O_ENTRY_D) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)");
        Query q2 = session.createNativeQuery(s2);
        q2.setParameter(1, W_ID);
        q2.setParameter(2, D_ID);
        q2.setParameter(3, N);
        q2.setParameter(4, C_ID);
        q2.setParameter(5, null);
        q2.setParameter(6, num_items);
        q2.setParameter(7, allLocal); //should change
        q2.setParameter(8, o_entry_d);
        q2.executeUpdate();
        queryCount++;

        //4
        double totalAmount = 0.0;

        //5 (a)
        StringBuilder getStockQuantityBuilder = new StringBuilder();
        for(int i=0; i<num_items; i++) {
            int itemNumber = inputList.get(i).itemNumber;
            int supplierWarehouseNumber = inputList.get(i).supplierWarehouseNumber;
            int quantity = inputList.get(i).quantity;
            getStockQuantityBuilder.append(String.format("SELECT S_QUANTITY FROM Stock WHERE S_W_ID = %s AND S_I_ID = %s", supplierWarehouseNumber, itemNumber));
            getStockQuantityBuilder.append((i<num_items-1) ? " UNION ALL " : "");
        }
        Query getStockQuantity = session.createNativeQuery(new String(getStockQuantityBuilder));
        List<Double> stockQuantityList = getStockQuantity.getResultList();
        queryCount++;

        //5 (g)
        StringBuilder insertOrderLineQueryBuilder = new StringBuilder("INSERT INTO OrderLine(OL_O_ID, OL_D_ID, OL_W_ID, OL_NUMBER, OL_I_ID, OL_SUPPLY_W_ID, OL_QUANTITY, OL_AMOUNT, OL_DELIVERY_D, OL_DIST_INFO) VALUES ");
        StringBuilder insertOrderLineValuesQueryBuilder = new StringBuilder();

        //5 (e)
        StringBuilder getItemPriceQueryBuilder = new StringBuilder("SELECT I_NAME, I_PRICE, I_ID FROM Item WHERE I_ID IN (");
        //building the query string
        for(int i=0; i<num_items; i++){
            int itemNumber = inputList.get(i).itemNumber;
            getItemPriceQueryBuilder.append((i < num_items-1) ? itemNumber + "," : itemNumber + ")");
        }

        Query getItemPrice = session.createQuery(new String(getItemPriceQueryBuilder));
        List<Object[]> itemDataList = getItemPrice.getResultList();
        if(itemDataList.size()!=num_items){
            System.out.println("Not equal");
            System.out.println(itemDataList.size() + " === "+ num_items);
            System.out.println(getItemPriceQueryBuilder.toString());
        }
        queryCount++;
        HashMap<Integer, Double> itemIdVsPrice = new HashMap<>();
        HashMap<Integer, String> itemIdVsName = new HashMap<>();
        for(Object[] o : itemDataList){
            itemIdVsPrice.put((Integer)o[2], (double)o[1]);
            itemIdVsName.put((Integer)o[2], (String)o[0]);
        }

        //initializing output list for each item
        List<ItemOutput> itemOutputs = new ArrayList<ItemOutput>();

        //5
        for(int i=0; i<num_items; i++){
            int itemNumber = inputList.get(i).itemNumber;
            int supplierWarehouseNumber = inputList.get(i).supplierWarehouseNumber;
            int quantity = inputList.get(i).quantity;
            double stockQuantity = stockQuantityList.get(i);
            //b
            int adjustedQuantity =  (int)stockQuantity - quantity;

            //c
            adjustedQuantity = (adjustedQuantity < 10) ? (adjustedQuantity + 100) : adjustedQuantity;

            //d
            String updateStockQuery = "UPDATE Stock SET S_QUANTITY = :s_quantity, S_YTD = S_YTD + :quantity, S_ORDER_CNT = S_ORDER_CNT + 1," +
                                      " S_REMOTE_CNT = S_REMOTE_CNT + (1 - :all_local) WHERE S_W_ID = :w_id AND S_I_ID = :i_id";
            Query updateStock = session.createQuery(updateStockQuery);
            updateStock.setParameter("s_quantity", (double) adjustedQuantity);
            updateStock.setParameter("quantity", (double) quantity);
            updateStock.setParameter("all_local", allLocal);
            updateStock.setParameter("w_id", supplierWarehouseNumber);
            updateStock.setParameter("i_id", itemNumber);
            updateStock.executeUpdate();
            queryCount++;

            //e
            double itemAmount = quantity * itemIdVsPrice.get(itemNumber);

            //f
            totalAmount += itemAmount;

            //g
            insertOrderLineValuesQueryBuilder.append(String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", N, D_ID, W_ID, i, itemNumber, supplierWarehouseNumber, quantity, itemAmount, null, "'"+"S_DIST_"+D_ID+"'"));
            insertOrderLineValuesQueryBuilder.append((i < num_items-1) ? ", " : "");

            //setting output data
            ItemOutput itemOutput = new ItemOutput();
            itemOutput.ITEM_NUMBER = i + 1;
            itemOutput.I_NAME = itemIdVsName.get(itemNumber);
            itemOutput.SUPPLIER_WAREHOUSE = supplierWarehouseNumber;
            itemOutput.QUANTITY = quantity;
            itemOutput.OL_AMOUNT = itemAmount;
            itemOutput.S_QUANTITY = (double) adjustedQuantity;
            itemOutputs.add(itemOutput);

        }

        //5 (g) - bulk insert instead of multiple single-inserts
        insertOrderLineQueryBuilder.append(insertOrderLineValuesQueryBuilder);
        String orderLineString = new String(insertOrderLineQueryBuilder);
        Query orderLineQuery = session.createNativeQuery(orderLineString);
        orderLineQuery.executeUpdate();
        queryCount++;

        //6
        totalAmount = totalAmount * (1 + d_tax + w_tax) * (1 - c_discount);

        //setting output data
        newOrderTransactionOutput.W_ID = W_ID;
        newOrderTransactionOutput.C_ID = C_ID;
        newOrderTransactionOutput.D_ID = D_ID;
        newOrderTransactionOutput.C_DISCOUNT = c_discount;

        newOrderTransactionOutput.W_TAX = w_tax;
        newOrderTransactionOutput.D_TAX = d_tax;
        newOrderTransactionOutput.O_ID = N;
        newOrderTransactionOutput.O_ENTRY_D = o_entry_d;
        newOrderTransactionOutput.NUM_ITEMS = num_items;
        newOrderTransactionOutput.TOTAL_AMOUNT = totalAmount;
        newOrderTransactionOutput.itemOutputs = itemOutputs;

        return newOrderTransactionOutput;
    }

    private int checkAllLocal(List<NewOrderTransactionInput> inputs, int W_ID){
        for(NewOrderTransactionInput input: inputs){
            if(input.supplierWarehouseNumber != W_ID)
                return 0;
        }
        return 1;
    }

    public void printOutput(NewOrderTransactionOutput output){
        System.out.println("-------------Output of New Order Transaction-------------");
        System.out.println("W_ID: " + output.W_ID);
        System.out.println("D_ID: " + output.D_ID);
        System.out.println("C_ID: " + output.C_ID);
        System.out.println("C_LAST: " + output.C_LAST);
        System.out.println("C_CREDIT: " + output.C_CREDIT);
        System.out.println("C_DISCOUNT: " + output.C_DISCOUNT);
        System.out.println("W_TAX: " + output.W_TAX);
        System.out.println("D_TAX: " + output.D_TAX);
        System.out.println("O_ID: " + output.O_ID);
        System.out.println("O_ENTRY_D: " + output.O_ENTRY_D);
        System.out.println("NUM_ITEMS: " + output.NUM_ITEMS);
        System.out.println("TOTAL_AMOUNT: " + output.TOTAL_AMOUNT);
        try {
            for (ItemOutput itemOutput : output.itemOutputs) {
                System.out.println("\n");
                System.out.println("ITEM_NUMBER: " + itemOutput.ITEM_NUMBER);
                System.out.println("I_NAME: " + itemOutput.I_NAME);
                System.out.println("SUPPLIER_WAREHOUSE: " + itemOutput.SUPPLIER_WAREHOUSE);
                System.out.println("QUANTITY: " + itemOutput.QUANTITY);
                System.out.println("OL_AMOUNT: " + itemOutput.OL_AMOUNT);
                System.out.println("S_QUANTITY: " + itemOutput.S_QUANTITY);
            }
        }catch (Exception e){ }
    }
}

class NewOrderTransactionOutput {
    //1
    Integer W_ID;
    Integer D_ID;
    Integer C_ID;
    String C_LAST;
    String C_CREDIT;
    Double C_DISCOUNT;
    //2
    Double W_TAX;
    Double D_TAX;
    //3
    Integer O_ID;
    String O_ENTRY_D;
    //4
    Integer NUM_ITEMS;
    Double TOTAL_AMOUNT;
    //5
    List<ItemOutput> itemOutputs;
}

class ItemOutput{
    Integer ITEM_NUMBER;
    String I_NAME;
    Integer SUPPLIER_WAREHOUSE;
    Integer QUANTITY;
    Double OL_AMOUNT;
    Double S_QUANTITY;
}
