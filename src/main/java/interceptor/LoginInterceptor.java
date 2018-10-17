package interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ssm_test.pojo.User;
import ssm_test.service.CategoryService;
import ssm_test.service.OrderItemService;

/**
 * 设置过滤器， 用于处理需要登陆的操作
 * @author disentice
 *
 */
//继承springmvc中 的自定义拦截器
public class LoginInterceptor extends HandlerInterceptorAdapter{

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

		HttpSession session = request.getSession();
		String contextPath = session.getServletContext().getContextPath();//获取上下文路径
		String[] noNeedAuthPage = new String[] { "home", "checkLogin", "register", "loginAjax", "login", "product",
				"category", "search" };//将不需要进行身份验证的功能创建一个String数组

		String uri = request.getRequestURI();//获取请求部分uri；
		System.out.println(uri);
		uri = StringUtils.remove(uri, contextPath);//去掉项目名前缀
	    System.out.println(uri);
		if (uri.startsWith("/fore")) {//判断是否为前台请求
			String method = StringUtils.substringAfterLast(uri, "/fore");//取出请求后面的字符串——home、login等
			if (!Arrays.asList(noNeedAuthPage).contains(method)) {//判断是否在自定义的数组里
				User user = (User) session.getAttribute("user");//不再获取user
				if (null == user) {
					response.sendRedirect("loginPage");//为空返回登陆页面
					return false;
				}
			}
		}

		return true;

	}
	
	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作 可在modelAndView中加入数据，比如当前时间
	 */

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
}
