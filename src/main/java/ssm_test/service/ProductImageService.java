package ssm_test.service;

import java.util.List;

import ssm_test.pojo.ProductImage;

/**
 * 图片管理接口
 * 
 * @author disentice
 *
 */
public interface ProductImageService {

	// 一套CRUD，加一个图片详情和单个图片成员变量；
	String type_single = "type_single";
	String type_detail = "type_detail";

	void add(ProductImage pi);

	void delete(int id);

	void update(ProductImage pi);

	ProductImage get(int id);

	List list(int pid, String type);

}
