package ssm_test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ssm_test.pojo.User;
import ssm_test.service.UserService;
import ssm_test.util.Page;
/**
 * 用户控制类
 * @author disentice
 *
 */
@Controller
@RequestMapping("")
public class UserController {
	@Autowired
	UserService userService;

	//分页查询用户
	@RequestMapping("admin_user_list")
	public String list(Model model, Page page) {
		PageHelper.offsetPage(page.getStart(), page.getCount());

		List<User> us = userService.list();

		int total = (int) new PageInfo<>(us).getTotal();
		page.setTotal(total);

		model.addAttribute("us", us);
		model.addAttribute("page", page);

		return "admin/listUser";
	}
}
