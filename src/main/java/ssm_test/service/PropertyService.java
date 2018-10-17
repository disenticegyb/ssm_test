package ssm_test.service;

import java.util.List;

import ssm_test.pojo.Property;
/**
 * 属性类服务接口
 * @author disentice
 *
 */
public interface PropertyService {

	//增加
	void add(Property c);

	//删除，根据id
	void delete(int id);

	//更新
	void update(Property c);

	//编辑
	Property get(int id);

	//查询分类下的属性，根据id
	List list(int cid);
}
