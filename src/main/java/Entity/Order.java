package Entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
class OrderId implements Serializable
{
    Integer O_W_ID;
    Integer O_D_ID;
    Integer O_ID;
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
}
