package Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item {
    @Id
    Integer I_ID;
    String I_NAME;
    Double I_PRICE;
    Integer I_IM_ID;
    String I_DATA;
}
