package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
class OrderLineId implements Serializable
{
    Integer OL_W_ID;
    Integer OL_D_ID;
    Integer OL_O_ID;
    Integer OL_NUMBER;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineId that = (OrderLineId) o;
        return OL_W_ID.equals(that.OL_W_ID) &&
                OL_D_ID.equals(that.OL_D_ID) &&
                OL_O_ID.equals(that.OL_O_ID) &&
                OL_NUMBER.equals(that.OL_NUMBER);
    }

    @Override
    public int hashCode() {
        return Objects.hash(OL_W_ID, OL_D_ID, OL_O_ID, OL_NUMBER);
    }
}

@Entity
@Table(name = "orderline")
public class OrderLine {
    @EmbeddedId
    OrderLineId orderLineId;
    Integer OL_I_ID;
    String OL_DELIVERY_D;
    Double OL_AMOUNT;
    Integer OL_SUPPLY_W_ID;
    Double OL_QUANTITY;
    String OL_DIST_INFO;
}
