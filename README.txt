cockroach sql --user=root --host=localhost --port=26257  --insecure < helper.sql

cockroach sql --insecure --host=localhost:26257

cockroach start-single-node --insecure

explain select d1.o_id, d1.o_entry_d, d1.o_carrier_id, ol_number, ol_i_id, ol_supply_w_id, ol_quantity, ol_amount, ol_delivery_d from orderline ol inner join (select o_id, o_entry_d, o_carrier_id from orders o inner join (select max(o_entry_d) as last_order_d from orders where o_w_id = 1 and o_d_id = 1 and o_c_id = 1)  as d on o.o_entry_d = d.last_order_d and o_w_id = 1 and o_d_id = 1 and o_c_id = 1 ) d1 on ol.ol_o_id = d1.o_id and ol.ol_w_id = 1 and ol.ol_d_id = 1;

explain select s_i_id from stock s inner join (select ol_i_id from orderline where ol_w_id = 1 and ol_d_id = 1 and ol_o_id >= 2900 and ol_o_id < 3001) as d on s.s_i_id = d.ol_i_id and s.s_w_id = 1 where s.s_quantity < 11;

explain select c.c_first, c.c_middle, c.c_last, d1.entryDateTime, d1.orderId, d1.customerId, d1.maxQuantity, d1.itemIds from customer c inner join (select o.o_entry_d as entryDateTime, o.o_id as orderId, o.o_c_id as customerId, d.maxQuantity as maxQuantity, d.itemIds as itemIds from orders o inner join (select ol.ol_o_id as orderId, max(ol.ol_quantity) as maxQuantity, string_agg(CAST(ol.ol_i_id as string),',') as itemIds from orderline ol where ol.ol_d_id = 1 and ol.ol_w_id = 1 and ol.ol_o_id >= 1 and ol.ol_o_id < 3 group by ol.ol_o_id) as d on o.o_id = d.orderId and o.o_w_id = 1 and o.o_d_id = 1) d1 on c.c_id = d1.customerId and c.c_w_id = 1 and c.c_d_id = 1;

explain select ol.ol_i_id from orders o inner join orderline ol on o.o_id = ol.ol_o_id and o.o_w_id=1 and o.o_d_id=1 and o.o_c_id=1 and ol.ol_w_id=1 and ol.ol_d_id=1 inner join item i on i.i_id = ol.ol_i_id;

explain select ol.ol_o_id from orderline ol where ol.ol_i_id in (select ol.ol_i_id from orders o inner join orderline ol on o.o_id = ol.ol_o_id and o.o_w_id=1 and o.o_d_id=1 and o.o_c_id=1 and ol.ol_w_id=1 and ol.ol_d_id=1 inner join item i on i.i_id = ol.ol_i_id);

explain select c_id from customer c inner join orders o1 on c.c_id = o1.o_c_id inner join (select ol.ol_o_id as orderId from orderline ol where ol.ol_i_id in (select ol.ol_i_id  from orders o inner join orderline ol on o.o_id = ol.ol_o_id and o.o_w_id=1 and o.o_d_id=1 and o.o_c_id=1 and ol.ol_w_id=1 and ol.ol_d_id=1 inner join item i on i.i_id = ol.ol_i_id) and ol.ol_w_id != 1) as d1 on d1.orderId = o1.o_id;

explain select o.o_c_id from orders o where o.o_id in (select ol.ol_o_id as orderId from orderline ol where ol.ol_i_id in (select ol.ol_i_id  from orders o inner join orderline ol on o.o_id = ol.ol_o_id and o.o_w_id=1 and o.o_d_id=1 and o.o_c_id=1 and ol.ol_w_id=1 and ol.ol_d_id=1 inner join item i on i.i_id = ol.ol_i_id) and ol.ol_w_id != 1);

explain select c_id from item_by_customer where i_id in (select i_id from item_by_customer where w_id=1 and d_id=1 and c_id=1) and w_id!=1 group by c_id having count(i_id) > 2;
