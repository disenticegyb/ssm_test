package ssm_test.service;

import java.util.List;

import ssm_test.pojo.Category;
import ssm_test.pojo.Product;
/**
 * 产品业务层接口
 * @author disentice
 *
 */
public interface ProductService {
	
	//一套CRUD；
	void add(Product p);

	void delete(int id);

	void update(Product p);

	Product get(int id);

	List list(int cid);
	//修改图片
	void setFirstProductImage(Product p);
	
	void fill(List<Category> cs);
	 
    void fill(Category c);
 
    void fillByRow(List<Category> cs);
	
    void setSaleAndReviewNumber(Product p);
    
    void setSaleAndReviewNumber(List<Product> ps);
    
    List<Product> search(String keyword);

}
