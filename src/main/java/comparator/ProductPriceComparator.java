package comparator;

import java.util.Comparator;

import ssm_test.pojo.Product;

//按促销价格排序
public class ProductPriceComparator implements Comparator<Product> {

	@Override
	public int compare(Product p1, Product p2) {
		return (int) (p1.getPromotePrice()-p2.getPromotePrice());
	}

}

