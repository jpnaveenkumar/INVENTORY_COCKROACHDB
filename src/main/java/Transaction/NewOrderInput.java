package Transaction;

public class NewOrderInput {
    int itemNumber;
    int supplierWarehouseNumber;
    int quantity;

    public NewOrderInput(int itemNumber, int supplierWarehouseNumber, int quantity) {
        this.itemNumber = itemNumber;
        this.supplierWarehouseNumber = supplierWarehouseNumber;
        this.quantity = quantity;
    }
}
