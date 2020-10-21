package Transaction;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DatabaseState {

    Framework framework;
    Session session;

    public DatabaseState(){
        this.framework = Framework.getInstance(0);
        this.session = this.framework.getSession();
    }
    
    public List<String> outputDatabaseState(int i){
        List<String> output = new ArrayList<>();
        output.add(String.valueOf(i));

        Query warehouseQuery  = session.createNativeQuery("select sum(W_YTD) as w_ytd from warehouse");
        double w_ytd = (double) warehouseQuery.getSingleResult();
        output.add(String.valueOf(w_ytd));

        Query districtQuery = session.createNativeQuery("select sum(D_YTD) as d_ytd, sum(D_NEXT_O_ID) as d_next_o_id from district");
        Object[] districtResult = (Object[]) districtQuery.getSingleResult();
        double d_ytd = (double) districtResult[0];
        output.add(String.valueOf(d_ytd));
        output.add(String.valueOf(districtResult[1]));

        Query customerQuery = session.createNativeQuery("select sum(C_BALANCE) as c_balance, sum(C_YTD_PAYMENT) as c_ytd_payment, sum(C_PAYMENT_CNT) as c_payment_cnt, sum(C_DELIVERY_CNT) as c_delivery_cnt from customer");
        Object[] customerResult = (Object[]) customerQuery.getSingleResult();
        double c_balance = (double) customerResult[0];
        double c_ytd_payment = (double) customerResult[1];
        output.add(String.valueOf(c_balance));
        output.add(String.valueOf(c_ytd_payment));
        output.add(String.valueOf(customerResult[2]));
        output.add(String.valueOf(customerResult[3]));

        Query orderQuery = session.createNativeQuery("select max(O_ID) as o_id, sum(O_OL_CNT) as o_ol_cnt from orders");
        Object[] orderResult = (Object[]) orderQuery.getSingleResult();
        double o_ol_cnt = (double) orderResult[1];
        output.add(String.valueOf(orderResult[0]));
        output.add(String.valueOf(o_ol_cnt));

        Query orderlineQuery = session.createNativeQuery("select sum(OL_AMOUNT) as ol_amount, sum(OL_QUANTITY) as ol_quantity from orderline");
        Object[] orderlineResult = (Object[]) orderlineQuery.getSingleResult();
        double ol_amount = (double) orderlineResult[0];
        double ol_quantity = (double) orderlineResult[1];
        output.add(String.valueOf(ol_amount));
        output.add(String.valueOf(ol_quantity));

        Query stockQuery = session.createNativeQuery("select sum(S_QUANTITY) as s_quantity, sum(S_YTD) as s_ytd, sum(S_ORDER_CNT) as s_order_cnt, sum(S_REMOTE_CNT) as s_remote_cnt from stock");
        Object[] stockResult = (Object[]) stockQuery.getSingleResult();
        double s_quantity = (double) stockResult[0];
        double s_ytd = (double) stockResult[1];
        output.add(String.valueOf(s_quantity));
        output.add(String.valueOf(s_ytd));
        output.add(String.valueOf(stockResult[2]));
        output.add(String.valueOf(stockResult[3]));

        return output;
    }

}
