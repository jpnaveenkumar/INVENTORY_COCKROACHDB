package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Embeddable
class CustomerId implements Serializable
{
    Integer C_ID;
    Integer C_W_ID;
    Integer C_D_ID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return C_ID.equals(that.C_ID) &&
                C_W_ID.equals(that.C_W_ID) &&
                C_D_ID.equals(that.C_D_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_ID, C_W_ID, C_D_ID);
    }


}

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
}
