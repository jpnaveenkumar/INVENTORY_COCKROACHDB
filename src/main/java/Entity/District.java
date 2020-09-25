package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
class DistrictId implements Serializable
{
    Integer D_ID;
    Integer D_W_ID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistrictId that = (DistrictId) o;
        return D_ID.equals(that.D_ID) &&
                D_W_ID.equals(that.D_W_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(D_ID, D_W_ID);
    }
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
