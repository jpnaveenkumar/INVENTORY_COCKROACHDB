cockroach sql --user=root --host=localhost --port=26257  --insecure < helper.sql

cockroach sql --insecure --host=localhost:26257

cockroach start-single-node --insecure

explain select d1.o_id, d1.o_entry_d, d1.o_carrier_id, ol_number, ol_i_id, ol_supply_w_id, ol_quantity, ol_amount, ol_delivery_d from orderline ol inner join (select o_id, o_entry_d, o_carrier_id from orders o inner join (select max(o_entry_d) as last_order_d from orders where o_w_id = 1 and o_d_id = 1 and o_c_id = 1)  as d on o.o_entry_d = d.last_order_d and o_w_id = 1 and o_d_id = 1 and o_c_id = 1 ) d1 on ol.ol_o_id = d1.o_id and ol.ol_w_id = 1 and ol.ol_d_id = 1;

explain select s_i_id from stock s inner join (select ol_i_id from orderline where ol_w_id = 1 and ol_d_id = 1 and ol_o_id >= 2900 and ol_o_id < 3001) as d on s.s_i_id = d.ol_i_id and s.s_w_id = 1 where s.s_quantity < 2000;