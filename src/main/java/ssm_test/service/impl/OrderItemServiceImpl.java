package ssm_test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssm_test.mapper.OrderItemMapper;
import ssm_test.pojo.Order;
import ssm_test.pojo.OrderItem;
import ssm_test.pojo.OrderItemExample;
import ssm_test.pojo.Product;
import ssm_test.service.OrderItemService;
import ssm_test.service.ProductService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	OrderItemMapper orderItemMapper;
	@Autowired
	ProductService productService;

	// 增加订单项
	@Override
	public void add(OrderItem c) {
		orderItemMapper.insert(c);
	}

	// 删除订单项
	@Override
	public void delete(int id) {
		orderItemMapper.deleteByPrimaryKey(id);
	}

	// 修改订单项
	@Override
	public void update(OrderItem c) {
		orderItemMapper.updateByPrimaryKeySelective(c);
	}

	// 根据id查询
	@Override
	public OrderItem get(int id) {
		OrderItem result = orderItemMapper.selectByPrimaryKey(id);
		setProduct(result);
		return result;
	}

	// id降序查询订单项；返回集合
	public List<OrderItem> list() {
		OrderItemExample example = new OrderItemExample();
		example.setOrderByClause("id desc");
		return orderItemMapper.selectByExample(example);

	}

	// 遍历订单
	@Override
	public void fill(List<Order> os) {
		for (Order o : os) {
			fill(o);
		}
	}

	// 订单与订单项是一对多，需要自己写
	public void fill(Order o) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andOidEqualTo(o.getId());// 根据订单id查询订单项集合
		example.setOrderByClause("id desc");
		List<OrderItem> ois = orderItemMapper.selectByExample(example);
		setProduct(ois);// 订单项中产品属性赋值

		float total = 0;
		int totalNumber = 0;
		for (OrderItem oi : ois) {// 遍历订单项
			total += oi.getNumber() * oi.getProduct().getPromotePrice();// 计算金额
			totalNumber += oi.getNumber();// 累加
		}
		o.setTotal(total);
		o.setTotalNumber(totalNumber);
		o.setOrderItems(ois);

	}

	// 遍历订单项
	public void setProduct(List<OrderItem> ois) {
		for (OrderItem oi : ois) {
			setProduct(oi);
		}
	}

	// 根据产品id，向订单项中产品赋值
	private void setProduct(OrderItem oi) {
		Product p = productService.get(oi.getPid());
		oi.setProduct(p);
	}

	// 获取销量条数
	@Override
	public int getSaleCount(int pid) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andPidEqualTo(pid);
		List<OrderItem> ois = orderItemMapper.selectByExample(example);
		int result = 0;
		for (OrderItem oi : ois) {// 根据pid进行分类别查询，遍历list集合，累加
			result += oi.getNumber();
		}
		return result;
	}

	//查询订单项
	@Override
	public List<OrderItem> listByUser(int uid) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andUidEqualTo(uid).andOidIsNull();//根据userid查询，可为空；
		List<OrderItem> result = orderItemMapper.selectByExample(example);
		setProduct(result);
		return result;
	};

}
