package ssm_test.service;

import java.util.List;

import ssm_test.pojo.Order;
import ssm_test.pojo.OrderItem;
/**
 * 订单项接口
 * @author disentice
 *
 */
public interface OrderItemService {

	void add(OrderItem c);

	void delete(int id);

	void update(OrderItem c);

	OrderItem get(int id);

	List list();

	//遍历订单
	void fill(List<Order> os);

	//通过订单查询产品，遍历两次
	void fill(Order o);
	
	//查询销量
	int getSaleCount(int  pid);
	
	//通过用户id查询订单项
	List<OrderItem> listByUser(int uid);
}
