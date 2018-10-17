package ssm_test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssm_test.mapper.UserMapper;
import ssm_test.pojo.User;
import ssm_test.pojo.UserExample;
import ssm_test.service.UserService;

/**
 * 用户服务层实现类
 * @author disentice
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Override
	public void add(User c) {
		userMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(User c) {
		userMapper.updateByPrimaryKey(c);
	}

	@Override
	public User get(int id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public List list() {
		UserExample example =new UserExample();
        example.setOrderByClause("id desc");
        return userMapper.selectByExample(example);
	}

	@Override
	public boolean isExist(String name) {
		 UserExample example =new UserExample();
	        example.createCriteria().andNameEqualTo(name);
	        List<User> result= userMapper.selectByExample(example);
	        if(!result.isEmpty())
	            return true;
	        return false;
	}
	
	//根据name‘pasword进行查询，true则返回User
	 @Override
	    public User get(String name, String password) {
	        UserExample example =new UserExample();
	        example.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
	        List<User> result= userMapper.selectByExample(example);
	        if(result.isEmpty())
	            return null;
	        return result.get(0);
	    }

}
