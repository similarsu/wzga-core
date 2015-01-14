package cn.wzga.core.dao.base.support;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Description: 分页处理类
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
@SuppressWarnings("serial")
public class Pager<T> extends BasePager implements Serializable {
	private Where where;

	private Order order;

	private Join join;

	public Where getWhere() {
		return where;
	}

	public void setWhere(Where where) {
		this.where = where;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Join getJoin() {
		return join;
	}

	public void setJoin(Join join) {
		this.join = join;
	}

	/**
	 * 初始化函数
	 * 
	 * @param pageNo
	 * @param pageSize
	 */
	public Pager(int pageNo, int pageSize) {
		super(pageNo, pageSize, 0);
	}

	/**
	 * 主初始化函数
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param where
	 */
	public Pager(int pageNo, int pageSize, Where where) {
		super(pageNo, pageSize, 0);
		this.where = where;
	}

	/**
	 * 主初始化函数
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param where
	 * @param order
	 */
	public Pager(int pageNo, int pageSize, Where where, Order order) {
		super(pageNo, pageSize, 0);
		this.order = order;
		this.where = where;
	}

	/**
	 * 初始化函数
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param where
	 * @param order
	 * @param joins
	 */
	public Pager(int pageNo, int pageSize, Where where, Order order, Join join) {
		super(pageNo, pageSize, 0);
		this.order = order;
		this.join = join;
		this.where = where;
	}

	/**
	 * 初始化
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 */
	public Pager(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	/**
	 * 数据列表
	 */
	private List<T> list;
	/**
	 * 索引总数
	 */
	private int maxIndexPages = 5;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getMaxIndexPages() {
		return maxIndexPages;
	}

	public void setMaxIndexPages(int maxIndexPages) {
		this.maxIndexPages = maxIndexPages;
	}

	/**
	 * 调整分页参数，使合理化
	 */
	@Override
	public void adjustPage() {
		if (totalCount <= 0) {
			totalCount = 0;
		}
		if (pageSize <= 0) {
			pageSize = DEF_COUNT;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		}
		if ((pageNo - 1) * pageSize >= totalCount) {
			pageNo = totalCount / pageSize;
		}
		if (maxIndexPages <= 0) {
			maxIndexPages = 0;
		}
	}

	/**
	 * 分页索引导航
	 * 
	 * @return 索引数组
	 */
	public int[] getIndexPages() {
		int firstPage = getFirstIndexPage();
		int lastPage = getLastIndexPage(firstPage);

		int[] indexPages = new int[lastPage - firstPage + 1];

		for (int i = 0; i < lastPage - firstPage + 1; i++) {
			indexPages[i] = firstPage + i;
		}
		return indexPages;
	}

	/**
	 * 获取分页索引首页
	 * 
	 * @return int
	 */
	private final int getFirstIndexPage() {
		int firstPage = 1;
		int halfIndexPages = maxIndexPages / 2;
		int totalPage = super.getTotalPage();

		if (pageNo <= halfIndexPages) {
			firstPage = 1;
		} else if (pageNo > totalPage - halfIndexPages
				&& totalPage - halfIndexPages > 0) {
			firstPage = totalPage - maxIndexPages + 1;
			firstPage = Math.max(1, firstPage);
		} else {
			firstPage = pageNo - halfIndexPages;
		}

		return firstPage;
	}

	/**
	 * 获取分页索引尾页
	 * 
	 * @param firstPage
	 * @return int
	 */
	private final int getLastIndexPage(int firstPage) {
		int lastPage = firstPage + maxIndexPages - 1;
		int totalPage = super.getTotalPage();

		return Math.min(lastPage, totalPage);
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		Pager pager = new Pager(1, 10, 0);

		int[] indexPages = pager.getIndexPages();
		for (int i = 0; i < indexPages.length; i++) {
			System.out.println(indexPages[i]);
		}
	}
}
