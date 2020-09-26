package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {
    @EmbeddedId
    CustomerId customerId;
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
    Double C_YTD_PAYMENT;
    Integer C_PAYMENT_CNT;
    Integer C_DELIVERY_CNT;
    String C_DATA;

    public CustomerId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public String getC_FIRST() {
        return C_FIRST;
    }

    public void setC_FIRST(String c_FIRST) {
        C_FIRST = c_FIRST;
    }

    public String getC_MIDDLE() {
        return C_MIDDLE;
    }

    public void setC_MIDDLE(String c_MIDDLE) {
        C_MIDDLE = c_MIDDLE;
    }

    public String getC_LAST() {
        return C_LAST;
    }

    public void setC_LAST(String c_LAST) {
        C_LAST = c_LAST;
    }

    public String getC_STREET_1() {
        return C_STREET_1;
    }

    public void setC_STREET_1(String c_STREET_1) {
        C_STREET_1 = c_STREET_1;
    }

    public String getC_STREET_2() {
        return C_STREET_2;
    }

    public void setC_STREET_2(String c_STREET_2) {
        C_STREET_2 = c_STREET_2;
    }

    public String getC_CITY() {
        return C_CITY;
    }

    public void setC_CITY(String c_CITY) {
        C_CITY = c_CITY;
    }

    public String getC_STATE() {
        return C_STATE;
    }

    public void setC_STATE(String c_STATE) {
        C_STATE = c_STATE;
    }

    public String getC_ZIP() {
        return C_ZIP;
    }

    public void setC_ZIP(String c_ZIP) {
        C_ZIP = c_ZIP;
    }

    public String getC_PHONE() {
        return C_PHONE;
    }

    public void setC_PHONE(String c_PHONE) {
        C_PHONE = c_PHONE;
    }

    public String getC_SINCE() {
        return C_SINCE;
    }

    public void setC_SINCE(String c_SINCE) {
        C_SINCE = c_SINCE;
    }

    public String getC_CREDIT() {
        return C_CREDIT;
    }

    public void setC_CREDIT(String c_CREDIT) {
        C_CREDIT = c_CREDIT;
    }

    public Double getC_CREDIT_LIM() {
        return C_CREDIT_LIM;
    }

    public void setC_CREDIT_LIM(Double c_CREDIT_LIM) {
        C_CREDIT_LIM = c_CREDIT_LIM;
    }

    public Double getC_DISCOUNT() {
        return C_DISCOUNT;
    }

    public void setC_DISCOUNT(Double c_DISCOUNT) {
        C_DISCOUNT = c_DISCOUNT;
    }

    public Double getC_BALANCE() {
        return C_BALANCE;
    }

    public void setC_BALANCE(Double c_BALANCE) {
        C_BALANCE = c_BALANCE;
    }

    public Double getC_YTD_PAYMENT() {
        return C_YTD_PAYMENT;
    }

    public void setC_YTD_PAYMENT(Double c_YTD_PAYMENT) {
        C_YTD_PAYMENT = c_YTD_PAYMENT;
    }

    public Integer getC_PAYMENT_CNT() {
        return C_PAYMENT_CNT;
    }

    public void setC_PAYMENT_CNT(Integer c_PAYMENT_CNT) {
        C_PAYMENT_CNT = c_PAYMENT_CNT;
    }

    public Integer getC_DELIVERY_CNT() {
        return C_DELIVERY_CNT;
    }

    public void setC_DELIVERY_CNT(Integer c_DELIVERY_CNT) {
        C_DELIVERY_CNT = c_DELIVERY_CNT;
    }

    public String getC_DATA() {
        return C_DATA;
    }

    public void setC_DATA(String c_DATA) {
        C_DATA = c_DATA;
    }
}
