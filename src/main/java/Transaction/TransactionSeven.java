package Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TransactionSeven {

    private List<T7Output> transactionSeven(){
        Framework framework = Framework.getInstance();
        Session session = framework.getSession();
        Transaction transaction = framework.startTransaction();
        Query topBalanceQuery = session.createNativeQuery("select c.c_first, c.c_middle, c.c_last, c.c_balance, w.w_name, d.d_name from warehouse w, district d, (select c_id, c_w_id, c_d_id, c_first, c_middle, c_last, c_balance from customer order by c_balance desc limit 10) as c where w.w_id = c.c_w_id and d.d_w_id = c.c_w_id and d.d_id = c.c_d_id limit 10;");
        List<Object[]> list = topBalanceQuery.getResultList();
        List<T7Output> outputList = new ArrayList<>();
        for(Object[] o : list){
            T7Output output = new T7Output();
            output.C_FIRST = (String) o[0];
            output.C_MIDDLE = (String) o[1];
            output.C_LAST = (String) o[2];
            output.C_BALANCE = (Double) o[3];
            output.W_NAME = (String) o[4];
            output.D_NAME = (String) o[5];
            outputList.add(output);
        }
        framework.commitTransaction(transaction);
        return outputList;
    }

    private void printOutPut(List<T7Output> outputList){
        System.out.println("----------Top 10 Customers ranked by remaining balance in descending order----------");
        int i = 0;
        for(T7Output customer: outputList){
            System.out.println("\n-----"+" Customer " + i + " -----");
            System.out.println("First name: " + customer.C_FIRST);
            System.out.println("Middle name: " + customer.C_MIDDLE);
            System.out.println("Last name: " + customer.C_LAST);
            System.out.println("Balance: " + customer.C_BALANCE);
            System.out.println("Warehouse name: " + customer.W_NAME);
            System.out.println("District name: " + customer.D_NAME);
            i++;
        }
    }

    public static void main(String[] args){
        Framework framework = Framework.getInstance();
        framework.initHibernate(); // Initializing Hibernate
        TransactionSeven t7 = new TransactionSeven();
        Instant start = Instant.now();
        List<T7Output> outputList = t7.transactionSeven();
        Instant end = Instant.now();    //calculating end time
        t7.printOutPut(outputList);
        Duration timeElapsed = Duration.between(start, end);

        System.out.println("\nTime taken to complete this transaction: "+ timeElapsed.toMillis() +" milliseconds");
        System.out.println("-------------DONE-------------");

        framework.destroy();
    }
}

class T7Output{
    String C_FIRST;
    String C_MIDDLE;
    String C_LAST;
    Double C_BALANCE;
    String W_NAME;
    String D_NAME;
}