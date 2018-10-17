package ssm_test.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ssm_test.pojo.Category;
import ssm_test.service.CategoryService;
import ssm_test.util.ImageUtil;
import ssm_test.util.Page;
import ssm_test.util.UploadedImageFile;
/**
 * 控制层，负责对请求的控制
 * @author disentice
 *
 */
@Controller//MVC标记控制器对象
@RequestMapping("")//跳转不加额外条件
public class CategoryController {

	//类型注入
	@Autowired
    CategoryService categoryService;
	
	//使用PageHelper实现分页
	@RequestMapping("admin_category_list")
	public String list(Model model,Page page){
	    PageHelper.offsetPage(page.getStart(),page.getCount());//给查询范围
	    List<Category> cs= categoryService.list();//查询
	    int total = (int) new PageInfo<>(cs).getTotal();//查询总数
	    page.setTotal(total);//设置page属性
	    model.addAttribute("cs", cs);//数据赋值给cs
	    model.addAttribute("page", page);//数据赋值给page
	    return "admin/listCategory";
	}
  
/*	//设置分页 查询跳转
    @RequestMapping("admin_category_list")
    public String list(Model model,Page page){
        List<Category> cs= categoryService.list(page);//返回查询结果
        int total = categoryService.total();//获取总页数
        page.setTotal(total);//设置page属性
        model.addAttribute("cs", cs);//存储数据传递
        model.addAttribute("page", page);
        return "admin/listCategory";//跳转；
    }*/
    
    //增加方法，获取分类名称、session、工具类
    @RequestMapping("admin_category_add")
    public String add(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        System.out.println(c.getId());
        categoryService.add(c);
        System.out.println(c.getId());
        //获取文件
        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        //根据分类id创建名字；
        File file = new File(imageFolder,c.getId()+".jpg");
        if(!file.getParentFile().exists())//判断，并获取文件
            file.getParentFile().mkdirs();
        System.out.println(uploadedImageFile);
        System.out.println(uploadedImageFile.getImage());
        System.out.println(file);
        uploadedImageFile.getImage().transferTo(file);//图片写入磁盘
        BufferedImage img = ImageUtil.change2jpg(file);//to jpg；
        ImageIO.write(img, "jpg", file);//压缩写入
 
        return "redirect:/admin_category_list";
    }
    
    //删除
    @RequestMapping("admin_category_delete")
    public String delete(int id,HttpSession session) throws IOException {
        categoryService.delete(id);//删除数据
        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();//删除图片
 
        return "redirect:/admin_category_list";
    }
    
    //编辑后跳转
    @RequestMapping("admin_category_edit")
    public String edit(int id,Model model) throws IOException {
        Category c= categoryService.get(id);//获取category
        model.addAttribute("c", c);//对象赋值给c；
        return "admin/editCategory";//跳转；
    }
    
    //更新后跳转
    @RequestMapping("admin_category_update")
    public String update(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        categoryService.update(c);//更新数据
        MultipartFile image = uploadedImageFile.getImage();//获取图片
        if(null!=image &&!image.isEmpty()){//判断是否为空
            File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
            File file = new File(imageFolder,c.getId()+".jpg");//以id命名
            image.transferTo(file);//图片写入磁盘
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);//压缩写入
        }
        return "redirect:/admin_category_list";
    }
}
