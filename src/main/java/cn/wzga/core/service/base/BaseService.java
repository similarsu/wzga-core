package cn.wzga.core.service.base;

import java.io.Serializable;
import java.util.List;

import cn.wzga.core.dao.base.support.Join;
import cn.wzga.core.dao.base.support.Order;
import cn.wzga.core.dao.base.support.Pager;
import cn.wzga.core.dao.base.support.Where;

/**
 * <p>
 * Description: 业务层基类接口
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
public interface BaseService<T extends Serializable> {
	/**
	 * 插入一条记录
	 * 
	 * @param t
	 * @return String
	 */
	public Serializable add(T t);

	/**
	 * 更新一条记录
	 * 
	 * @param t
	 */
	public void update(T t);

	/**
	 * 指定某几个参数更新一条记录
	 * 
	 * @param t
	 * @param fields
	 *            excample: "dlm", "jh", "sfz", "dwdm", "mm:true", "jsList"
	 *            需要更新的字段属性，mm:true表示为空不更新
	 */
	public void update(T t, String... fields);

	/**
	 * 指定条件的不更新
	 * 
	 * @param t
	 * @param fields
	 */
	public void updateWithOut(T t, String... fields);

	/**
	 * 通过删除一条记录
	 * 
	 * @param id
	 */
	public void delete(Serializable id);

	/**
	 * 查询分页列表
	 * 
	 * @param pager
	 * @return Pager
	 */
	public Pager<T> findPage(Pager<T> pager);

	/**
	 * 查询一条记录
	 * 
	 * @param id
	 * @return T
	 */
	public T load(Serializable id);

	/**
	 * 查询一条记录带关联
	 * 
	 * @param id
	 * @param join
	 * @return T
	 */
	public T load(Serializable id, Join join);

	/**
	 * 通过条件查询一条记录
	 * 
	 * @param where
	 * @return T
	 */
	public T find(Where where);

	/**
	 * 通过条件查询一条记录带关联
	 * 
	 * @param where
	 * @param join
	 * @return T
	 */
	public T find(Where where, Join join);

	/**
	 * 通过条件获取总数
	 * 
	 * @param where
	 * @return int
	 */
	public int getCount(Where where);

	/**
	 * 获取列表
	 * 
	 * @param where
	 * @param order
	 * @return List
	 */
	public List<T> findAll(Where where, Order order);

	/**
	 * 获取列表带关联
	 * 
	 * @param where
	 * @param order
	 * @param join
	 * @return List
	 */
	public List<T> findAll(Where where, Order order, Join join);

	/**
	 * 获取列表
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
	 * 获取列表带关联
	 * 
	 * @param where
	 * @param order
	 * @param offset
	 * @param size
	 * @param join
	 * @return List
	 */
	public List<T> findAll(Where where, Order order, Integer offset,
			Integer size, Join join);
}
