package Entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DistrictId implements Serializable {
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

    public DistrictId(Integer d_ID, Integer d_W_ID){
        this.D_ID = d_ID;
        this.D_W_ID = d_W_ID;
    }

    public Integer getD_ID() {
        return D_ID;
    }

    public void setD_ID(Integer d_ID) {
        D_ID = d_ID;
    }

    public Integer getD_W_ID() {
        return D_W_ID;
    }

    public void setD_W_ID(Integer d_W_ID) {
        D_W_ID = d_W_ID;
    }
}
