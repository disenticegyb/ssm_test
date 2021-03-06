package ssm_test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssm_test.mapper.ProductImageMapper;
import ssm_test.pojo.ProductImage;
import ssm_test.pojo.ProductImageExample;
import ssm_test.service.ProductImageService;

/**
 * 产品图片实现类
 * 
 * @author disentice
 *
 */
@Service
public class ProductImageServiceImpl implements ProductImageService {

	@Autowired
	ProductImageMapper productImageMapper;

	@Override
	public void add(ProductImage pi) {
		productImageMapper.insert(pi);
	}

	@Override
	public void delete(int id) {
		productImageMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(ProductImage pi) {
		productImageMapper.updateByPrimaryKey(pi);
	}

	@Override
	public ProductImage get(int id) {
		return productImageMapper.selectByPrimaryKey(id);
	}

	@Override
	public List list(int pid, String type) {
		ProductImageExample example = new ProductImageExample();
		example.createCriteria().andPidEqualTo(pid).andTypeEqualTo(type);//同时匹配pid和type
		example.setOrderByClause("id desc");
		return productImageMapper.selectByExample(example);
	}

}
