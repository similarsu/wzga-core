package cn.wzga.core.util;

import java.lang.reflect.Field;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * <p>
 * Description: 对象公用类
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
public class PropertyUtil extends PropertyUtils {

	/**
	 * 清空对象中所有属性
	 * 
	 * @param bean
	 */
	public static void clear(Object bean) {
		try {
			Field[] declaredFields = bean.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				setProperty(bean, field.getName(), null);
			}
		} catch (Exception e) {
		}
	}
}
