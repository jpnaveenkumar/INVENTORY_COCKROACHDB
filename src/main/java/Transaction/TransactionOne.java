package Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

import java.time.Duration;
import java.time.Instant;

public class TransactionOne {

    private T1Output transaction1(int C_ID, int W_ID, int D_ID, int num_items, List<T1Input> inputList){

        //Note : Numbers or alphabet comments above a sequence of code lines indicate which part of problem statement it is related to. Refer section 2.1, Processing steps in project document.

        Framework framework = Framework.getInstance();
        Session session = framework.getSession();
        Transaction transaction = framework.startTransaction();

        //List<T1Input> inputList = new ArrayList<T1Input>();
        T1Output t1Output = new T1Output();

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
        t1Output.C_LAST = (String) districtList[4];
        t1Output.C_CREDIT = (String) districtList[5];

        //2
        Query q1 = session.createQuery("UPDATE District SET D_NEXT_O_ID = D_NEXT_O_ID - 1 WHERE D_W_ID = :warehouseId AND D_ID = :districtId");
        q1.setParameter("warehouseId", W_ID);
        q1.setParameter("districtId", D_ID);
        q1.executeUpdate();

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

        //5 (g)
        StringBuilder insertOrderLineQueryBuilder = new StringBuilder("INSERT INTO OrderLine(OL_O_ID, OL_D_ID, OL_W_ID, OL_NUMBER, OL_I_ID, OL_SUPPLY_W_ID, OL_QUANTITY, OL_AMOUNT, OL_DELIVERY_D, OL_DIST_INFO) VALUES ");
        StringBuilder insertOrderLineValuesQueryBuilder = new StringBuilder();

        //5 (e)
        StringBuilder getItemPriceQueryBuilder = new StringBuilder("SELECT I_NAME, I_PRICE FROM Item WHERE I_ID IN (");
        //building the query string
        for(int i=0; i<num_items; i++){
            int itemNumber = inputList.get(i).itemNumber;
            getItemPriceQueryBuilder.append((i < num_items-1) ? itemNumber + "," : itemNumber + ")");
        }

        Query getItemPrice = session.createQuery(new String(getItemPriceQueryBuilder));
        List<Object[]> itemDataList = getItemPrice.getResultList();
        List<Double> itemPriceList = new ArrayList<>();
        List<String> itemNameList = new ArrayList<>();
        for(Object[] o : itemDataList){
            itemNameList.add((String) o[0]);
            itemPriceList.add((double) o[1]);
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

            //e
            double itemAmount = quantity * itemPriceList.get(i);

            //f
            totalAmount += itemAmount;

            //g
            insertOrderLineValuesQueryBuilder.append(String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", N, D_ID, W_ID, i, itemNumber, supplierWarehouseNumber, quantity, itemAmount, null, "'"+"S_DIST_"+D_ID+"'"));
            insertOrderLineValuesQueryBuilder.append((i < num_items-1) ? ", " : "");

            //setting output data
            ItemOutput itemOutput = new ItemOutput();
            itemOutput.ITEM_NUMBER = i + 1;
            itemOutput.I_NAME = itemNameList.get(i);
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

        //6
        totalAmount = totalAmount * (1 + d_tax + w_tax) * (1 - c_discount);

        //setting output data
        t1Output.W_ID = W_ID;
        t1Output.C_ID = C_ID;
        t1Output.D_ID = D_ID;
        t1Output.C_DISCOUNT = c_discount;

        t1Output.W_TAX = w_tax;
        t1Output.D_TAX = d_tax;
        t1Output.O_ID = N;
        t1Output.O_ENTRY_D = o_entry_d;
        t1Output.NUM_ITEMS = num_items;
        t1Output.TOTAL_AMOUNT = totalAmount;
        t1Output.itemOutputs = itemOutputs;

        framework.commitTransaction(transaction);
        return t1Output;
    }

    private int checkAllLocal(List<T1Input> inputs, int W_ID){
        for(T1Input input: inputs){
            if(input.supplierWarehouseNumber != W_ID)
                return 0;
        }
        return 1;
    }

    //function which can be used for unit testing
    private void test(){
        Framework framework = Framework.getInstance();
        Session session = framework.getSession();
        Transaction transaction = framework.startTransaction();

        int W_ID = 3;
        int D_ID = 3;
        int C_ID = 1281;
        int num_items = 10;
        int N = 3000;
        int allLocal = 1;
        String o_entry_d = String.valueOf(java.time.LocalTime.now());
        String s2 = String.format("insert into Order(O_W_ID, O_D_ID, O_ID, O_C_ID, O_CARRIER_ID, O_OL_CNT, O_ALL_LOCAL, O_ENTRY_D) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)");
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

        session.flush();
        framework.commitTransaction(transaction);
    }

    private void printOutput(T1Output output){
        System.out.println("-------------Transaction 1 has ended; Showing outputs below-------------");
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
        for(ItemOutput itemOutput : output.itemOutputs){
            System.out.println("\n");
            System.out.println("ITEM_NUMBER: " + itemOutput.ITEM_NUMBER);
            System.out.println("I_NAME: " + itemOutput.I_NAME);
            System.out.println("SUPPLIER_WAREHOUSE: " + itemOutput.SUPPLIER_WAREHOUSE);
            System.out.println("QUANTITY: " + itemOutput.QUANTITY);
            System.out.println("OL_AMOUNT: " + itemOutput.OL_AMOUNT);
            System.out.println("S_QUANTITY: " + itemOutput.S_QUANTITY);
        }
    }

    private List<T1Input> createTestInput(){
        List<T1Input> list = new ArrayList<T1Input>();
        list.add(new T1Input(1,2,10));
        list.add(new T1Input(2,2,10));
        list.add(new T1Input(3,2,10));
        list.add(new T1Input(4,2,10));
        list.add(new T1Input(5,2,10));
        list.add(new T1Input(6,2,10));
        list.add(new T1Input(7,2,10));
        list.add(new T1Input(8,2,10));
        list.add(new T1Input(9,2,10));
        list.add(new T1Input(10,2,10));
        return list;
    }

    public static void main(String args[]) {

        TransactionOne t1 = new TransactionOne();
        Framework framework = Framework.getInstance();
        framework.initHibernate(); // Initializing Hibernate

        List<T1Input> list = t1.createTestInput();
        Instant start = Instant.now();  //calculating start time
        T1Output output = t1.transaction1(1300, 5, 5, 10, list);
        Instant end = Instant.now();    //calculating end time
        Duration timeElapsed = Duration.between(start, end);
        t1.printOutput(output);
        System.out.println("\nTime taken to complete this transaction: "+ timeElapsed.toMillis() +" milliseconds");
        System.out.println("-------------DONE-------------");
        //t1.test();

        framework.destroy(); // Graceful shutdown of Hibernate
    }
}

class T1Input {
    int itemNumber;
    int supplierWarehouseNumber;
    int quantity;

    T1Input(int itemNumber, int supplierWarehouseNumber, int quantity){
        this.itemNumber = itemNumber;
        this.supplierWarehouseNumber = supplierWarehouseNumber;
        this.quantity = quantity;
    }
}

class T1Output {
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
