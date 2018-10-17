package ssm_test.service;

import java.util.List;

import ssm_test.pojo.Review;

/**
 * 累计评论的接口
 * 
 * @author disentice
 *
 */
public interface ReviewService {

	//一套CRUD
	void add(Review c);

	void delete(int id);

	void update(Review c);

	Review get(int id);

	//查询产品评论集合
	List list(int pid);

	//获取总条数
	int getCount(int pid);
}
