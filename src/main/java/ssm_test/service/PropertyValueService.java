package ssm_test.service;

import java.util.List;

import ssm_test.pojo.Product;
import ssm_test.pojo.PropertyValue;

public interface PropertyValueService {
	
	void init(Product p);
    void update(PropertyValue pv);
 
    PropertyValue get(int ptid, int pid);
    List<PropertyValue> list(int pid);

}
