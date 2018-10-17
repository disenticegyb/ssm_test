package ssm_test.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssm_test.mapper.ProductMapper;
import ssm_test.pojo.Category;
import ssm_test.pojo.Product;
import ssm_test.pojo.ProductExample;
import ssm_test.pojo.ProductImage;
import ssm_test.service.CategoryService;
import ssm_test.service.OrderItemService;
import ssm_test.service.ProductImageService;
import ssm_test.service.ProductService;
import ssm_test.service.ReviewService;

/**
 * product服务层实现类
 * 
 * @author disentice
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductMapper productMapper;
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductImageService productImageService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	ReviewService reviewService;

	@Override
	public void add(Product p) {
		productMapper.insert(p);
	}

	@Override
	public void delete(int id) {
		productMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Product p) {
		productMapper.updateByPrimaryKey(p);
	}

	@Override
	public Product get(int id) {
		return productMapper.selectByPrimaryKey(id);
	}

	// 遍历查询到的Product结果；调用setcategory方法
	public void setCategory(List<Product> ps) {
		for (Product p : ps)
			setCategory(p);
	}

	// 向产品category属性赋值
	public void setCategory(Product p) {
		int cid = p.getCid();
		Category c = categoryService.get(cid);
		p.setCategory(c);
	}

	// 查询product
	@Override
	public List list(int cid) {
		ProductExample example = new ProductExample();
		example.createCriteria().andCidEqualTo(cid);
		example.setOrderByClause("id desc");// 降序
		List result = productMapper.selectByExample(example);// 返回product集合
		setCategory(result);//
		return result;
	}

	// 设置图片
	@Override
	public void setFirstProductImage(Product p) {
		List<ProductImage> pis = productImageService.list(p.getId(), ProductImageService.type_single);
		if (!pis.isEmpty()) {// 查询图片后判断是否为空
			ProductImage pi = pis.get(0);// 获取第一张
			p.setFirstProductImage(pi);// 设置
		}
	}

	// 遍历产品集合
	public void setFirstProductImage(List<Product> ps) {
		for (Product p : ps) {
			setFirstProductImage(p);
		}
	}

	//分行查询产品
	@Override
	public void fillByRow(List<Category> cs) {
		int productNumberEachRow = 8;//设置显示数量
		for (Category c : cs) {//遍历
			List<Product> products = c.getProducts();//获取产品集合
			List<List<Product>> productsByRow = new ArrayList<>();//创建存放产品集合的容器
			for (int i = 0; i < products.size(); i += productNumberEachRow) {//for循环，没次＋显示个数
				int size = i + productNumberEachRow;//赋值个isize
				size = size > products.size() ? products.size() : size;//判断，如果集合够长则按8，负责按集合长度
				List<Product> productsOfEachRow = products.subList(i, size);//每行产品数据存放至集合中
				productsByRow.add(productsOfEachRow);//放到list集合
			}
			c.setProductsByRow(productsByRow);//赋值属性
		}
	}

	//设置销量和评论数量
	@Override
	public void setSaleAndReviewNumber(Product p) {
		int saleCount = orderItemService.getSaleCount(p.getId());
		p.setSaleCount(saleCount);

		int reviewCount = reviewService.getCount(p.getId());
		p.setReviewCount(reviewCount);
	}

	//遍历每一个产品
	@Override
	public void setSaleAndReviewNumber(List<Product> ps) {
		for (Product p : ps) {
			setSaleAndReviewNumber(p);
		}
	}

	//通多Categorry获取product；
	@Override
	public void fill(Category c) {
		List<Product> ps = list(c.getId());
		c.setProducts(ps);
	}

	//遍历Category集合
	@Override
	public void fill(List<Category> cs) {
		 for (Category c : cs) {
	            fill(c);
	        }
	}

	//根据关键字进行模糊查询
	@Override
	public List<Product> search(String keyword) {
		ProductExample example = new ProductExample();
        example.createCriteria().andNameLike("%" + keyword + "%");
        example.setOrderByClause("id desc");
        List result = productMapper.selectByExample(example);
        setFirstProductImage(result);
        setCategory(result);
        return result;
	}

}
