package Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item {
    @Id
    Integer I_ID;
    String I_NAME;
    Double I_PRICE;
    Integer I_IM_ID;
    String I_DATA;

    public Integer getI_ID() {
        return I_ID;
    }

    public void setI_ID(Integer i_ID) {
        I_ID = i_ID;
    }

    public String getI_NAME() {
        return I_NAME;
    }

    public void setI_NAME(String i_NAME) {
        I_NAME = i_NAME;
    }

    public Double getI_PRICE() {
        return I_PRICE;
    }

    public void setI_PRICE(Double i_PRICE) {
        I_PRICE = i_PRICE;
    }

    public Integer getI_IM_ID() {
        return I_IM_ID;
    }

    public void setI_IM_ID(Integer i_IM_ID) {
        I_IM_ID = i_IM_ID;
    }

    public String getI_DATA() {
        return I_DATA;
    }

    public void setI_DATA(String i_DATA) {
        I_DATA = i_DATA;
    }
}
