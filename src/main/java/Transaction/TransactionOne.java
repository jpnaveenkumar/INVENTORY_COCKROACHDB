package Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class TransactionOne {

    void transaction1(Integer C_ID, Integer W_ID, Integer D_ID, Integer num_items){
        Framework framework = Framework.getInstance();
        Session session = framework.getSession();
        Transaction transaction = framework.startTransaction();

        Query q0 = session.createQuery("SELECT D_NEXT_O_ID from District where D_W_ID = :warehouseId and D_ID = :districtId");
        q0.setParameter("warehouseId", W_ID);
        q0.setParameter("districtId", D_ID);
        int N = (int) q0.getSingleResult();

        System.out.println("D_NEXT_O_ID: " + N);

        Query q1 = session.createQuery("UPDATE District SET D_NEXT_O_ID = D_NEXT_O_ID - 1 WHERE D_W_ID = :warehouseId AND D_ID = :districtId");
        q1.setParameter("warehouseId", W_ID);
        q1.setParameter("districtId", D_ID);
        q1.executeUpdate();

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

        List<T1Input> list = new ArrayList<T1Input>();
        double totalAmount = 0.0;

        //processing for all items
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
            Query getItemPrice = session.createQuery("SELECT I_PRICE FROM Item WHERE I_ID = :i_id");
            getItemPrice.setParameter("i_id", itemNumber);

            double itemPrice = (double) getItemPrice.getSingleResult();
            double itemAmount = quantity * itemPrice;

            //f
            totalAmount += itemAmount;

            //g
            String orderLine = "INSERT INTO OrderLine (OL_O_ID, OL_D_ID, OL_W_ID, OL_NUMBER, OL_I_ID, OL_SUPPLY_W_ID, OL_QUANTITY, OL_AMOUNT, OL_DELIVERY_D, OL_DIST_INFO) VALUES (:N, :d_id, :w_id, :i, :i_id, :supplier_w_id, :quantity, :item_amount, :n, :dist)";
            Query orderLineQuery = session.createQuery(orderLine);
            orderLineQuery.setParameter("N", N);
            orderLineQuery.setParameter("d_id", D_ID);
            orderLineQuery.setParameter("w_id", W_ID);
            orderLineQuery.setParameter("i", i);
            orderLineQuery.setParameter("i_id", itemNumber);
            orderLineQuery.setParameter("supplier_w_id", supplierWarehouseNumber);
            orderLineQuery.setParameter("quantity", quantity);
            orderLineQuery.setParameter("item_amount", itemAmount);
            orderLineQuery.setParameter("n", null);
            orderLineQuery.setParameter("dist", "S_DIST_"+D_ID);
            orderLineQuery.executeUpdate();
        }
        //get taxes and discount
        Query getDistrictTax = session.createQuery("SELECT D_TAX FROM District WHERE D_ID = :d_id");
        getDistrictTax.setParameter("d_id", D_ID);
        double d_tax = (double) getDistrictTax.getSingleResult();

        Query getWareHouseTax = session.createQuery("SELECT W_TAX FROM Warehouse WHERE W_ID = :w_id");
        getWareHouseTax.setParameter("w_id", W_ID);
        double w_tax = (double) getWareHouseTax.getSingleResult();

        Query getCustomerDiscount = session.createQuery("SELECT C_DISCOUNT FROM Customer WHERE C_ID = : c_id");
        getCustomerDiscount.setParameter("c_id", C_ID);
        double c_discount = (double) getCustomerDiscount.getSingleResult();

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

    public static void main(String args[]) {
        TransactionOne t1 = new TransactionOne();
        Framework framework = Framework.getInstance();
        framework.initHibernate(); // Initializing Hibernate
        //t1.transaction1(1279, 1, 1, 15);

        framework.destroy(); // Graceful shutdown of Hibernate
    }
}

class T1Input {
    Integer itemNumber;
    Integer supplierWarehouseNumber;
    Integer quantity;
}
