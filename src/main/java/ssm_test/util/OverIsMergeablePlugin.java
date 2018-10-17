package ssm_test.util;

import java.lang.reflect.Field;
import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * mybatis代码重构
 * 通过逆向工程实现；通过数据库表格自动生成pojo、mapper、xml类；
 * 
 * @author disentice
 *
 */
public class OverIsMergeablePlugin extends  PluginAdapter{
	
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
 
    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            Field field = sqlMap.getClass().getDeclaredField("isMergeable");
            field.setAccessible(true);
            field.setBoolean(sqlMap, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
