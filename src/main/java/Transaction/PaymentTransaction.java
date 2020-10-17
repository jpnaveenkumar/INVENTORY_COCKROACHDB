package Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.Duration;
import java.time.Instant;

public class PaymentTransaction {

    Framework framework;
    Session session;

    public PaymentTransaction(){
        this.framework = Framework.getInstance();
        this.session = this.framework.getSession();
    }

    public PaymentOutput processPaymentTransaction(int C_W_ID, int C_D_ID, int C_ID, double PAYMENT){
        System.out.println("-------------Payment transaction-------------");
        Instant start = Instant.now();

        PaymentOutput paymentOutput = new PaymentOutput();

        Transaction transaction = framework.startTransaction();

        Query updateWareHouse = session.createNativeQuery("UPDATE warehouse set W_YTD = W_YTD + :payment WHERE W_ID = :c_w_id");
        updateWareHouse.setParameter("payment", PAYMENT);
        updateWareHouse.setParameter("c_w_id", C_W_ID);
        updateWareHouse.executeUpdate();

        Query updateDistrict = session.createNativeQuery("UPDATE district set D_YTD = D_YTD + :payment WHERE D_W_ID = :c_w_id AND D_ID = :c_d_id");
        updateDistrict.setParameter("payment", PAYMENT);
        updateDistrict.setParameter("c_w_id", C_W_ID);
        updateDistrict.setParameter("c_d_id", C_D_ID);
        updateDistrict.executeUpdate();

        Query updateCustomer = session.createNativeQuery("UPDATE customer SET C_BALANCE = C_BALANCE - :payment, C_YTD_PAYMENT = C_YTD_PAYMENT + :payment, C_PAYMENT_CNT = C_PAYMENT_CNT + 1 WHERE C_W_ID = :c_w_id AND C_D_ID = :c_d_id AND C_ID = :c_id");
        updateCustomer.setParameter("payment", PAYMENT);
        updateCustomer.setParameter("c_w_id", C_W_ID);
        updateCustomer.setParameter("c_d_id", C_D_ID);
        updateCustomer.setParameter("c_id", C_ID);
        updateCustomer.executeUpdate();

        Query getCustomerData = session.createNativeQuery("SELECT C_W_ID, C_D_ID, C_ID, C_FIRST, C_MIDDLE, C_LAST, C_STREET_1, C_STREET_2, C_CITY, C_STATE, C_ZIP, C_PHONE, C_SINCE, C_CREDIT, C_CREDIT_LIM, C_DISCOUNT, C_BALANCE FROM customer where C_W_ID = :c_w_id AND C_D_ID = :c_d_id AND C_ID = :c_id");
        getCustomerData.setParameter("c_w_id", C_W_ID);
        getCustomerData.setParameter("c_d_id", C_D_ID);
        getCustomerData.setParameter("c_id", C_ID);
        Object[] result = (Object[]) getCustomerData.getSingleResult();
        paymentOutput.C_W_ID = (int) result[0];
        paymentOutput.C_D_ID = (int) result[1];
        paymentOutput.C_ID = (int) result[2];
        paymentOutput.C_FIRST = (String) result[3];
        paymentOutput.C_MIDDLE = (String) result[4];
        paymentOutput.C_LAST = (String) result[5];
        paymentOutput.C_STREET_1 = (String) result[6];
        paymentOutput.C_STREET_2 = (String) result[7];
        paymentOutput.C_CITY = (String) result[8];
        paymentOutput.C_STATE = (String) result[9];
        paymentOutput.C_ZIP = (String) result[10];
        paymentOutput.C_PHONE = (String) result[11];
        paymentOutput.C_SINCE = (String) result[12];
        paymentOutput.C_CREDIT = (String) result[13];
        paymentOutput.C_CREDIT_LIM = (double) result[14];
        paymentOutput.C_DISCOUNT = (double) result[15];
        paymentOutput.C_BALANCE = (double) result[16];

        Query getWarehouseData = session.createNativeQuery("SELECT W_STREET_1, W_STREET2, W_CITY, W_STATE, W_ZIP FROM warehouse WHERE W_ID = :c_w_id");
        getWarehouseData.setParameter("c_w_id", C_W_ID);
        Object[] result2 = (Object[]) getWarehouseData.getSingleResult();
        paymentOutput.W_STREET_1 = (String) result2[0];
        paymentOutput.W_STREET_2 = (String) result2[1];
        paymentOutput.W_CITY = (String) result2[2];
        paymentOutput.W_STATE = (String) result2[3];
        paymentOutput.W_ZIP = (String) result2[4];

        Query getDistrictData = session.createNativeQuery("SELECT D_STREET_1, D_STREET_2, D_CITY, D_STATE, D_ZIP FROM district WHERE D_W_ID = :c_w_id AND D_ID = :c_d_id");
        getDistrictData.setParameter("c_w_id", C_W_ID);
        getDistrictData.setParameter("c_d_id", C_D_ID);
        Object[] result3 = (Object[]) getDistrictData.getSingleResult();
        paymentOutput.D_STREET_1 = (String) result3[0];
        paymentOutput.D_STREET_2 = (String) result3[1];
        paymentOutput.D_CITY = (String) result3[2];
        paymentOutput.D_STATE = (String) result3[3];
        paymentOutput.D_ZIP = (String) result3[4];

        paymentOutput.PAYMENT = PAYMENT;

        framework.commitTransaction(transaction);

        Instant end = Instant.now();    //calculating end time
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("\nTime taken to complete this transaction: "+ timeElapsed.toMillis() +" milliseconds");
        //System.out.println("-------------DONE-------------");

        return paymentOutput;
    }

