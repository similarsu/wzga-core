package cn.wzga.core.dao.base;

import java.io.Serializable;
import java.util.List;

import cn.wzga.core.dao.base.support.Join;
import cn.wzga.core.dao.base.support.Order;
import cn.wzga.core.dao.base.support.Pager;
import cn.wzga.core.dao.base.support.Where;

/**
 * <p>
 * Description: 持久层基类接口
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
public interface BaseDao<T extends Serializable> {
	public void save(T entity);

	public void update(T entity);

	public void update(T t, String... fields) throws Exception;

	public void updateWithOut(T t, String... fields) throws Exception;

	public void delete(Where where);

	public void delete(T entity);

	public void deleteById(Serializable id);

	/**
	 * 根据主键查询一条记录
	 * 
	 * @param id
	 * @return T
	 */
	public T load(Serializable id);

	/**
	 * 根据主机按查询一条记录带关联
	 * 
	 * @param id
	 * @param join
	 * @return T
	 */
	public T load(Serializable id, Join join);

	/**
	 * 查找一条记录
	 * 
	 * @param where
	 * @return T
	 */
	public T find(Where where);

	/**
	 * 查找一条记录带关联
	 * 
	 * @param where
	 * @param join
	 * @return T
	 */
	public T find(Where where, Join join);

	/**
	 * 查找所有列表
	 * 
	 * @param where
	 * @param order
	 * @return List
	 */
	public List<T> findAll(Where where, Order order);

	/**
	 * 查找所有列表
	 * 
	 * @param where
	 * @return List
	 */
	public List<T> findAll(Where where);

	/**
	 * 查找列表
	 * 
	 * @param where
	 * @param order
	 * @param offset
	 * @param size
	 * @return List
	 */
	public List<T> findAll(Where where, Order order, Integer offset,
			Integer size);

	/**
	 * 查找所有列表带关联
	 * 
	 * @param where
	 * @param order
	 * @param joins
	 * @return List
	 */
	public List<T> findAll(Where where, Order order, Join join);

	/**
	 * 查找列表带关联
	 * 
	 * @param where
	 * @param order
	 * @param offset
	 * @param size
	 * @param joins
	 * @return List
	 */
	public List<T> findAll(Where where, Order order, Integer offset,
			Integer size, Join join);

	/**
	 * 查询总数
	 * 
	 * @param where
	 * @return int
	 */
	public int getCount(Where where);

	/**
	 * 分页查找
	 * 
	 * @param pager
	 * @return Pager
	 */
	public Pager<T> findPage(Pager<T> pager);

}
