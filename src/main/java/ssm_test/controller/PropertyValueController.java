package ssm_test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ssm_test.pojo.Product;
import ssm_test.pojo.PropertyValue;
import ssm_test.service.ProductService;
import ssm_test.service.PropertyValueService;

/**
 * + 属性值控制类
 * 
 * @author disentice
 *
 */
@Controller
@RequestMapping("")
public class PropertyValueController {
	@Autowired
	PropertyValueService propertyValueService;
	@Autowired
	ProductService productService;

	// 编辑
	@RequestMapping("admin_propertyValue_edit")
	public String edit(Model model, int pid) {
		Product p = productService.get(pid);// 获取Product对象
		propertyValueService.init(p);// 初始化属性值，第一次访问时候属性值不存在；
		List<PropertyValue> pvs = propertyValueService.list(p.getId());//获取属性值

		model.addAttribute("p", p);
		model.addAttribute("pvs", pvs);
		return "admin/editPropertyValue";
	}

	//使用ajax异步调用：
	@RequestMapping("admin_propertyValue_update")
	@ResponseBody
	public String update(PropertyValue pv) {
		propertyValueService.update(pv);//更新数据库；
		return "success";
	}
}
