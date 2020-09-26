package Entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StockId implements Serializable {
    Integer S_W_ID;
    Integer S_I_ID;

    public StockId(Integer s_W_ID, Integer s_I_ID) {
        S_W_ID = s_W_ID;
        S_I_ID = s_I_ID;
    }

    public Integer getS_W_ID() {
        return S_W_ID;
    }

    public void setS_W_ID(Integer s_W_ID) {
        S_W_ID = s_W_ID;
    }

    public Integer getS_I_ID() {
        return S_I_ID;
    }

    public void setS_I_ID(Integer s_I_ID) {
        S_I_ID = s_I_ID;
    }
}
