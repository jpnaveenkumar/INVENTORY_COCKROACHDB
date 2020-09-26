package Entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class OrderLineId implements Serializable {
    Integer OL_W_ID;
    Integer OL_D_ID;
    Integer OL_O_ID;
    Integer OL_NUMBER;

    public OrderLineId(Integer OL_W_ID, Integer OL_D_ID, Integer OL_O_ID, Integer OL_NUMBER) {
        this.OL_W_ID = OL_W_ID;
        this.OL_D_ID = OL_D_ID;
        this.OL_O_ID = OL_O_ID;
        this.OL_NUMBER = OL_NUMBER;
    }

    public Integer getOL_W_ID() {
        return OL_W_ID;
    }

    public void setOL_W_ID(Integer OL_W_ID) {
        this.OL_W_ID = OL_W_ID;
    }

    public Integer getOL_D_ID() {
        return OL_D_ID;
    }

    public void setOL_D_ID(Integer OL_D_ID) {
        this.OL_D_ID = OL_D_ID;
    }

    public Integer getOL_O_ID() {
        return OL_O_ID;
    }

    public void setOL_O_ID(Integer OL_O_ID) {
        this.OL_O_ID = OL_O_ID;
    }

    public Integer getOL_NUMBER() {
        return OL_NUMBER;
    }

    public void setOL_NUMBER(Integer OL_NUMBER) {
        this.OL_NUMBER = OL_NUMBER;
    }
}
