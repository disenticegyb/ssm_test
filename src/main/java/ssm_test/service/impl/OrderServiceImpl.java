package ssm_test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ssm_test.mapper.OrderMapper;
import ssm_test.pojo.Order;
import ssm_test.pojo.OrderExample;
import ssm_test.pojo.OrderItem;
import ssm_test.pojo.User;
import ssm_test.service.OrderItemService;
import ssm_test.service.OrderService;
import ssm_test.service.UserService;
/**
 * 订单实现类
 * @author disentice
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	UserService userService;
	
	@Autowired
	OrderItemService orderItemService;

	@Override
	public void add(Order c) {
		orderMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		orderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Order c) {
		orderMapper.updateByPrimaryKeySelective(c);
	}

	@Override
	public Order get(int id) {
		return orderMapper.selectByPrimaryKey(id);
	}

	public List<Order> list() {
		OrderExample example = new OrderExample();
		example.setOrderByClause("id desc");
		List<Order> result = orderMapper.selectByExample(example);
		setUser(result);
		return result;
	}

	public void setUser(List<Order> os) {
		for (Order o : os)
			setUser(o);
	}

	public void setUser(Order o) {
		int uid = o.getUid();
		User u = userService.get(uid);
		o.setUser(u);
	}

	//配置事务管理
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
	public float add(Order c, List<OrderItem> ois) {
		float total = 0;//返回订单价格
        add(c);//增加订单
 
        if(false)//查看事务是否配置成功，设为true；
            throw new RuntimeException();
 
        for (OrderItem oi: ois) {//遍历订单项
            oi.setOid(c.getId());//设置id，根据订单id
            orderItemService.update(oi);//更新
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();//计算累加金额
        }
        return total;
	}

	//user和订单状态查询，传参数为非此状态
	@Override
	public List list(int uid, String excludedStatus) {
		OrderExample example =new OrderExample();
        example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        example.setOrderByClause("id desc");
        return orderMapper.selectByExample(example);
	}
}
