package Entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CustomerId implements Serializable {
    Integer C_ID;
    Integer C_W_ID;
    Integer C_D_ID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return C_ID.equals(that.C_ID) &&
                C_W_ID.equals(that.C_W_ID) &&
                C_D_ID.equals(that.C_D_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_ID, C_W_ID, C_D_ID);
    }

    public CustomerId(Integer c_ID, Integer c_D_ID, Integer c_W_ID)
    {
        this.C_ID = c_ID;
        this.C_W_ID = c_W_ID;
        this.C_D_ID = c_D_ID;
    }

    public Integer getC_ID() {
        return C_ID;
    }

    public void setC_ID(Integer c_ID) {
        C_ID = c_ID;
    }

    public Integer getC_W_ID() {
        return C_W_ID;
    }

    public void setC_W_ID(Integer c_W_ID) {
        C_W_ID = c_W_ID;
    }

    public Integer getC_D_ID() {
        return C_D_ID;
    }

    public void setC_D_ID(Integer c_D_ID) {
        C_D_ID = c_D_ID;
    }
}
