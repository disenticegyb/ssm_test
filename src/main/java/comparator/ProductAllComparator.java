package comparator;

import java.util.Comparator;

import ssm_test.pojo.Product;

/**
 * 比较器
 * @author disentice
 *
 */
//按评论*销量排序
public class ProductAllComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1, Product p2) {
		return p2.getReviewCount()*p2.getSaleCount()-p1.getReviewCount()*p1.getSaleCount();
	}

}


