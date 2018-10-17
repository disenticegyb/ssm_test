package ssm_test.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.github.pagehelper.PageHelper;

import comparator.ProductAllComparator;
import comparator.ProductDateComparator;
import comparator.ProductPriceComparator;
import comparator.ProductReviewComparator;
import comparator.ProductSaleCountComparator;
import ssm_test.pojo.Category;
import ssm_test.pojo.Order;
import ssm_test.pojo.OrderItem;
import ssm_test.pojo.Product;
import ssm_test.pojo.ProductImage;
import ssm_test.pojo.PropertyValue;
import ssm_test.pojo.Review;
import ssm_test.pojo.User;
import ssm_test.service.CategoryService;
import ssm_test.service.OrderItemService;
import ssm_test.service.OrderService;
import ssm_test.service.ProductImageService;
import ssm_test.service.ProductService;
import ssm_test.service.PropertyValueService;
import ssm_test.service.ReviewService;
import ssm_test.service.UserService;

/**
 * 前台请求控制
 * 
 * @author disentice
 *
 */
@Controller
@RequestMapping("")
public class ForeController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	@Autowired
	UserService userService;
	@Autowired
	ProductImageService productImageService;
	@Autowired
	PropertyValueService propertyValueService;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	ReviewService reviewService;

	// 页面显示产品分类数据
	@RequestMapping("forehome")
	public String home(Model model) {
		List<Category> cs = categoryService.list();
		productService.fill(cs);
		productService.fillByRow(cs);
		model.addAttribute("cs", cs);
		return "fore/home";
	}

	// 注册判断
	@RequestMapping("foreregister")
	public String register(Model model, User user) {
		String name = user.getName();
		name = HtmlUtils.htmlEscape(name);
		user.setName(name);
		boolean exist = userService.isExist(name);// 获取并判断是否存在此name

		if (exist) {
			String m = "用户名已经被使用,不能使用";
			model.addAttribute("msg", m);
			model.addAttribute("user", null);
			return "fore/register";
		}
		userService.add(user);

		return "redirect:registerSuccessPage";
	}

	// 登陆判断
	@RequestMapping("forelogin")
	public String login(@RequestParam("name") String name, @RequestParam("password") String password, Model model,
			HttpSession session) {
		name = HtmlUtils.htmlEscape(name);// 对那么进行转意，避免恶意注册
		User user = userService.get(name, password);// 通过账号密码获取对象

		if (null == user) {// 判断
			model.addAttribute("msg", "账号密码错误");
			return "fore/login";
		}
		session.setAttribute("user", user);
		return "redirect:forehome";
	}

	// 退出
	@RequestMapping("forelogout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");// 从session中移除对象
		return "redirect:forehome";
	}

	// 产品显示
	@RequestMapping("foreproduct")
	public String product(int pid, Model model) {
		Product p = productService.get(pid);

		List<ProductImage> productSingleImages = productImageService.list(p.getId(), ProductImageService.type_single);
		List<ProductImage> productDetailImages = productImageService.list(p.getId(), ProductImageService.type_detail);
		// 获取单个和详情图片
		p.setProductSingleImages(productSingleImages);
		p.setProductDetailImages(productDetailImages);
		// 获取属性值和评价
		List<PropertyValue> pvs = propertyValueService.list(p.getId());
		List<Review> reviews = reviewService.list(p.getId());
		// 设置评价数量和销量
		productService.setSaleAndReviewNumber(p);
		model.addAttribute("reviews", reviews);
		model.addAttribute("p", p);
		model.addAttribute("pvs", pvs);
		return "fore/product";
	}

	// 登陆判断
	@RequestMapping("forecheckLogin")
	@ResponseBody // 异步获取数据
	public String checkLogin(HttpSession session) {
		User user = (User) session.getAttribute("user");// 获取session对象中的user
		if (null != user)
			return "success";
		return "fail";
	}

	// 登陆（动态获取user对象并登陆）
	@RequestMapping("foreloginAjax")
	@ResponseBody
	public String loginAjax(@RequestParam("name") String name, @RequestParam("password") String password,
			HttpSession session) {
		name = HtmlUtils.htmlEscape(name);
		User user = userService.get(name, password);

		if (null == user) {
			return "fail";
		}
		session.setAttribute("user", user);
		return "success";
	}

	// 页面显示排序
	@RequestMapping("forecategory")
	public String category(int cid, String sort, Model model) {
		Category c = categoryService.get(cid);
		productService.fill(c);
		productService.setSaleAndReviewNumber(c.getProducts());

		if (null != sort) {
			switch (sort) {
			case "review":
				Collections.sort(c.getProducts(), new ProductReviewComparator());
				break;
			case "date":
				Collections.sort(c.getProducts(), new ProductDateComparator());
				break;

			case "saleCount":
				Collections.sort(c.getProducts(), new ProductSaleCountComparator());
				break;

			case "price":
				Collections.sort(c.getProducts(), new ProductPriceComparator());
				break;

			case "all":
				Collections.sort(c.getProducts(), new ProductAllComparator());
				break;
			}
		}

		model.addAttribute("c", c);
		return "fore/category";
	}

	// 搜索结果
	@RequestMapping("foresearch")
	public String search(String keyword, Model model) {

		PageHelper.offsetPage(0, 20);// 20条数据
		List<Product> ps = productService.search(keyword);// 调用search进行模糊查询；
		productService.setSaleAndReviewNumber(ps);
		model.addAttribute("ps", ps);
		return "fore/searchResult";
	}

	//购买一个跳转
	@RequestMapping("forebuyone")
	public String buyone(int pid, int num, HttpSession session) {
		Product p = productService.get(pid);//获取产品
		int oiid = 0;//用于传递订单项id；

		User user = (User) session.getAttribute("user");//获取用户
		boolean found = false;
		List<OrderItem> ois = orderItemService.listByUser(user.getId());//通过用户差订单项
		for (OrderItem oi : ois) {
			if (oi.getProduct().getId().intValue() == p.getId().intValue()) {//判断订单项中是否有该产品
				oi.setNumber(oi.getNumber() + num);//数量+1
				orderItemService.update(oi);
				found = true;
				oiid = oi.getId();
				break;
			}
		}

		if (!found) {//没有则设置订单项
			OrderItem oi = new OrderItem();
			oi.setUid(user.getId());
			oi.setNumber(num);
			oi.setPid(pid);
			orderItemService.add(oi);
			oiid = oi.getId();
		}
		return "redirect:forebuy?oiid=" + oiid;
	}
	
	//结算
	@RequestMapping("forebuy")
    public String buy( Model model,String[] oiid,HttpSession session){
        List<OrderItem> ois = new ArrayList<>();//创建订单项容器
        float total = 0;//价格
 
        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem oi= orderItemService.get(id);
            total +=oi.getProduct().getPromotePrice()*oi.getNumber();//产品促销价*数量
            ois.add(oi);//订单项集合加入产品订单项
        }
 
        session.setAttribute("ois", ois);
        model.addAttribute("total", total);
        return "fore/buy";
    }
	
	//加入购物车
	@RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num, Model model,HttpSession session) {
        Product p = productService.get(pid);//获取产品
        User user =(User)  session.getAttribute("user");//获取对象
        boolean found = false;
 
        List<OrderItem> ois = orderItemService.listByUser(user.getId());//通过对象查找订单项
        for (OrderItem oi : ois) {//遍历订单
            if(oi.getProduct().getId().intValue()==p.getId().intValue()){//判断产品id是否相等
                oi.setNumber(oi.getNumber()+num);//+1
                orderItemService.update(oi);//更新
                found = true;
                break;
            }
        }
 
        if(!found){//不相等，建立新的订单项
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            orderItemService.add(oi);
        }
        return "success";
    }
	
	//购物车，登陆才有
	@RequestMapping("forecart")
    public String cart( Model model,HttpSession session) {
        User user =(User)  session.getAttribute("user");//获取user
        List<OrderItem> ois = orderItemService.listByUser(user.getId());//获取所有订单项
        model.addAttribute("ois", ois);
        return "fore/cart";
    }
	
	//订单项数量控制——ajax实现
	@RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem( Model model,HttpSession session, int pid, int number) {
        User user =(User)  session.getAttribute("user");//获取user
        if(null==user)
            return "fail";
 
        List<OrderItem> ois = orderItemService.listByUser(user.getId());//遍历user中的订单项
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId().intValue()==pid){//判断产品id
                oi.setNumber(number);//赋值
                orderItemService.update(oi);//更新
                break;
            }
 
        }
        return "success";
    }
	
	//删除订单项
	@RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem( Model model,HttpSession session,int oiid){
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return "fail";
        orderItemService.delete(oiid);
        return "success";
    }
	
	//创建订单
	@RequestMapping("forecreateOrder")
	public String createOrder( Model model,Order order,HttpSession session){
	    User user =(User)  session.getAttribute("user");
	    //格式化时间+1000以内随机数，订单编号
	    String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
	    order.setOrderCode(orderCode);//地址
	    order.setCreateDate(new Date());//日期
	    order.setUid(user.getId());//用户id
	    order.setStatus(OrderService.waitPay);//待支付状态
	    List<OrderItem> ois= (List<OrderItem>)  session.getAttribute("ois");//获取订单项集合
	 
	    float total =orderService.add(order,ois);//调用更新表格的方法，并计算金额
	    return "redirect:forealipay?oid="+order.getId() +"&total="+total;
	}
	
	//支付
	@RequestMapping("forepayed")
	public String payed(int oid, float total, Model model) {
	    Order order = orderService.get(oid);
	    order.setStatus(OrderService.waitDelivery);//代发货订单
	    order.setPayDate(new Date());
	    orderService.update(order);
	    model.addAttribute("o", order);
	    return "fore/payed";
	}
	
	//我的订单页面
	@RequestMapping("forebought")
    public String bought( Model model,HttpSession session) {
        User user =(User)  session.getAttribute("user");
        //获取不是已删除的订单
        List<Order> os= orderService.list(user.getId(),OrderService.delete);
 
        //遍历订单项，并填充
        orderItemService.fill(os);
 
        model.addAttribute("os", os);
 
        return "fore/bought";
    }
	
	//确认收货
	@RequestMapping("foreconfirmPay")
	public String confirmPay( Model model,int oid) {
	    Order o = orderService.get(oid);//获取订单对象
	    orderItemService.fill(o);//遍历并填充
	    model.addAttribute("o", o);
	    return "fore/confirmPay";
	}
	
	//确认收货成功
	@RequestMapping("foreorderConfirmed")
	public String orderConfirmed( Model model,int oid) {
	    Order o = orderService.get(oid);
	    o.setStatus(OrderService.waitReview);//设置订单为待评价
	    o.setConfirmDate(new Date());//更新日期
	    orderService.update(o);
	    return "fore/orderConfirmed";
	}
	
	//删除订单
	@RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder( Model model,int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return "success";
    }
	
	//评价产品页面显示
	@RequestMapping("forereview")
	public String review( Model model,int oid) {
	    Order o = orderService.get(oid);//获取订单对象
	    orderItemService.fill(o);//填充订单
	    Product p = o.getOrderItems().get(0).getProduct();//获取第一个产品，图片显示
	    List<Review> reviews = reviewService.list(p.getId());//获取产品评价
	    productService.setSaleAndReviewNumber(p);//设置销量和评论数量
	    model.addAttribute("p", p);
	    model.addAttribute("o", o);
	    model.addAttribute("reviews", reviews);
	    return "fore/review";
	}
	
	//提交评价
	@RequestMapping("foredoreview")
	public String doreview( Model model,HttpSession session,@RequestParam("oid") int oid,@RequestParam("pid") int pid,String content) {
	    Order o = orderService.get(oid);
	    o.setStatus(OrderService.finish);//设置状态，已完成评价
	    orderService.update(o);//更新数据库
	 
	    Product p = productService.get(pid);//获取产品对象
	    content = HtmlUtils.htmlEscape(content);//获取产品评价信息
	 
	    User user =(User)  session.getAttribute("user");
	    Review review = new Review();//新建评价类
	    review.setContent(content);
	    review.setPid(pid);
	    review.setCreateDate(new Date());
	    review.setUid(user.getId());
	    reviewService.add(review);
	 
	    return "redirect:forereview?oid="+oid+"&showonly=true";
	}
}