    public void printOutput(PaymentOutput output){
        System.out.println("-------------Transaction 2 has ended; Showing outputs below-------------");
        System.out.println("C_W_ID: " + output.C_W_ID);
        System.out.println("C_D_ID: " + output.C_D_ID);
        System.out.println("C_ID: " + output.C_ID);
        System.out.println("C_FIRST: " + output.C_FIRST);
        System.out.println("C_MIDDLE: " + output.C_MIDDLE);
        System.out.println("C_LAST: " + output.C_LAST);
        System.out.println("C_STREET_1: " + output.C_STREET_1);
        System.out.println("C_STREET_2: " + output.C_STREET_2);
        System.out.println("C_CITY: " + output.C_CITY);
        System.out.println("C_STATE: " + output.C_STATE);
        System.out.println("C_ZIP: " + output.C_ZIP);
        System.out.println("C_PHONE: " + output.C_PHONE);
        System.out.println("C_SINCE: " + output.C_SINCE);
        System.out.println("C_CREDIT: " + output.C_CREDIT);
        System.out.println("C_CREDIT_LIM: " + output.C_CREDIT_LIM);
        System.out.println("C_DISCOUNT: " + output.C_DISCOUNT);
        System.out.println("C_BALANCE: " + output.C_BALANCE);

        System.out.println("W_STREET_1: " + output.W_STREET_1);
        System.out.println("W_STREET_2: " + output.W_STREET_2);
        System.out.println("W_CITY: " + output.W_CITY);
        System.out.println("W_STATE: " + output.W_STATE);
        System.out.println("W_ZIP: " + output.W_ZIP);

        System.out.println("D_STREET_1: " + output.D_STREET_1);
        System.out.println("D_STREET_2: " + output.D_STREET_2);
        System.out.println("D_CITY: " + output.D_CITY);
        System.out.println("D_STATE: " + output.D_STATE);
        System.out.println("D_ZIP: " + output.D_ZIP);

        System.out.println("PAYMENT: " + output.PAYMENT);
    }

    public static void main(String args[]) {
        PaymentTransaction t2 = new PaymentTransaction();
        t2.framework.initHibernate(); // Initializing Hibernate
        Instant start = Instant.now();
        PaymentOutput output = t2.processPaymentTransaction(5, 5, 5,1.0);
        Instant end = Instant.now();    //calculating end time
        Duration timeElapsed = Duration.between(start, end);
        t2.printOutput(output);
        System.out.println("\nTime taken to complete this transaction: "+ timeElapsed.toMillis() +" milliseconds");
        System.out.println("-------------DONE-------------");
        t2.framework.destroy(); // Graceful shutdown of Hibernate
    }
}

class PaymentOutput {
    //customer
    int C_W_ID;
    int C_D_ID;
    int C_ID;
    String C_FIRST;
    String C_MIDDLE;
    String C_LAST;
    String C_STREET_1;
    String C_STREET_2;
    String C_CITY;
    String C_STATE;
    String C_ZIP;
    String C_PHONE;
    String C_SINCE;
    String C_CREDIT;
    Double C_CREDIT_LIM;
    Double C_DISCOUNT;
    Double C_BALANCE;

    //warehouse
    String W_STREET_1;
    String W_STREET_2;
    String W_CITY;
    String W_STATE;
    String W_ZIP;

    //district
    String D_STREET_1;
    String D_STREET_2;
    String D_CITY;
    String D_STATE;
    String D_ZIP;

    Double PAYMENT;
}
