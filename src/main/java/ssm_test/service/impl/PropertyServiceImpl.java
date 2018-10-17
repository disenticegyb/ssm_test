package ssm_test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssm_test.mapper.PropertyMapper;
import ssm_test.pojo.Property;
import ssm_test.pojo.PropertyExample;
import ssm_test.service.PropertyService;

/**
 *属性接口实现类
 * @author disentice
 *
 */
@Service
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	PropertyMapper propertyMapper;
	
	@Override
	public void add(Property c) {
		propertyMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		propertyMapper.deleteByPrimaryKey(id);

	}

	@Override
	public void update(Property c) {
		propertyMapper.updateByPrimaryKey(c);

	}

	@Override
	public Property get(int id) {
		return propertyMapper.selectByPrimaryKey(id);
	}

	//降序查询
	@Override
	public List list(int cid) {
		PropertyExample example =new PropertyExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        return propertyMapper.selectByExample(example);
	}

}
