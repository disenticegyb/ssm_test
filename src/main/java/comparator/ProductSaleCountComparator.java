package comparator;

import java.util.Comparator;

import ssm_test.pojo.Product;
//按销量排序
public class ProductSaleCountComparator implements Comparator<Product> {

	@Override
	public int compare(Product p1, Product p2) {
		return p2.getSaleCount()-p1.getSaleCount();
	}

}

