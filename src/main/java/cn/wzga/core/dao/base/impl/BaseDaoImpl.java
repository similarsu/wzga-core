package cn.wzga.core.dao.base.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cn.wzga.core.dao.base.BaseDao;
import cn.wzga.core.dao.base.support.BaseUtil;
import cn.wzga.core.dao.base.support.Join;
import cn.wzga.core.dao.base.support.Order;
import cn.wzga.core.dao.base.support.Pager;
import cn.wzga.core.dao.base.support.Where;
import cn.wzga.core.util.PropertyUtil;
import cn.wzga.core.util.StringUtil;

/**
 * <p>
 * Description: 持久层基类
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
@SuppressWarnings("unchecked")
public class BaseDaoImpl<T extends Serializable> implements BaseDao<T> {
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	private Class<T> pojoClass;

	public BaseDaoImpl() {
		this.pojoClass = BaseUtil.getSuperClassGenricType(getClass());
	}

	protected Class<T> getPojoClass() {
		return this.pojoClass;
	}

	public void save(T entity) {
		Session session = getSession();
		session.save(entity);
	}

	public void update(T entity) {
		Session session = getSession();
		session.update(entity);
	}

	/**
	 * 指定条件更新
	 * 
	 * @param t
	 * @param fields
	 *            excample: "dlm", "jh", "sfz", "dwdm", "mm:true", "jsList"
	 *            需要更新的字段属性，mm:true表示为空不更新
	 * @throws Exception
	 */
	public void update(T t, String... fields) throws Exception {
		Session session = getSession();

		Method method = pojoClass.getMethod("getId");
		Serializable id = (Serializable) method.invoke(t);

		T tTmp = this.load(id);

		if (fields == null) {
			PropertyUtil.copyProperties(tTmp, t);
			session.update(tTmp);
			return;
		}
		for (String field : fields) {
			if (field.indexOf(":") < 0) {
				Object obj = PropertyUtil.getProperty(t, field);
				PropertyUtil.setProperty(tTmp, field, obj);
			} else {
				String[] fieldArray = field.split(":");
				if ("true".equals(fieldArray[1])) {
					Object obj = PropertyUtil.getProperty(t, fieldArray[0]);
					if (obj != null) {
						PropertyUtil.setProperty(tTmp, fieldArray[0], obj);
					}
				}
			}
		}

		session.update(tTmp);
	}

	/**
	 * 指定条件的不更新
	 * 
	 * @param t
	 * @param fields
	 * @throws Exception
	 */
	public void updateWithOut(T t, String... fields) throws Exception {
		Session session = getSession();

		Method method = pojoClass.getMethod("getId");
		Serializable id = (Serializable) method.invoke(t);

		T tTmp = this.load(id);

		// 全部更新
		if (fields == null) {
			PropertyUtil.copyProperties(tTmp, t);
			session.update(tTmp);
			return;
		}

		Field[] declaredFields = t.getClass().getDeclaredFields();
		for (Field declaredField : declaredFields) {
			int mod = declaredField.getModifiers();
			if (!Modifier.toString(mod).equals("private")) {
				continue;
			}
			// 不包含在数组中的字段更新
			if (!StringUtil.contains(declaredField.getName(), fields)) {
				Object obj = PropertyUtil.getProperty(t,
						declaredField.getName());
				PropertyUtil.setProperty(tTmp, declaredField.getName(), obj);
			}
		}

		session.update(tTmp);
	}

	public void delete(Where where) {
		StringBuffer ql = new StringBuffer("delete from "
				+ pojoClass.getSimpleName() + " "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " where 1=1 ");

		if (where != null)
			ql.append(where.getWhereStr());

		Query query = this.createQuery(ql.toString(), where.getValues()
				.toArray());

		query.executeUpdate();
	}

	public void delete(T entity) {
		Session session = getSession();
		session.delete(entity);
	}

	public void deleteById(Serializable id) {
		T entity = load(id);
		this.delete(entity);
	}

	/**
	 * 根据主键查询一条记录
	 * 
	 * @param id
	 * @return T
	 */
	public T load(Serializable id) {
		StringBuffer hql = new StringBuffer("select "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " from " + pojoClass.getSimpleName() + " "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " where 1=1  and "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ ".id=?");

		Session session = getSession();
		Query query = session.createQuery(hql.toString());
		query.setParameter(0, id);
		return (T) query.uniqueResult();
	}

	/**
	 * 根据主机按查询一条记录带关联
	 * 
	 * @param id
	 * @param join
	 * @return T
	 */
	public T load(Serializable id, Join join) {
		StringBuffer hql = new StringBuffer("select "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " from " + pojoClass.getSimpleName() + " "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName()) + " ");
		if (join != null) {
			hql.append(join.toString(StringUtil.firstToLowerCase(pojoClass
					.getSimpleName())));
		}

		hql.append("where 1=1 and "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ ".id=?");

		Session session = getSession();
		Query query = session.createQuery(hql.toString());
		query.setParameter(0, id);
		return (T) query.uniqueResult();
	}

	/**
	 * 通过sql查询一条记录
	 * 
	 * @param ql
	 * @param values
	 * @return T
	 */
	protected T find(String hql, Object... values) {
		Query query = this.createQuery(hql.toString());

		return (T) query.uniqueResult();
	}

	/**
	 * 建立query
	 * 
	 * @param queryString
	 * @param values
	 * @return Query
	 */
	protected Query createQuery(String queryString, Object... values) {
		Session session = getSession();
		Query query = session.createQuery(queryString);
		int i = 0;
		for (Object val : values) {
			query.setParameter(i++, val);
		}
		return query;
	}

	/**
	 * 查找一条记录
	 * 
	 * @param where
	 * @return T
	 */
	public T find(Where where) {
		StringBuffer ql = new StringBuffer("select "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " from " + pojoClass.getSimpleName() + " "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " where 1=1 ");
		if (where != null)
			ql.append(where.getWhereStr());

		Query query = null;
		if (where != null && !where.getValues().isEmpty()) {
			query = this
					.createQuery(ql.toString(), where.getValues().toArray());
		} else {
			query = this.createQuery(ql.toString());
		}

		List<T> resultList = query.list();
		if (resultList == null || resultList.isEmpty()) {
			return null;
		} else if (resultList.size() > 1) {
			throw new RuntimeException("not unique");
		} else {
			return resultList.get(0);
		}
	}

	/**
	 * 查找一条记录带关联
	 * 
	 * @param entity
	 * @param join
	 * @return T
	 */
	public T find(Where where, Join join) {
		StringBuffer ql = new StringBuffer("select "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " from " + pojoClass.getSimpleName() + " "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName()) + " ");
		if (join != null) {
			ql.append(join.toString(StringUtil.firstToLowerCase(pojoClass
					.getSimpleName())));
		}

		ql.append("where 1=1 ");
		if (where != null)
			ql.append(where.getWhereStr());

		Query query = null;
		if (where != null && !where.getValues().isEmpty()) {
			query = this
					.createQuery(ql.toString(), where.getValues().toArray());
			query.setFirstResult(0);
		} else {
			query = this.createQuery(ql.toString());
			query.setFirstResult(0);
		}

		List<T> resultList = query.list();
		if (resultList == null || resultList.isEmpty()) {
			return null;
		} else if (resultList.size() > 1) {
			throw new RuntimeException("not unique");
		} else {
			return resultList.get(0);
		}
	}

	/**
	 * 查找所有列表
	 * 
	 * @param where
	 * @param order
	 * @return List
	 */
	public List<T> findAll(Where where, Order order) {
		return findAll(where, order, null, null);
	}

	/**
	 * 查找所有列表
	 * 
	 * @param where
	 * @return List
	 */
	public List<T> findAll(Where where) {
		return findAll(where, null);
	}

	/**
	 * 查找列表
	 * 
	 * @param entity
	 * @param order
	 * @param offset
	 * @param size
	 * @return List
	 */
	public List<T> findAll(Where where, Order order, Integer offset,
			Integer size) {
		StringBuffer ql = new StringBuffer("select "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " from " + pojoClass.getSimpleName() + " "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " where 1=1 ");
		if (where != null)
			ql.append(where.getWhereStr());

		if (order != null && !StringUtil.isBlank(order.toString())) {
			ql.append("order by " + order.toString());
		}

		Query query = null;
		if (where != null && !where.getValues().isEmpty()) {
			query = this
					.createQuery(ql.toString(), where.getValues().toArray());
		} else {
			query = this.createQuery(ql.toString());
		}
		if (offset != null) {
			query.setFirstResult(offset);
		}
		if (size != null) {
			query.setMaxResults(size);
		}

		return query.list();
	}

	/**
	 * 查找所有列表带关联
	 * 
	 * @param where
	 * @param order
	 * @param joins
	 * @return List
	 */
	public List<T> findAll(Where where, Order order, Join join) {
		return findAll(where, order, null, null, join);
	}

	/**
	 * 查找列表带关联
	 * 
	 * @param entity
	 * @param order
	 * @param offset
	 * @param size
	 * @param joins
	 * @return List
	 */
	public List<T> findAll(Where where, Order order, Integer offset,
			Integer size, Join join) {
		StringBuffer ql = new StringBuffer("select "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " from " + pojoClass.getSimpleName() + " "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName()) + " ");

		if (join != null) {
			ql.append(join.toString(StringUtil.firstToLowerCase(pojoClass
					.getSimpleName())));
		}
		ql.append("where 1=1 ");
		if (where != null)
			ql.append(where.getWhereStr());

		if (order != null && !StringUtil.isBlank(order.toString())) {
			ql.append("order by " + order.toString());
		}

		Query query = null;
		if (where != null && !where.getValues().isEmpty()) {
			query = this
					.createQuery(ql.toString(), where.getValues().toArray());
		} else {
			query = this.createQuery(ql.toString());
		}
		if (offset != null) {
			query.setFirstResult(offset);
		}
		if (size != null) {
			query.setMaxResults(size);
		}

		return query.list();
	}

	/**
	 * 查询总数
	 * 
	 * @param where
	 * @return int
	 */
	public int getCount(Where where) {
		StringBuffer ql = new StringBuffer("select count("
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ ") from " + pojoClass.getSimpleName() + " "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ " where 1=1 ");
		if (where != null)
			ql.append(where.getWhereStr());

		Query query = null;
		if (where != null && !where.getValues().isEmpty()) {
			query = this
					.createQuery(ql.toString(), where.getValues().toArray());
		} else {
			query = this.createQuery(ql.toString());
		}

		return ((Number) query.uniqueResult()).intValue();
	}

	/**
	 * 查询总数
	 * 
	 * @param where
	 * @param join
	 * @return int
	 */
	public int getCount(Where where, Join join) {
		StringBuffer ql = new StringBuffer("select count("
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName())
				+ ") from " + pojoClass.getSimpleName() + " "
				+ StringUtil.firstToLowerCase(pojoClass.getSimpleName()) + " ");
		if (join != null) {
			ql.append(join.toString(
					StringUtil.firstToLowerCase(pojoClass.getSimpleName()))
					.replace("fetch", ""));
		}
		ql.append("where 1=1 ");
		if (where != null)
			ql.append(where.getWhereStr());

		Query query = null;
		if (where != null && !where.getValues().isEmpty()) {
			query = this
					.createQuery(ql.toString(), where.getValues().toArray());
		} else {
			query = this.createQuery(ql.toString());
		}

		return ((Number) query.uniqueResult()).intValue();
	}

	/**
	 * 分页查找
	 * 
	 * @param pager
	 * @return Pager
	 */
	public Pager<T> findPage(Pager<T> pager) {
		List<T> list = null;
		int count;
		if (pager.getJoin() != null) {
			list = this.findAll(pager.getWhere(), pager.getOrder(),
					pager.getOffset(), pager.getPageSize(), pager.getJoin());
			count = this.getCount(pager.getWhere(), pager.getJoin());
		} else {
			list = this.findAll(pager.getWhere(), pager.getOrder(),
					pager.getOffset(), pager.getPageSize());
			count = this.getCount(pager.getWhere());
		}

		pager.setList(list);
		pager.setTotalCount(count);
		return pager;
	}
}
