package ssm_test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssm_test.mapper.CategoryMapper;
import ssm_test.pojo.Category;
import ssm_test.pojo.CategoryExample;
import ssm_test.service.CategoryService;
import ssm_test.util.Page;

/**
 * Category服务层
 * 实现接口方法
 * 使用逆向工程之后原代码覆盖，修改调用方法即可；
 * @author disentice
 *
 */
@Service//Spring服务层注解
public class CategoryServiceImpl implements CategoryService{

	//类型注入categoryMapper外部资源；
	@Autowired
	CategoryMapper categoryMapper;

	
	@Override
	public void add(Category category) {
		 categoryMapper.insert(category);
	}

	@Override
	public void delete(int id) {
		categoryMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Category get(int id) {
		return categoryMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(Category category) {
		categoryMapper.updateByPrimaryKey(category);
	}

	//指定id倒序查询
	@Override
	public List<Category> list() {
		CategoryExample example =new CategoryExample();
        example.setOrderByClause("id desc");
        return categoryMapper.selectByExample(example);
	};

}
