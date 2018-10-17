package ssm_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务端跳转控制
 * 
 * @author disentice
 *
 */

@Controller
@RequestMapping("")
public class PageController {
	//注册
	@RequestMapping("registerPage")
	public String registerPage() {
		return "fore/register";
	}

	//注册成功
	@RequestMapping("registerSuccessPage")
	public String registerSuccessPage() {
		return "fore/registerSuccess";
	}

	//登陆
	@RequestMapping("loginPage")
	public String loginPage() {
		return "fore/login";
	}

	//支付
	@RequestMapping("forealipay")
	public String alipay() {
		return "fore/alipay";
	}
}
