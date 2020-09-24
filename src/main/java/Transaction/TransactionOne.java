package Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class TransactionOne {

    void transaction1(Integer C_ID, Integer W_ID, Integer D_ID, Integer num_items){

        //Note : Numbers or alphabet comments above a sequence of code lines indicate which part of problem statement it is related to. Refer section 2.1, Processing steps in project document.

        Framework framework = Framework.getInstance();
        Session session = framework.getSession();
        Transaction transaction = framework.startTransaction();

        List<T1Input> list = new ArrayList<T1Input>();

        //1, 6
        Query q0 = session.createNativeQuery("SELECT D_NEXT_O_ID, D_TAX, W_TAX, C_DISCOUNT from district, warehouse, customer where D_ID = :d_id and D_W_ID = :w_id and W_ID = :w_id and C_ID = :c_id and C_W_ID = :w_id and C_D_ID = :d_id");
        q0.setParameter("d_id", D_ID);
        q0.setParameter("w_id", W_ID);
        q0.setParameter("c_id", C_ID);
        Object[] districtList = (Object[]) q0.getSingleResult();
        int N = (int) districtList[0];
        double d_tax = (double) districtList[1];
        double w_tax = (double) districtList[2];
        double c_discount = (double) districtList[3];

        //2
        Query q1 = session.createQuery("UPDATE District SET D_NEXT_O_ID = D_NEXT_O_ID - 1 WHERE D_W_ID = :warehouseId AND D_ID = :districtId");
        q1.setParameter("warehouseId", W_ID);
        q1.setParameter("districtId", D_ID);
        q1.executeUpdate();

        //3
        //int allLocal = checkAllLocal(inputs, W_ID);
        int allLocal = 1;
        String s2 = "INSERT INTO Order (O_W_ID, O_D_ID, O_ID, O_C_ID, O_CARRIER_ID, O_OL_CNT, O_ALL_LOCAL, O_ENTRY_D) VALUES (:w_id, :d_id, :n, :c_id, :carrier_id, :ol_cnt, :all_local, :entry_d)";
        Query q2 = session.createQuery(s2);
        q2.setParameter("w_id", W_ID);
        q2.setParameter("d_id", D_ID);
        q2.setParameter("n", N);
        q2.setParameter("c_id", C_ID);
        q2.setParameter("carrier_id", null);
        q2.setParameter("ol_cnt", num_items);
        q2.setParameter("all_local", allLocal); //should change
        q2.setParameter("entry_d", String.valueOf(java.time.LocalTime.now()));
        q2.executeUpdate();

        //4
        double totalAmount = 0.0;

        //5 (g)
        StringBuilder insertOrderLineQueryBuilder = new StringBuilder("INSERT INTO OrderLine (OL_O_ID, OL_D_ID, OL_W_ID, OL_NUMBER, OL_I_ID, OL_SUPPLY_W_ID, OL_QUANTITY, OL_AMOUNT, OL_DELIVERY_D, OL_DIST_INFO) VALUES ");
        StringBuilder insertOrderLineValuesQueryBuilder = new StringBuilder();

        //5 (e)
        StringBuilder getItemPriceQueryBuilder = new StringBuilder("SELECT I_PRICE FROM Item WHERE I_ID IN (");
        for(int i=0; i<num_items; i++){
            int itemNumber = list.get(i).itemNumber;
            getItemPriceQueryBuilder.append((i < num_items-1) ? itemNumber + "," : itemNumber + ")");
        }
        Query getItemPrice = session.createQuery(new String(getItemPriceQueryBuilder));
        List<Double> itemPriceList = getItemPrice.getResultList();

        //5
        for(int i=0; i<num_items; i++){
            int itemNumber = list.get(i).itemNumber;
            int supplierWarehouseNumber = list.get(i).supplierWarehouseNumber;
            int quantity = list.get(i).quantity;

            //a
            Query getStockQuantity = session.createQuery("SELECT S_QUANTITY FROM Stock WHERE S_W_ID = :w_id AND S_I_ID = :i_id");
            getStockQuantity.setParameter("w_id", supplierWarehouseNumber);
            getStockQuantity.setParameter("i_id", itemNumber);
            int stockQuantity = (int) getStockQuantity.getSingleResult();

            //b
            int adjustedQuantity = stockQuantity - quantity;

            //c
            adjustedQuantity = (adjustedQuantity < 10) ? (adjustedQuantity + 100) : adjustedQuantity;

            //d
            String updateStockQuery = "UPDATE Stock SET S_QUANTITY = :s_quantity, S_YTD = S_YTD + :quantity, S_ORDER_CNT = S_ORDER_CNT + 1, S_REMOTE_CNT = S_REMOTE_CNT + (1 - :all_local) WHERE S_W_ID = :w_id AND S_I_ID = :i_id";
            Query updateStock = session.createQuery(updateStockQuery);
            updateStock.setParameter("s_quantity", adjustedQuantity);
            updateStock.setParameter("quantity", quantity);
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
        }

        //5 (g) - bulk insert instead of multiple single-inserts
        insertOrderLineQueryBuilder.append(insertOrderLineValuesQueryBuilder);
        String orderLineString = new String(insertOrderLineQueryBuilder);
        Query orderLineQuery = session.createNativeQuery(orderLineString);
        orderLineQuery.executeUpdate();

        //6
        totalAmount = totalAmount * (1 + d_tax + w_tax) * (1 - c_discount);




        framework.commitTransaction(transaction);
    }

    public int checkAllLocal(List<T1Input> inputs, int W_ID){
        for(T1Input input: inputs){
            if(input.supplierWarehouseNumber != W_ID)
                return 0;
        }
        return 1;
    }

    public void sampletest(){
        Framework framework = Framework.getInstance();
        Session session = framework.getSession();
        Transaction transaction = framework.startTransaction();

        int W_ID = 3;
        int D_ID = 3;
        int C_ID = 1281;

        List<T1Input> list = new ArrayList<T1Input>();
        list.add(new T1Input(1,2,10));
        list.add(new T1Input(2,2,10));
        list.add(new T1Input(3,2,10));
        list.add(new T1Input(4,2,10));
        double totalAmount = 0.0;


        int num_items = 4;
        StringBuilder p = new StringBuilder("SELECT I_PRICE FROM Item WHERE I_ID IN (");

        for(int i=0; i<num_items; i++){
            int itemNumber = list.get(i).itemNumber;
            p.append((i < num_items-1) ? itemNumber + "," : itemNumber + ")");
        }
        Query getItemPrice = session.createQuery(new String(p));
        List<Double> itemPriceList = getItemPrice.getResultList();

        for(double itemPrice : itemPriceList)
            System.out.println(itemPrice);

        for(int i=0; i<num_items; i++){
            int itemNumber = list.get(i).itemNumber;
            int supplierWarehouseNumber = list.get(i).supplierWarehouseNumber;
            int quantity = list.get(i).quantity;

            double itemAmount = quantity * itemPriceList.get(i);
            totalAmount += itemAmount;
        }



        session.flush();
        framework.commitTransaction(transaction);
    }

    public static void main(String args[]) {
        TransactionOne t1 = new TransactionOne();
        Framework framework = Framework.getInstance();
        framework.initHibernate(); // Initializing Hibernate
        //t1.transaction1(1279, 1, 1, 15);
        t1.sampletest();

        framework.destroy(); // Graceful shutdown of Hibernate
    }
}

class T1Input {
    Integer itemNumber;
    Integer supplierWarehouseNumber;
    Integer quantity;

    T1Input(int itemNumber, int supplierWarehouseNumber, int quantity){
        this.itemNumber = itemNumber;
        this.supplierWarehouseNumber = supplierWarehouseNumber;
        this.quantity = quantity;
    }
}
