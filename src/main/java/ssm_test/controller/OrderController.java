package ssm_test.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ssm_test.pojo.Order;
import ssm_test.service.OrderItemService;
import ssm_test.service.OrderService;
import ssm_test.util.Page;
/**
 * 后台管理查询和发货功能
 * @author disentice
 *
 */
@Controller
@RequestMapping("")
public class OrderController {
	@Autowired
	OrderService orderService;
	@Autowired
	OrderItemService orderItemService;

	//查询
	@RequestMapping("admin_order_list")
	public String list(Model model, Page page) {
		PageHelper.offsetPage(page.getStart(), page.getCount());

		List<Order> os = orderService.list();

		int total = (int) new PageInfo<>(os).getTotal();
		page.setTotal(total);

		orderItemService.fill(os);

		model.addAttribute("os", os);
		model.addAttribute("page", page);

		return "admin/listOrder";
	}

	//发货
	@RequestMapping("admin_order_delivery")
	public String delivery(Order o) throws IOException {
		o.setDeliveryDate(new Date());//设置日期
		o.setStatus(OrderService.waitConfirm);//设置订单状态
		orderService.update(o);//更新
		return "redirect:admin_order_list";
	}
}
