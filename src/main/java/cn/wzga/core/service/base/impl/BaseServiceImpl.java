package cn.wzga.core.service.base.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import cn.wzga.core.dao.base.BaseDao;
import cn.wzga.core.dao.base.support.BaseUtil;
import cn.wzga.core.dao.base.support.Join;
import cn.wzga.core.dao.base.support.Order;
import cn.wzga.core.dao.base.support.Pager;
import cn.wzga.core.dao.base.support.Where;
import cn.wzga.core.exception.ServiceException;
import cn.wzga.core.service.base.BaseService;
import cn.wzga.core.util.PropertyUtil;

/**
 * <p>
 * Description: 业务层基类
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Transactional("transactionManager")
public class BaseServiceImpl<T extends Serializable> implements BaseService<T> {
	private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

	protected final static String ADD_ERROR_MSG = "error.base.add";
	protected final static String UPDATE_ERROR_MSG = "error.base.update";
	protected final static String DELETE_ERROR_MSG = "error.base.delete";
	protected final static String FIND_ERROR_MSG = "error.base.find";

	private Class<T> pojoClass;

	public BaseServiceImpl() {
		this.pojoClass = BaseUtil.getSuperClassGenricType(getClass());
	}

	public Class<T> getPojoClass() {
		return this.pojoClass;
	}

	private BaseDao baseDao;

	public void injectBaseDao(BaseDao baseDao) {

		this.baseDao = baseDao;
	}

	/**
	 * 插入一条记录
	 * 
	 * @param t
	 * @return String
	 */
	public Serializable add(T t) {
		try {
			baseDao.save(t);

			Method method = pojoClass.getMethod("getId");
			Serializable id = (Serializable) method.invoke(t);
			return id;
		} catch (Exception e) {
			logger.error(null, e);
			e.printStackTrace();
			throw new ServiceException(ADD_ERROR_MSG);
		}
	}

	/**
	 * 更新一条记录
	 * 
	 * @param t
	 */
	public void update(T t) {
		try {
			Method method = pojoClass.getMethod("getNbbh");
			Serializable id = (Serializable) method.invoke(t);

			T tTmp = (T) baseDao.load(id);

			// 设置需要更新的方法
			this.setUpdateValues(tTmp, t);

			baseDao.update(tTmp);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(UPDATE_ERROR_MSG);
		}
	}

	/**
	 * 指定某几个参数更新一条记录
	 * 
	 * @param t
	 * @param fields
	 *            excample: "dlm", "jh", "sfz", "dwdm", "mm:true", "jsList"
	 *            需要更新的字段属性，mm:true表示为空不更新
	 */
	public void update(T t, String... fields) {
		try {
			baseDao.update(t, fields);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(UPDATE_ERROR_MSG);
		}
	}

	/**
	 * 指定条件的不更新
	 * 
	 * @param t
	 * @param fields
	 */
	public void updateWithOut(T t, String... fields) {
		try {
			baseDao.updateWithOut(t, fields);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(UPDATE_ERROR_MSG);
		}
	}

	/**
	 * 设置更新条件,在子类重写
	 * 
	 * @param tTmp
	 * @param t
	 * @throws Exception
	 */
	protected void setUpdateValues(T tTmp, T t) throws Exception {
		PropertyUtil.copyProperties(tTmp, t);
	}

	/**
	 * 设置更新条件
	 * 
	 * @param tTmp
	 * @param t
	 * @param fields
	 *            需要更新的字段属性，xxx:true表示为空不更新
	 * @throws Exception
	 */
	protected void setUpdateValues(T tTmp, T t, String... fields)
			throws Exception {
		if (fields == null) {
			setUpdateValues(tTmp, t);
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
	}

	/**
	 * 通过删除一条记录
	 * 
	 * @param id
	 */
	public void delete(Serializable id) {
		try {
			baseDao.deleteById(id);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(DELETE_ERROR_MSG);
		}
	}

	/**
	 * 查询分页列表
	 * 
	 * @param pager
	 * @return Pager
	 */

	public Pager<T> findPage(Pager<T> pager) {
		try {
			return baseDao.findPage(pager);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}

	/**
	 * 查询一条记录
	 * 
	 * @param id
	 * @return T
	 */

	public T load(Serializable id) {
		try {
			return (T) baseDao.load(id);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}

	/**
	 * 查询一条记录带关联
	 * 
	 * @param id
	 * @param join
	 * @return T
	 */

	public T load(Serializable id, Join join) {
		try {
			return (T) baseDao.load(id, join);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}

	/**
	 * 通过条件查询一条记录
	 * 
	 * @param where
	 * @return T
	 */

	public T find(Where where) {
		try {
			return (T) baseDao.find(where);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}

	/**
	 * 通过条件查询一条记录带关联
	 * 
	 * @param where
	 * @param join
	 * @return T
	 */

	public T find(Where where, Join join) {
		try {
			return (T) baseDao.find(where, join);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}

	/**
	 * 通过条件获取总数
	 * 
	 * @param where
	 * @return int
	 */

	public int getCount(Where where) {
		try {
			return baseDao.getCount(where);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}

	/**
	 * 获取列表
	 * 
	 * @param where
	 * @param order
	 * @return List
	 */

	public List<T> findAll(Where where, Order order) {
		try {
			return baseDao.findAll(where, order);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}

	/**
	 * 获取列表带关联
	 * 
	 * @param where
	 * @param order
	 * @param join
	 * @return List
	 */

	public List<T> findAll(Where where, Order order, Join join) {
		try {
			return baseDao.findAll(where, order, join);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}

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
			Integer size) {
		try {
			return baseDao.findAll(where, order, offset, size);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}

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
			Integer size, Join join) {
		try {
			return baseDao.findAll(where, order, offset, size, join);
		} catch (Exception e) {
			logger.error(null, e);
			throw new ServiceException(FIND_ERROR_MSG);
		}
	}
}
