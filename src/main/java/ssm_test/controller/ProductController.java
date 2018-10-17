package ssm_test.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ssm_test.pojo.Category;
import ssm_test.pojo.Product;
import ssm_test.service.CategoryService;
import ssm_test.service.ProductService;
import ssm_test.util.Page;
/**
 * 产品管理控制类
 * @author disentice
 *
 */
@Controller
@RequestMapping("")
public class ProductController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;

	//增加产品
	@RequestMapping("admin_product_add")
	public String add(Model model, Product p) {
		p.setCreateDate(new Date());//给日期属性赋值
		productService.add(p);
		return "redirect:admin_product_list?cid=" + p.getCid();
	}

	//删除产品
	@RequestMapping("admin_product_delete")
	public String delete(int id) {
		Product p = productService.get(id);//获取id
		productService.delete(id);
		return "redirect:admin_product_list?cid=" + p.getCid();
	}

	//编辑产品
	@RequestMapping("admin_product_edit")
	public String edit(Model model, int id) {
		Product p = productService.get(id);
		Category c = categoryService.get(p.getCid());
		p.setCategory(c);//给分类对象初始化
		model.addAttribute("p", p);//传递至前端
		return "admin/editProduct";
	}

	//修改产品
	@RequestMapping("admin_product_update")
	public String update(Product p) {
		productService.update(p);
		return "redirect:admin_product_list?cid=" + p.getCid();
	}

	//查询
	@RequestMapping("admin_product_list")
	public String list(int cid, Model model, Page page) {
		Category c = categoryService.get(cid);

		PageHelper.offsetPage(page.getStart(), page.getCount());
		List<Product> ps = productService.list(cid);

		int total = (int) new PageInfo<>(ps).getTotal();
		page.setTotal(total);
		page.setParam("&cid=" + c.getId());

		model.addAttribute("ps", ps);
		model.addAttribute("c", c);
		model.addAttribute("page", page);

		return "admin/listProduct";
	}
}
