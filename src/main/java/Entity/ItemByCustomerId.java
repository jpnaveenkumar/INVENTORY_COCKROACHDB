package Entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ItemByCustomerId implements Serializable {
    Integer W_ID;
    Integer I_ID;
    Integer O_ID;
    Integer D_ID;
    Integer C_ID;
}
