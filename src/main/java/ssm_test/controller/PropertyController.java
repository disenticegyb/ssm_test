package ssm_test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ssm_test.pojo.Category;
import ssm_test.pojo.Property;
import ssm_test.service.CategoryService;
import ssm_test.service.PropertyService;
import ssm_test.util.Page;
/**
 * property控制类
 * @author disentice
 *
 */
@Controller
@RequestMapping("")
public class PropertyController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	PropertyService propertyService;

	//增加属性名
	@RequestMapping("admin_property_add")
	public String add(Model model, Property p) {
		propertyService.add(p);
		return "redirect:admin_property_list?cid=" + p.getCid();//重定向，附上参数cid；
	}

	//删除
	@RequestMapping("admin_property_delete")
	public String delete(int id) {
		Property p = propertyService.get(id);
		propertyService.delete(id);
		return "redirect:admin_property_list?cid=" + p.getCid();
	}

	//编辑属性
	@RequestMapping("admin_property_edit")
	public String edit(Model model, int id) {
		Property p = propertyService.get(id);//获取property，通过id；
		Category c = categoryService.get(p.getCid());//获取Category，通过cid
		p.setCategory(c);//赋值
		model.addAttribute("p", p);
		return "admin/editProperty";
	}

	//更新修改
	@RequestMapping("admin_property_update")
	public String update(Property p) {
		propertyService.update(p);//更新Property
		return "redirect:admin_property_list?cid=" + p.getCid();
	}

	//分页查询
	@RequestMapping("admin_property_list")
	public String list(int cid, Model model, Page page) {
		Category c = categoryService.get(cid);//获取属性的分类id

		PageHelper.offsetPage(page.getStart(), page.getCount());//分页查询
		List<Property> ps = propertyService.list(cid);

		int total = (int) new PageInfo<>(ps).getTotal();//获取属性总数，通过PageHelper；
		page.setTotal(total);//赋值
		page.setParam("&cid=" + c.getId());//设置Parm值，通过字符串拼接

		model.addAttribute("ps", ps);
		model.addAttribute("c", c);
		model.addAttribute("page", page);//将数据放置在ps、c、page上；

		return "admin/listProperty";
	}
}
