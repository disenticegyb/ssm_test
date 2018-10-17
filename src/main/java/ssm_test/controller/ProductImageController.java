package ssm_test.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ssm_test.pojo.Product;
import ssm_test.pojo.ProductImage;
import ssm_test.service.ProductImageService;
import ssm_test.service.ProductService;
import ssm_test.util.ImageUtil;
import ssm_test.util.UploadedImageFile;
/**
 * 图片管理控制类
 * @author disentice
 *
 */
@Controller
@RequestMapping("")
public class ProductImageController {
	
	@Autowired
	ProductService productService;

	@Autowired
	ProductImageService productImageService;

	//增加 pi对象接受type和pid的注入
	@RequestMapping("admin_productImage_add")
	public String add(ProductImage pi, HttpSession session, UploadedImageFile uploadedImageFile) {
		productImageService.add(pi);//调用插入图片方法
		String fileName = pi.getId() + ".jpg";//获取id，拼接得到文件名
		String imageFolder;
		String imageFolder_small = null;
		String imageFolder_middle = null;
		if (ProductImageService.type_single.equals(pi.getType())) {
			imageFolder = session.getServletContext().getRealPath("img/productSingle");//单个图片定位存储目录
			imageFolder_small = session.getServletContext().getRealPath("img/productSingle_small");//缩略图
			imageFolder_middle = session.getServletContext().getRealPath("img/productSingle_middle");//放大图
		} else {
			imageFolder = session.getServletContext().getRealPath("img/productDetail");//否则定位详情图片
		}

		File f = new File(imageFolder, fileName);//创建文件
		f.getParentFile().mkdirs();//调用父类目录实例创建文件夹
		try {
			uploadedImageFile.getImage().transferTo(f);//保存文件
			BufferedImage img = ImageUtil.change2jpg(f);//转换格式
			ImageIO.write(img, "jpg", f);//压缩传输

			if (ProductImageService.type_single.equals(pi.getType())) {
				File f_small = new File(imageFolder_small, fileName);
				File f_middle = new File(imageFolder_middle, fileName);

				ImageUtil.resizeImage(f, 56, 56, f_small);//设置小图片大小
				ImageUtil.resizeImage(f, 217, 190, f_middle);//设置大图片大小
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:admin_productImage_list?pid=" + pi.getPid();
	}

	//删除
	/*点击删除超链，进入ProductImageController的delete方法
	1. 获取id
	2. 根据id获取ProductImage 对象pi
	3. 借助productImageService，删除数据
	4. 如果是单个图片，那么删除3张正常，中等，小号图片
	5. 如果是详情图片，那么删除一张图片
	6. 客户端跳转到admin_productImage_list地址*/
	@RequestMapping("admin_productImage_delete")
	public String delete(int id, HttpSession session) {
		ProductImage pi = productImageService.get(id);//根据id获取对象

		String fileName = pi.getId() + ".jpg";//根据id，拼接出文件名
		String imageFolder;
		String imageFolder_small = null;
		String imageFolder_middle = null;

		if (ProductImageService.type_single.equals(pi.getType())) {//判断类型并根据类型在相应文件下删除
			imageFolder = session.getServletContext().getRealPath("img/productSingle");
			imageFolder_small = session.getServletContext().getRealPath("img/productSingle_small");
			imageFolder_middle = session.getServletContext().getRealPath("img/productSingle_middle");
			File imageFile = new File(imageFolder, fileName);
			File f_small = new File(imageFolder_small, fileName);
			File f_middle = new File(imageFolder_middle, fileName);
			imageFile.delete();
			f_small.delete();
			f_middle.delete();

		} else {
			imageFolder = session.getServletContext().getRealPath("img/productDetail");
			File imageFile = new File(imageFolder, fileName);
			imageFile.delete();
		}

		productImageService.delete(id);

		return "redirect:admin_productImage_list?pid=" + pi.getPid();
	}

	//查询
	@RequestMapping("admin_productImage_list")
	public String list(int pid, Model model) {
		Product p = productService.get(pid);//根据pid获取产品对象
		//获取简单图片
		List<ProductImage> pisSingle = productImageService.list(pid, ProductImageService.type_single);
		//获取多个图片
		List<ProductImage> pisDetail = productImageService.list(pid, ProductImageService.type_detail);

		model.addAttribute("p", p);
		model.addAttribute("pisSingle", pisSingle);
		model.addAttribute("pisDetail", pisDetail);

		return "admin/listProductImage";
	}
}
