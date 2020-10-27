package Transaction;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

class DeliveryTransactionExecutor extends Thread {
    int W_ID;
    int D_ID;
    int CARRIER_ID;
    int serverId;
    static Double timeTakenSoFar = 0.0;
    CountDownLatch latch;
    static Logger log = Logger.getLogger(DeliveryTransactionExecutor.class.getName());
    public DeliveryTransactionExecutor(int W_ID, int D_ID, int CARRIER_ID, int serverId, CountDownLatch latch){
        this.W_ID = W_ID;
        this.D_ID = D_ID;
        this.CARRIER_ID = CARRIER_ID;
        this.serverId = serverId;
        this.latch = latch;

    }
    public void processDeliveryTransactionRunner(Session session){
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
    }
    public void run(){
        int currentTransactionRetryCount = 0;
        Session session = null;
        Double timeTaken = 0.0;
        Integer transactionRetryThreshold = 30;
        Transaction transaction = null;
        while (currentTransactionRetryCount < transactionRetryThreshold){
            Instant startTime = Instant.now();
            try{
                Framework framework = Framework.getInstance(serverId);
                session = framework.getSession();
                currentTransactionRetryCount++;
                transaction = framework.startTransaction();
                processDeliveryTransactionRunner(session);
                transaction.commit();
                Instant endTime = Instant.now();
                Duration timeElapsed = Duration.between(startTime, endTime);
                timeTaken = (double) timeElapsed.toMillis() / 1000;
                timeTakenSoFar = timeTakenSoFar + timeTaken;
                latch.countDown();
                System.out.println("Committing transaction successfully with retry count : "+currentTransactionRetryCount);
                break;
            }catch (Exception e){
                log.error("Error occurred while committing delivery transaction retry count :"+currentTransactionRetryCount + Thread.currentThread().getName(), e);
                System.out.println("Error occurred while delivery committing transaction retry count : "+currentTransactionRetryCount + Thread.currentThread().getName());
                if(currentTransactionRetryCount == transactionRetryThreshold){
                    latch.countDown();
                }
                try {
                    //int sleepMillis = (int)(Math.pow(2, currentTransactionRetryCount) * 100) + new Random().nextInt(100);
                    int sleepMillis = (int)(Math.pow(2, Math.min(currentTransactionRetryCount,11)) * 100) + new Random().nextInt(100);
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
//                if(transaction != null) transaction.rollback();
//                if(session != null) session.close();
            }
        }
    }
}

public class DeliveryTransactionRunner{

    public Double processDeliveryTransaction(int W_ID, int CARRIER_ID, int serverId) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        for(int districtNumber = 1; districtNumber <= 10; districtNumber++){
            DeliveryTransactionExecutor deliveryTransactionExecutor = new DeliveryTransactionExecutor(W_ID, districtNumber, CARRIER_ID, serverId, latch);
            deliveryTransactionExecutor.start();
        }
        latch.await();
        return DeliveryTransactionExecutor.timeTakenSoFar;
    }

//    public static void main(String[] args) throws InterruptedException {
//        Framework framework = Framework.getInstance(0);
//        framework.initHibernate();
//        DeliveryTransactionRunner driver = new DeliveryTransactionRunner();
//        driver.processDeliveryTransaction(1, 7, 0);
//        framework.destroy();
//    }
}