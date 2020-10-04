package Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RelatedCustomerTransaction {
    Integer warehouseId;
    Integer customerId;
    Integer districtId;
    Framework framework;
    Session session;

    public RelatedCustomerTransaction(Integer warehouseId, Integer customerId, Integer districtId)
    {
        this.warehouseId = warehouseId;
        this.customerId = customerId;
        this.districtId = districtId;
        this.framework = Framework.getInstance();
        this.session = this.framework.getSession();
    }

    void getRelatedCustomers()
    {
        String sql = "select c_id from item_by_customer where i_id in (select i_id from item_by_customer " +
                "where w_id= :w_id and d_id= :d_id and c_id= :c_id) and w_id!= :w_id group by c_id having count(i_id) >= 2";
        Query query = this.session.createNativeQuery(sql);
        query.setParameter("w_id", this.warehouseId);
        query.setParameter("d_id", this.districtId);
        query.setParameter("c_id", this.customerId);
        List<Integer> customerIds = query.getResultList();
        System.out.println(customerIds);
    }

    public void findRelatedCustomers()
    {
        Transaction transaction = this.framework.startTransaction();
        getRelatedCustomers();
        this.framework.commitTransaction(transaction);
    }
}
