package ssm_test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssm_test.mapper.ReviewMapper;
import ssm_test.pojo.Review;
import ssm_test.pojo.ReviewExample;
import ssm_test.pojo.User;
import ssm_test.service.ReviewService;
import ssm_test.service.UserService;

/**
 * 评论实现类
 * 
 * @author disentice
 *
 */
@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	ReviewMapper reviewMapper;
	@Autowired
	UserService userService;

	@Override
	public void add(Review c) {
		reviewMapper.insert(c);
	}

	@Override
	public void delete(int id) {
		reviewMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Review c) {
		reviewMapper.updateByPrimaryKey(c);
	}

	@Override
	public Review get(int id) {
		return reviewMapper.selectByPrimaryKey(id);
	}

	//查询产品评论集合
	public List<Review> list(int pid) {
		ReviewExample example = new ReviewExample();
		example.createCriteria().andPidEqualTo(pid);
		example.setOrderByClause("id desc");//按id降序

		List<Review> result = reviewMapper.selectByExample(example);
		setUser(result);
		return result;
	}

	public void setUser(List<Review> reviews) {//遍历评论
		for (Review review : reviews) {
			setUser(review);
		}
	}

	//赋值给Review 中的User的属性
	private void setUser(Review review) {
		int uid = review.getUid();
		User user = userService.get(uid);
		review.setUser(user);
	}

	@Override
	public int getCount(int pid) {
		return list(pid).size();
	}
}
