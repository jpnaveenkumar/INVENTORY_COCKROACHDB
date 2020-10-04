package Entity;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

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

    public StockId getStockId() {
        return stockId;
    }

    public void setStockId(StockId stockId) {
        this.stockId = stockId;
    }

    public Double getS_QUANTITY() {
        return S_QUANTITY;
    }

    public void setS_QUANTITY(Double s_QUANTITY) {
        S_QUANTITY = s_QUANTITY;
    }

    public Double getS_YTD() {
        return S_YTD;
    }

    public void setS_YTD(Double s_YTD) {
        S_YTD = s_YTD;
    }

    public Integer getS_ORDER_CNT() {
        return S_ORDER_CNT;
    }

    public void setS_ORDER_CNT(Integer s_ORDER_CNT) {
        S_ORDER_CNT = s_ORDER_CNT;
    }

    public Integer getS_REMOTE_CNT() {
        return S_REMOTE_CNT;
    }

    public void setS_REMOTE_CNT(Integer s_REMOTE_CNT) {
        S_REMOTE_CNT = s_REMOTE_CNT;
    }

    public String getS_DIST_01() {
        return S_DIST_01;
    }

    public void setS_DIST_01(String s_DIST_01) {
        S_DIST_01 = s_DIST_01;
    }

    public String getS_DIST_02() {
        return S_DIST_02;
    }

    public void setS_DIST_02(String s_DIST_02) {
        S_DIST_02 = s_DIST_02;
    }

    public String getS_DIST_03() {
        return S_DIST_03;
    }

    public void setS_DIST_03(String s_DIST_03) {
        S_DIST_03 = s_DIST_03;
    }

    public String getS_DIST_04() {
        return S_DIST_04;
    }

    public void setS_DIST_04(String s_DIST_04) {
        S_DIST_04 = s_DIST_04;
    }

    public String getS_DIST_05() {
        return S_DIST_05;
    }

    public void setS_DIST_05(String s_DIST_05) {
        S_DIST_05 = s_DIST_05;
    }

    public String getS_DIST_06() {
        return S_DIST_06;
    }

    public void setS_DIST_06(String s_DIST_06) {
        S_DIST_06 = s_DIST_06;
    }

    public String getS_DIST_07() {
        return S_DIST_07;
    }

    public void setS_DIST_07(String s_DIST_07) {
        S_DIST_07 = s_DIST_07;
    }

    public String getS_DIST_08() {
        return S_DIST_08;
    }

    public void setS_DIST_08(String s_DIST_08) {
        S_DIST_08 = s_DIST_08;
    }

    public String getS_DIST_09() {
        return S_DIST_09;
    }

    public void setS_DIST_09(String s_DIST_09) {
        S_DIST_09 = s_DIST_09;
    }

    public String getS_DIST_10() {
        return S_DIST_10;
    }

    public void setS_DIST_10(String s_DIST_10) {
        S_DIST_10 = s_DIST_10;
    }

    public String getS_DATA() {
        return S_DATA;
    }

    public void setS_DATA(String s_DATA) {
        S_DATA = s_DATA;
    }
}
