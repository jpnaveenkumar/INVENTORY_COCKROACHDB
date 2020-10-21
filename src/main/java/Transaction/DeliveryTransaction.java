package Transaction;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryTransaction {

    Integer serverId;

    public void transactionThree(int W_ID, int CARRIER_ID, Integer serverId){
        this.serverId = serverId;
        Framework framework = Framework.getInstance(serverId);
        Session session = framework.getSession();
        Transaction transaction = framework.startTransaction();

        List<Integer> smallestOrderNumber = new ArrayList<>();
        List<Integer> customerOfSmallestOrderNumber = new ArrayList<>();
        StringBuilder getSmallestOrderNumberQueryBuilder = new StringBuilder();

        for(int i=1;i<=10;i++){
            String formatted = String.format("(select o_id, o_c_id, %d as sortorder from orders where o_w_id = %d and o_d_id = %d and o_carrier_id = %s order by o_id limit 1)", i, W_ID, i, "'" + null + "'");
            getSmallestOrderNumberQueryBuilder.append(formatted);
            getSmallestOrderNumberQueryBuilder.append((i!=10) ? " UNION ALL " : "");
        }
        getSmallestOrderNumberQueryBuilder.append(" ORDER BY sortorder");
        System.out.println(getSmallestOrderNumberQueryBuilder.toString());
        Query q0 = session.createNativeQuery(getSmallestOrderNumberQueryBuilder.toString());

        //a
        for(Object[] o : (List<Object[]>) q0.getResultList()){
            smallestOrderNumber.add((int)o[0]);
            customerOfSmallestOrderNumber.add((int)o[1]);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        String dateTime = sdf.format(date);

        List<Double> sumOfOrderLineAmountList = new ArrayList<>();
        StringBuilder sumOfOrderLineAmountQueryBuilder = new StringBuilder();
        for(int i=1;i<=10;i++){
            int o_id = smallestOrderNumber.get(i-1);
            //d
            String getSumOfOrderLineAmountQuery = String.format("select sum(ol_amount) from orderline where ol_w_id = %d and ol_d_id = %d and ol_o_id = %d" + ((i<10) ? " UNION ALL " : ""), W_ID, i, o_id);
            sumOfOrderLineAmountQueryBuilder.append(getSumOfOrderLineAmountQuery);
        }
        sumOfOrderLineAmountList = (List<Double>) session.createNativeQuery(sumOfOrderLineAmountQueryBuilder.toString()).getResultList();

        for(int i=1;i<=10;i++){

            int o_id = smallestOrderNumber.get(i-1);
            //b
            Query updateCarrierId = session.createNativeQuery(String.format("UPDATE orders SET o_carrier_id = %s WHERE o_id = %d AND o_w_id = %d AND o_d_id = %d", "'" + CARRIER_ID +"'", o_id, W_ID, i));
            updateCarrierId.executeUpdate();

            //c
            Query updateOrderLines = session.createNativeQuery(String.format("UPDATE orderline SET ol_delivery_d = %s WHERE ol_o_id = %d AND ol_d_id = %d AND ol_w_id = %d", "'" + dateTime + "'", o_id, i, W_ID));
            updateOrderLines.executeUpdate();

            //d
//            Query getSumOfOrderLineAmount = session.createNativeQuery(String.format("select sum(ol_amount) from orderline where ol_w_id = :ol_w_id and ol_d_id = :ol_d_id and ol_o_id = :ol_o_id");
//            getSumOfOrderLineAmount.setParameter("ol_w_id", W_ID);
//            getSumOfOrderLineAmount.setParameter("ol_d_id", i);
//            getSumOfOrderLineAmount.setParameter("ol_o_id", o_id);

            //update customer
            Query updateCustomer = session.createNativeQuery("update customer set c_balance = c_balance + :b, c_delivery_cnt = c_delivery_cnt + 1 where c_w_id = :w_id and c_d_id = :i and c_id = :c_id");
            updateCustomer.setParameter("b", sumOfOrderLineAmountList.get(i-1));
            updateCustomer.setParameter("w_id", W_ID);
            updateCustomer.setParameter("i", i);
            updateCustomer.setParameter("c_id", customerOfSmallestOrderNumber.get(i-1));
            updateCustomer.executeUpdate();
        }
        framework.commitTransaction(transaction);
    }

}
