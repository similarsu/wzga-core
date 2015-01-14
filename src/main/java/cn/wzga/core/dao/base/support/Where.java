package cn.wzga.core.dao.base.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import cn.wzga.core.util.PropertyUtil;
import cn.wzga.core.util.StringUtil;

/**
 * <p>
 * Description: 条件支持类
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
public class Where implements Serializable {

	protected StringBuffer whereStr;
	protected ArrayList<Object> values;

	private Object param;

	private ArrayList<String> fields;

	public StringBuffer getWhereStr() {
		return whereStr;
	}

	public void setWhereStr(StringBuffer whereStr) {
		this.whereStr = whereStr;
	}

	public ArrayList<Object> getValues() {
		return values;
	}

	public void setValues(ArrayList<Object> values) {
		this.values = values;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public Where() {
		whereStr = new StringBuffer();
		values = new ArrayList<Object>();

		fields = new ArrayList<String>();
	}

	public static Where getInstance(Object param) {
		Where where = new Where();
		where.setParam(param);
		return where;
	}

	public static Where getInstance() {
		Where where = new Where();
		return where;
	}

	/**
	 * 相等判断
	 * 
	 * @param field
	 * @param value
	 * @return Where
	 */
	public Where equal(String field, Object value) {
		if (StringUtil.isBlank(field) || value == null) {
			return this;
		}
		whereStr.append("and " + field + "=? ");
		values.add(value);
		return this;
	}

	/**
	 * 相等判断
	 * 
	 * @param field
	 * @return Where
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public Where equal(String field) throws Exception {
		if (param == null || StringUtil.isBlank(field)) {
			return this;
		}

		Object value = PropertyUtil.getProperty(param, field);
		if (value == null) {
			return this;
		}

		Class pojoClass = param.getClass();

		if (field.indexOf(".") > -1) {
			this.equal(field, value);
		} else {
			this.equal(StringUtil.firstToLowerCase(pojoClass.getSimpleName())
					+ "." + field, value);
		}

		fields.add(field);

		return this;
	}

	/**
	 * 不等判断
	 * 
	 * @param field
	 * @param value
	 * @return Where
	 */
	public Where notEqual(String field, Object value) {
		if (StringUtil.isBlank(field) || value == null) {
			return this;
		}
		whereStr.append("and " + field + "!=? ");
		values.add(value);
		return this;
	}

	/**
	 * 不等判断
	 * 
	 * @param field
	 * @return Where
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public Where notEqual(String field) throws Exception {
		if (param == null || StringUtil.isBlank(field)) {
			return this;
		}
		Object value = PropertyUtil.getProperty(param, field);
		if (value == null) {
			return this;
		}

		Class pojoClass = param.getClass();

		if (field.indexOf(".") > -1) {
			this.notEqual(field, value);
		} else {
			this.notEqual(
					StringUtil.firstToLowerCase(pojoClass.getSimpleName())
							+ "." + field, value);
		}

		fields.add(field);

		return this;
	}

	/**
	 * 外键条件
	 * 
	 * @param field
	 * @return Where
	 */
	public Where foreign(String field) throws Exception {
		if (param == null || StringUtil.isBlank(field)) {
			return this;
		}
		if (field.indexOf(".") > -1) {
			Object value = PropertyUtil.getProperty(param, field);
			if (value == null) {
				return this;
			}

			Class pojoClass = param.getClass();
			this.equal(StringUtil.firstToLowerCase(pojoClass.getSimpleName())
					+ "." + field, value);
		} else {
			return this;
		}

		fields.add(field);

		return this;
	}

	/**
	 * like判断
	 * 
	 * @param field
	 * @param value
	 * @return Where
	 */
	public Where like(String field, Object value) {
		if (StringUtil.isBlank(field) || value == null) {
			return this;
		}
		whereStr.append("and " + field + " like ? ");
		values.add("%" + value + "%");
		return this;
	}

	/**
	 * like判断
	 * 
	 * @param field
	 * @return Where
	 */
	public Where like(String field) throws Exception {
		if (param == null || StringUtil.isBlank(field)) {
			return this;
		}
		Object value = PropertyUtil.getProperty(param, field);
		if (value == null) {
			return this;
		}

		Class pojoClass = param.getClass();

		if (field.indexOf(".") > -1) {
			this.like(field, value);
		} else {
			this.like(StringUtil.firstToLowerCase(pojoClass.getSimpleName())
					+ "." + field, value);
		}

		fields.add(field);

		return this;
	}

	/**
	 * 前匹配Like
	 * 
	 * @param field
	 * @param value
	 * @return Where
	 */
	public Where prefixLike(String field, Object value) {
		if (StringUtil.isBlank(field) || value == null) {
			return this;
		}
		whereStr.append("and " + field + " like ? ");
		values.add(value + "%");
		return this;
	}

	/**
	 * 前匹配like
	 * 
	 * @param field
	 * @return Where
	 */
	public Where prefixLike(String field) throws Exception {
		if (param == null || StringUtil.isBlank(field)) {
			return this;
		}
		Object value = PropertyUtil.getProperty(param, field);
		if (value == null) {
			return this;
		}

		Class pojoClass = param.getClass();

		if (field.indexOf(".") > -1) {
			this.prefixLike(field, value);
		} else {
			this.prefixLike(
					StringUtil.firstToLowerCase(pojoClass.getSimpleName())
							+ "." + field, value);
		}

		fields.add(field);

		return this;
	}

	/**
	 * 大于
	 * 
	 * @param field
	 * @param value
	 * @return Where
	 */
	public Where greaterThan(String field, Object value) {
		if (StringUtil.isBlank(field) || value == null) {
			return this;
		}
		whereStr.append("and " + field + " >= ? ");
		values.add(value);
		return this;
	}

	/**
	 * 大于
	 * 
	 * @param field
	 * @return Where
	 */
	public Where greaterThan(String field) throws Exception {
		if (param == null || StringUtil.isBlank(field)) {
			return this;
		}
		Object value = PropertyUtil.getProperty(param, field);
		if (value == null) {
			return this;
		}

		Class pojoClass = param.getClass();

		if (field.indexOf(".") > -1) {
			this.greaterThan(field, value);
		} else {
			this.greaterThan(
					StringUtil.firstToLowerCase(pojoClass.getSimpleName())
							+ "." + field, value);
		}

		fields.add(field);

		return this;
	}

	/**
	 * 小于
	 * 
	 * @param field
	 * @param value
	 * @return Where
	 */
	public Where lessThan(String field, Object value) {
		if (StringUtil.isBlank(field) || value == null) {
			return this;
		}
		whereStr.append("and " + field + " <= ? ");
		values.add(value);
		return this;
	}

	/**
	 * 小于
	 * 
	 * @param field
	 * @return Where
	 */
	public Where lessThan(String field) throws Exception {
		if (param == null || StringUtil.isBlank(field)) {
			return this;
		}
		Object value = PropertyUtil.getProperty(param, field);
		if (value == null) {
			return this;
		}

		Class pojoClass = param.getClass();

		if (field.indexOf(".") > -1) {
			this.lessThan(field, value);
		} else {
			this.lessThan(
					StringUtil.firstToLowerCase(pojoClass.getSimpleName())
							+ "." + field, value);
		}

		fields.add(field);

		return this;
	}

	/**
	 * 后匹配like
	 * 
	 * @param field
	 * @param value
	 * @return Where
	 */
	public Where suffixLike(String field, Object value) {
		if (StringUtil.isBlank(field) || value == null) {
			return this;
		}
		whereStr.append("and " + field + " like ? ");
		values.add("%" + value);
		return this;
	}

	/**
	 * 后匹配like
	 * 
	 * @param field
	 * @return Where
	 */
	public Where suffixLike(String field) throws Exception {
		if (param == null || StringUtil.isBlank(field)) {
			return this;
		}
		Object value = PropertyUtil.getProperty(param, field);
		if (value == null) {
			return this;
		}

		Class pojoClass = param.getClass();

		if (field.indexOf(".") > -1) {
			this.suffixLike(field, value);
		} else {
			this.suffixLike(
					StringUtil.firstToLowerCase(pojoClass.getSimpleName())
							+ "." + field, value);
		}

		fields.add(field);

		return this;
	}

	/**
	 * 所有条件验证判断
	 * 
	 * @return Where
	 */
	public Where setValues() throws Exception {
		if (param == null) {
			return this;
		}

		Class pojoClass = param.getClass();
		Field[] declaredFields = pojoClass.getDeclaredFields();
		for (Field field : declaredFields) {
			// 如果已作为条件判断则继续下一条
			if (fields.contains(field.getName())) {
				continue;
			}

			int mod = field.getModifiers();
			if (!Modifier.toString(mod).equals("private")) {
				continue;
			}

			String type = field.getType().toString();
			if (type.equals("class java.lang.String")) {
				Method method = pojoClass.getMethod("get"
						+ StringUtil.firstToUpperCase(field.getName()));
				String value = (String) method.invoke(param);

				if (!StringUtil.isBlank(value)) {
					whereStr.append("and "
							+ StringUtil.firstToLowerCase(pojoClass
									.getSimpleName()) + "."
							+ StringUtil.firstToLowerCase(field.getName())
							+ "=? ");
					values.add(value);
				}
			} else if (type.equals("class java.lang.Integer")
					|| type.equals("class java.lang.Long")
					|| type.equals("class java.util.Date")
					|| type.equals("class java.lang.Double")) {
				Method method = pojoClass.getMethod("get"
						+ StringUtil.firstToUpperCase(field.getName()));
				Object value = (Object) method.invoke(param);

				if (value != null) {
					whereStr.append("and "
							+ StringUtil.firstToLowerCase(pojoClass
									.getSimpleName()) + "."
							+ StringUtil.firstToLowerCase(field.getName())
							+ "=? ");
					values.add(value);
				}
			} else if (type.equals("class java.util.List")
					|| type.equals("interface java.util.Set")) {
				// 一对多或多对多关联不做条件判断
				continue;
			} else {
				// 其他类型属于多对一或一对一关联
				Method method = pojoClass.getMethod("get"
						+ StringUtil.firstToUpperCase(field.getName()));
				Object value = (Object) method.invoke(param);
				if (value != null) {
					Method method2 = value.getClass().getMethod("getId");
					Long idValue = (Long) method2.invoke(value);
					if (idValue != null) {
						whereStr.append("and "
								+ StringUtil.firstToLowerCase(pojoClass
										.getSimpleName()) + "."
								+ StringUtil.firstToLowerCase(field.getName())
								+ ".id=? ");
						values.add(idValue);
					}
				}

			}

		}

		return this;
	}

	public Where joinState(String field) {
		if (StringUtil.isBlank(field)) {
			return this;
		}
		whereStr.append("and " + field + ".state=1 ");
		return this;
	}

	/**
	 * in 查询
	 * 
	 * @param field
	 * @param values
	 * @return
	 */
	public Where in(String field, Object[] values) {
		if (StringUtil.isBlank(field) || values == null || values.length == 0) {
			return this;
		}
		whereStr.append("and " + field + " in ( ");
		int i = 0;
		int j = values.length;
		for (Object value : values) {
			if (++i != j) {
				whereStr.append("?,");
			} else {
				whereStr.append("?");
			}
			this.values.add(value);
		}
		whereStr.append(") ");
		return this;
	}

	/**
	 * not in 查询
	 * 
	 * @param field
	 * @param values
	 * @return
	 */
	public Where notIn(String field, Object[] values) {
		if (StringUtil.isBlank(field) || values == null || values.length == 0) {
			return this;
		}
		whereStr.append("and " + field + " not in ( ");
		int i = 0;
		int j = values.length;
		for (Object value : values) {
			if (++i != j) {
				whereStr.append("?,");
			} else {
				whereStr.append("?");
			}
			this.values.add(value);
		}
		whereStr.append(") ");
		return this;
	}

	/**
	 * 空判断
	 * 
	 * @param field
	 * @param value
	 * @return Where
	 */
	public Where isNull(String field) {
		if (StringUtil.isBlank(field)) {
			return this;
		}
		whereStr.append("and " + field + " is null ");
		return this;
	}

	/**
	 * 不为空判断
	 * 
	 * @param field
	 * @param value
	 * @return Where
	 */
	public Where isNotNull(String field) {
		if (StringUtil.isBlank(field)) {
			return this;
		}
		whereStr.append("and " + field + " is not null ");
		return this;
	}

	/**
	 * 结果集不存在
	 * 
	 * @param subJPQL
	 * @param values
	 * @return
	 */
	public Where notExists(String subJPQL, ArrayList<Object> values) {
		if (StringUtil.isBlank(subJPQL)) {
			return this;
		}
		whereStr.append("and not exists ( " + subJPQL + " ) ");
		this.values.addAll(values);
		return this;
	}

	/**
	 * 结果集存在
	 * 
	 * @param subJPQL
	 * @param values
	 * @return
	 */
	public Where exists(String subJPQL, ArrayList<Object> values) {
		if (StringUtil.isBlank(subJPQL)) {
			return this;
		}
		whereStr.append("and exists ( " + subJPQL + " ) ");
		this.values.addAll(values);
		return this;
	}

	/**
	 * 直接组ql查询
	 * 
	 * @param field
	 * @param values
	 * @return Where
	 */
	public Where general(String field, Object[] values) {
		if (StringUtil.isBlank(field) || values == null || values.length == 0) {
			return this;
		}
		whereStr.append(field);
		for (Object value : values) {
			this.values.add(value);
		}
		return this;
	}

	@Override
	public String toString() {
		if (whereStr != null) {
			return whereStr.toString();
		} else {
			return null;
		}
	}

	public static void main(String[] args) {

	}
}
