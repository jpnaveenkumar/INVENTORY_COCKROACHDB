package Entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DistrictId implements Serializable {
    Integer D_ID;
    Integer D_W_ID;

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
