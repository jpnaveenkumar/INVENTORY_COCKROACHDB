package Transaction;

public class NewOrderTransactionInput {
    int itemNumber;
    int supplierWarehouseNumber;
    int quantity;

    public NewOrderTransactionInput(int itemNumber, int supplierWarehouseNumber, int quantity) {
        this.itemNumber = itemNumber;
        this.supplierWarehouseNumber = supplierWarehouseNumber;
        this.quantity = quantity;
    }
}
