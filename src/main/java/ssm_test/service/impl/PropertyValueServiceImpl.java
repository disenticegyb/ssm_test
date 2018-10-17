package ssm_test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssm_test.mapper.PropertyValueMapper;
import ssm_test.pojo.Product;
import ssm_test.pojo.Property;
import ssm_test.pojo.PropertyValue;
import ssm_test.pojo.PropertyValueExample;
import ssm_test.service.PropertyService;
import ssm_test.service.PropertyValueService;

/**
 * 属性值实现类
 * 
 * @author disentice
 *
 */
@Service
public class PropertyValueServiceImpl implements PropertyValueService {

	@Autowired
	PropertyValueMapper propertyValueMapper;

	@Autowired
	PropertyService propertyService;

	//初始化
	@Override
	public void init(Product p) {
		List<Property> pts = propertyService.list(p.getCid());//获取Property集合

		for (Property pt : pts) {//遍历
			PropertyValue pv = get(pt.getId(), p.getId());//获取对象
			if (null == pv) {//判断是否存在
				pv = new PropertyValue();//不存在创建
				pv.setPid(p.getId());
				pv.setPtid(pt.getId());
				propertyValueMapper.insert(pv);//插入数据
			}
		}
	}

	//更新
	@Override
	public void update(PropertyValue pv) {
		propertyValueMapper.updateByPrimaryKeySelective(pv);
	}

	//获取PropertyValue对象
	@Override
	public PropertyValue get(int ptid, int pid) {
		PropertyValueExample example = new PropertyValueExample();
		example.createCriteria().andPtidEqualTo(ptid).andPidEqualTo(pid);//根据属性ptid和产品pid查询
		List<PropertyValue> pvs = propertyValueMapper.selectByExample(example);
		if (pvs.isEmpty())
			return null;
		return pvs.get(0);
	}

	//获取产品id所有的属性值
	@Override
	public List<PropertyValue> list(int pid) {
		PropertyValueExample example = new PropertyValueExample();
		example.createCriteria().andPidEqualTo(pid);
		List<PropertyValue> result = propertyValueMapper.selectByExample(example);
		for (PropertyValue pv : result) {
			Property property = propertyService.get(pv.getPtid());
			pv.setProperty(property);
		}
		return result;
	}

}
