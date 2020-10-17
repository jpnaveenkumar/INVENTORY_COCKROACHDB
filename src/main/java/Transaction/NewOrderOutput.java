package Transaction;

import java.util.List;

public class NewOrderOutput {
    //1
    Integer W_ID;
    Integer D_ID;
    Integer C_ID;
    String C_LAST;
    String C_CREDIT;
    Double C_DISCOUNT;
    //2
    Double W_TAX;
    Double D_TAX;
    //3
    Integer O_ID;
    String O_ENTRY_D;
    //4
    Integer NUM_ITEMS;
    Double TOTAL_AMOUNT;
    //5
    List<ItemOutput> itemOutputs;
}
