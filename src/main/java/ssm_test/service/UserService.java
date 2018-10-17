package ssm_test.service;

import java.util.List;

import ssm_test.pojo.User;

public interface UserService {
	
	//一套CURD
	void add(User c);

	void delete(int id);

	void update(User c);

	User get(int id);

	List list();
	
	//前台方法：判断名字是否存在
	boolean isExist(String name);

	//获取登陆账号和密码
	User get(String name, String password);

}
