package Entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
class DistrictId implements Serializable
{
    Integer D_ID;
    Integer D_W_ID;
}

@Entity
@Table(name = "district")
public class District {
    @EmbeddedId
    DistrictId districtId;
    String D_NAME;
    String D_STREET_1;
    String D_STREET_2;
    String D_CITY;
    String D_STATE;
    String D_ZIP;
    Double D_TAX;
    Double D_YTD;
    Integer D_NEXT_O_ID;
}
