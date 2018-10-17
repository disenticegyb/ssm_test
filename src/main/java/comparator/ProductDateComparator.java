package comparator;

import java.util.Comparator;

import ssm_test.pojo.Product;

//按创建时间顺序
public class ProductDateComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1, Product p2) {
		return p1.getCreateDate().compareTo(p2.getCreateDate());
	}

}

