package ssm_test.pojo;

import java.util.List;


public class Category {
    private Integer id;

    private String name;
    
    	//非数据库字段！
    //分类下的产品集合
    private List<Product> products;

    //产品显示集合
    private List<List<Product>> productsByRow;

    public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<List<Product>> getProductsByRow() {
		return productsByRow;
	}

	public void setProductsByRow(List<List<Product>> productsByRow) {
		this.productsByRow = productsByRow;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}