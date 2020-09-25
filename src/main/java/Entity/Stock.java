package Entity;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
class StockId implements Serializable
{
    Integer S_W_ID;
    Integer S_I_ID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockId stockId = (StockId) o;
        return S_W_ID.equals(stockId.S_W_ID) &&
                S_I_ID.equals(stockId.S_I_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(S_W_ID, S_I_ID);
    }
}

@Entity
@Table(name = "stock")
public class Stock {

    @EmbeddedId
    StockId stockId;
    Double S_QUANTITY;
    Double S_YTD;
    Integer S_ORDER_CNT;
    Integer S_REMOTE_CNT;
    String S_DIST_01;
    String S_DIST_02 ;
    String S_DIST_03;
    String S_DIST_04;
    String S_DIST_05;
    String S_DIST_06;
    String S_DIST_07;
    String S_DIST_08;
    String S_DIST_09;
    String S_DIST_10;
    String S_DATA;
}
