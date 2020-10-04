package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;

@Entity
@Table(name = "orders")
public class Order {
    @EmbeddedId
    OrderId orderId;
    Integer O_C_ID;
    String O_CARRIER_ID;
    Double O_OL_CNT;
    Double O_ALL_LOCAL;
    String O_ENTRY_D;
    @Transient
    HashMap<Integer, Item> popularItems;
    @Transient
    Customer orderedCustomer;
    @Transient
    Double maximumQuantityOrdered;

    public HashMap<Integer, Item> getPopularItems() {
        return popularItems;
    }

    public void setPopularItems(HashMap<Integer, Item> popularItems) {
        this.popularItems = popularItems;
    }

    public Customer getOrderedCustomer() {
        return orderedCustomer;
    }

    public void setOrderedCustomer(Customer orderedCustomer) {
        this.orderedCustomer = orderedCustomer;
    }

    public Double getMaximumQuantityOrdered() {
        return maximumQuantityOrdered;
    }

    public void setMaximumQuantityOrdered(Double maximumQuantityOrdered) {
        this.maximumQuantityOrdered = maximumQuantityOrdered;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public void setOrderId(OrderId orderId) {
        this.orderId = orderId;
    }

    public Integer getO_C_ID() {
        return O_C_ID;
    }

    public void setO_C_ID(Integer o_C_ID) {
        O_C_ID = o_C_ID;
    }

    public String getO_CARRIER_ID() {
        return O_CARRIER_ID;
    }

    public void setO_CARRIER_ID(String o_CARRIER_ID) {
        O_CARRIER_ID = o_CARRIER_ID;
    }

    public Double getO_OL_CNT() {
        return O_OL_CNT;
    }

    public void setO_OL_CNT(Double o_OL_CNT) {
        O_OL_CNT = o_OL_CNT;
    }

    public Double getO_ALL_LOCAL() {
        return O_ALL_LOCAL;
    }

    public void setO_ALL_LOCAL(Double o_ALL_LOCAL) {
        O_ALL_LOCAL = o_ALL_LOCAL;
    }

    public String getO_ENTRY_D() {
        return O_ENTRY_D;
    }

    public void setO_ENTRY_D(String o_ENTRY_D) {
        O_ENTRY_D = o_ENTRY_D;
    }
}
