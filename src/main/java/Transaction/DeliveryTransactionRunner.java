package Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

class DeliveryTransactionExecutor extends Thread {
    int W_ID;
    int D_ID;
    int CARRIER_ID;
    Framework framework;
    Session session;
    int serverId;
    CountDownLatch latch;

    public DeliveryTransactionExecutor(int W_ID, int D_ID, int CARRIER_ID, int serverId, CountDownLatch latch){
        this.W_ID = W_ID;
        this.D_ID = D_ID;
        this.CARRIER_ID = CARRIER_ID;
        this.framework = Framework.getInstance(serverId);
        this.session = this.framework.getSession();
        this.serverId = serverId;
        this.latch = latch;

    }
    public void processDeliveryTransactionRunner(){
        Transaction transaction = framework.startTransaction();
        String selectSmallerOrderIDQuery = String.format("(select o_id, o_c_id from orders where o_w_id = %d and o_d_id = %d and o_carrier_id = %s order by o_id limit 1)", W_ID, this.D_ID , "'" + null + "'");
        Query q0 = session.createNativeQuery(selectSmallerOrderIDQuery);
        Object[] o_obj = (Object[])q0.getSingleResult();
        Integer o_id = (Integer) o_obj[0]; //N
        Integer o_c_id = (Integer) o_obj[1]; //C

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        String dateTime = sdf.format(date);

        String getSumOfOrderLineAmountQuery = String.format("select sum(ol_amount) from orderline where ol_w_id = %d and ol_d_id = %d and ol_o_id = %d", W_ID, D_ID, o_id);
        Query q1 = session.createNativeQuery(getSumOfOrderLineAmountQuery);
        Double sum_ol_amount = (Double)q1.getSingleResult();

        System.out.println("Processing oldest yet-to-be-delivered order for district number: " + D_ID);

        Query updateCarrierId = session.createNativeQuery(String.format("UPDATE orders SET o_carrier_id = %s WHERE o_id = %d AND o_w_id = %d AND o_d_id = %d", "'" + CARRIER_ID +"'", o_id, W_ID, D_ID));
        updateCarrierId.executeUpdate();

        Query updateOrderLines = session.createNativeQuery(String.format("UPDATE orderline SET ol_delivery_d = %s WHERE ol_o_id = %d AND ol_d_id = %d AND ol_w_id = %d", "'" + dateTime + "'", o_id, D_ID, W_ID));
        updateOrderLines.executeUpdate();

        Query updateCustomer = session.createNativeQuery("update customer set c_balance = c_balance + :b, c_delivery_cnt = c_delivery_cnt + 1 where c_w_id = :w_id and c_d_id = :i and c_id = :c_id");
        updateCustomer.setParameter("b", sum_ol_amount);
        updateCustomer.setParameter("w_id", W_ID);
        updateCustomer.setParameter("i", D_ID);
        updateCustomer.setParameter("c_id", o_c_id);
        updateCustomer.executeUpdate();
        framework.commitTransaction(transaction);
        latch.countDown();
    }
    public void run(){
        processDeliveryTransactionRunner();
    }
}

public class DeliveryTransactionRunner{
    public void processDeliveryTransaction(int W_ID, int CARRIER_ID, int serverId) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        for(int districtNumber = 1; districtNumber <= 10; districtNumber++){
            DeliveryTransactionExecutor deliveryTransactionExecutor = new DeliveryTransactionExecutor(W_ID, districtNumber, CARRIER_ID, serverId, latch);
            deliveryTransactionExecutor.start();
        }
        latch.await();
    }

//    public static void main(String[] args) throws InterruptedException {
//        Framework framework = Framework.getInstance(0);
//        framework.initHibernate();
//        DeliveryTransactionRunner driver = new DeliveryTransactionRunner();
//        driver.processDeliveryTransaction(1, 7, 0);
//        framework.destroy();
//    }
}