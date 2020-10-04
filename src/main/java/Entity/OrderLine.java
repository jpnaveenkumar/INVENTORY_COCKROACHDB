package Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orderline")
public class OrderLine {
    @EmbeddedId
    OrderLineId orderLineId;
    Integer OL_I_ID;
    String OL_DELIVERY_D;
    Double OL_AMOUNT;
    Integer OL_SUPPLY_W_ID;
    Double OL_QUANTITY;
    String OL_DIST_INFO;

    public OrderLineId getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(OrderLineId orderLineId) {
        this.orderLineId = orderLineId;
    }

    public Integer getOL_I_ID() {
        return OL_I_ID;
    }

    public void setOL_I_ID(Integer OL_I_ID) {
        this.OL_I_ID = OL_I_ID;
    }

    public String getOL_DELIVERY_D() {
        return OL_DELIVERY_D;
    }

    public void setOL_DELIVERY_D(String OL_DELIVERY_D) {
        this.OL_DELIVERY_D = OL_DELIVERY_D;
    }

    public Double getOL_AMOUNT() {
        return OL_AMOUNT;
    }

    public void setOL_AMOUNT(Double OL_AMOUNT) {
        this.OL_AMOUNT = OL_AMOUNT;
    }

    public Integer getOL_SUPPLY_W_ID() {
        return OL_SUPPLY_W_ID;
    }

    public void setOL_SUPPLY_W_ID(Integer OL_SUPPLY_W_ID) {
        this.OL_SUPPLY_W_ID = OL_SUPPLY_W_ID;
    }

    public Double getOL_QUANTITY() {
        return OL_QUANTITY;
    }

    public void setOL_QUANTITY(Double OL_QUANTITY) {
        this.OL_QUANTITY = OL_QUANTITY;
    }

    public String getOL_DIST_INFO() {
        return OL_DIST_INFO;
    }

    public void setOL_DIST_INFO(String OL_DIST_INFO) {
        this.OL_DIST_INFO = OL_DIST_INFO;
    }
}
