package ssm_test.service;

import java.util.List;

import ssm_test.pojo.Category;
import ssm_test.util.Page;
/**
 * 服务层分类接口
 * @author disentice
 *
 */
public interface CategoryService {

	//替换total、list方法
	List<Category> list();
/*	//获取总数
	int total();
	//分页查询
    List<Category> list(Page page);*/
    //增加
    void add(Category category);
    //删除
    void delete(int id);
    //查询
    Category get(int id);
    //更新
    void update(Category category);
}
