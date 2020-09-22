package Entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
class OrderLineId implements Serializable
{
    Integer OL_W_ID;
    Integer OL_D_ID;
    Integer OL_O_ID;
    Integer OL_NUMBER;
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
