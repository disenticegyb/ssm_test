package comparator;

import java.util.Comparator;

import ssm_test.pojo.Product;
//按评论多少排序
public class ProductReviewComparator implements Comparator<Product> {

	@Override
	public int compare(Product p1, Product p2) {
		return p2.getReviewCount()-p1.getReviewCount();
	}

}

