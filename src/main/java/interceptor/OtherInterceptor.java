package interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ssm_test.pojo.Category;
import ssm_test.pojo.OrderItem;
import ssm_test.pojo.User;
import ssm_test.service.CategoryService;
import ssm_test.service.OrderItemService;

/**
 * 过滤器主要为：搜索和页面跳转栏
 * 主要是为将需要重复写的代码抽出来
 * @author disentice
 *
 */
public class OtherInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	CategoryService categoryService;
	@Autowired
	OrderItemService orderItemService;

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 如果返回true
	 * 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;

	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作 可在modelAndView中加入数据，比如当前时间
	 */

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//获取分类集合信息，用于放在搜索栏下面 
		List<Category> cs = categoryService.list();
		request.getSession().setAttribute("cs", cs);

		//获取当前contextPath:ssm_test,用与左上角图片，跳转到首页
		HttpSession session = request.getSession();
		String contextPath = session.getServletContext().getContextPath();
		request.getSession().setAttribute("contextPath", contextPath);

		//购物车产品一共数量
		User user = (User) session.getAttribute("user");
		int cartTotalItemNumber = 0;//初始化为0
		if (null != user) {
			List<OrderItem> ois = orderItemService.listByUser(user.getId());
			for (OrderItem oi : ois) {
				cartTotalItemNumber += oi.getNumber();//遍历+1
			}

		}
		request.getSession().setAttribute("cartTotalItemNumber", cartTotalItemNumber);

	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	        System.out.println("afterCompletion(), 在访问视图之后被调用");
	}
}
