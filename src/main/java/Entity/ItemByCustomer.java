package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "item_by_customer")
public class ItemByCustomer {
    @EmbeddedId
    ItemByCustomerId itemByCustomerId;
}
