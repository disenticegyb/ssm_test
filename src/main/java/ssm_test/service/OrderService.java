package ssm_test.service;

import java.util.List;

import ssm_test.pojo.Order;
import ssm_test.pojo.OrderItem;
/**
 * 订单接口
 * @author disentice
 *
 */
public interface OrderService {

	//订单状态
	String waitPay = "waitPay";//待支付
    String waitDelivery = "waitDelivery";//待发货
    String waitConfirm = "waitConfirm";//待确认收获
    String waitReview = "waitReview";//待评价
    String finish = "finish";//完成
    String delete = "delete";//删除
 
    //增加方法用于修改订单项表和增加订单表，需进行事务管理
    float add(Order c,List<OrderItem> ois);
    
    //提供订单页面，订单状态，参数user和状态
    List list(int uid, String excludedStatus);
    //一套CRUD
    void add(Order c);
    
    void delete(int id);
    
    void update(Order c);
    
    Order get(int id);
    
    List list();
    
    
}
