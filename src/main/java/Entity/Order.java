package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
class OrderId implements Serializable
{
    Integer O_W_ID;
    Integer O_D_ID;
    Integer O_ID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderId orderId = (OrderId) o;
        return O_W_ID.equals(orderId.O_W_ID) &&
                O_D_ID.equals(orderId.O_D_ID) &&
                O_ID.equals(orderId.O_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(O_W_ID, O_D_ID, O_ID);
    }
}

@Entity
@Table(name = "orders")
public class Order {
    @EmbeddedId
    OrderId orderId;

    Integer O_C_ID;
    String O_CARRIER_ID;
    Double O_OL_CNT;
    Double O_ALL_LOCAL;
    String O_ENTRY_D;

    public void setOrderId(Integer O_W_ID, Integer O_D_ID, Integer O_ID) {
        this.orderId.O_W_ID = O_W_ID;
        this.orderId.O_D_ID = O_D_ID;
        this.orderId.O_ID = O_ID;
    }

    public Integer getO_C_ID() {
        return O_C_ID;
    }

    public void setO_C_ID(Integer o_C_ID) {
        O_C_ID = o_C_ID;
    }

    public String getO_CARRIER_ID() {
        return O_CARRIER_ID;
    }

    public void setO_CARRIER_ID(String o_CARRIER_ID) {
        O_CARRIER_ID = o_CARRIER_ID;
    }

    public Double getO_OL_CNT() {
        return O_OL_CNT;
    }

    public void setO_OL_CNT(Double o_OL_CNT) {
        O_OL_CNT = o_OL_CNT;
    }

    public Double getO_ALL_LOCAL() {
        return O_ALL_LOCAL;
    }

    public void setO_ALL_LOCAL(Double o_ALL_LOCAL) {
        O_ALL_LOCAL = o_ALL_LOCAL;
    }

    public String getO_ENTRY_D() {
        return O_ENTRY_D;
    }

    public void setO_ENTRY_D(String o_ENTRY_D) {
        O_ENTRY_D = o_ENTRY_D;
    }

}
