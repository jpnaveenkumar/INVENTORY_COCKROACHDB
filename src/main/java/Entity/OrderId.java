package Entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderId implements Serializable {
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

    public OrderId(Integer o_W_ID, Integer o_D_ID, Integer o_ID){
        this.O_D_ID = o_D_ID;
        this.O_ID = o_ID;
        this.O_W_ID = o_W_ID;
    }

    public Integer getO_W_ID() {
        return O_W_ID;
    }

    public void setO_W_ID(Integer o_W_ID) {
        O_W_ID = o_W_ID;
    }

    public Integer getO_D_ID() {
        return O_D_ID;
    }

    public void setO_D_ID(Integer o_D_ID) {
        O_D_ID = o_D_ID;
    }

    public Integer getO_ID() {
        return O_ID;
    }

    public void setO_ID(Integer o_ID) {
        O_ID = o_ID;
    }
}
