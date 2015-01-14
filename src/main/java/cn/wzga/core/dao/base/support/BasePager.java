package cn.wzga.core.dao.base.support;

import java.io.Serializable;

/**
 * <p>
 * Description: 分页基类
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
@SuppressWarnings("serial")
public class BasePager implements Serializable {

	public static final int DEF_COUNT = 20;

	protected int totalCount = 0;
	protected int pageSize = 20;
	protected int pageNo = 1;

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 初始化
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 */
	public BasePager(int pageNo, int pageSize, int totalCount) {
		if (totalCount <= 0) {
			this.totalCount = 0;
		} else {
			this.totalCount = totalCount;
		}
		if (pageSize <= 0) {
			this.pageSize = DEF_COUNT;
		} else {
			this.pageSize = pageSize;
		}
		if (pageNo <= 0) {
			this.pageNo = 1;
		} else {
			this.pageNo = pageNo;
		}
	}

	/**
	 * 调整分页参数，使合理化
	 */
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
	}

	/**
	 * 获取页码
	 * 
	 * @return int 页码
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 获取页大小
	 * 
	 * @return int 页大小
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获取总数
	 * 
	 * @return int 总数
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 获取总页数
	 * 
	 * @return int 总页数
	 */
	public int getTotalPage() {
		int totalPage = totalCount / pageSize;
		if (totalCount % pageSize != 0 || totalPage == 0) {
			totalPage++;
		}
		return totalPage;
	}

	/**
	 * 是否第一页
	 * 
	 * @return boolean
	 */
	public boolean getIsFirstPage() {
		return pageNo <= 1;
	}

	/**
	 * 是否最后页
	 * 
	 * @return boolean
	 */
	public boolean getIsLastPage() {
		return pageNo >= getTotalPage();
	}

	/**
	 * 获取下一页码
	 * 
	 * @return int
	 */
	public int getNextPage() {
		if (getIsLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}

	/**
	 * 获取前一页码
	 * 
	 * @return int
	 */
	public int getPrePage() {
		if (getIsFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * 获取当前页起始偏移量
	 * 
	 * @return int
	 */
	public int getOffset() {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * 获取当前页起始偏移量
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return int
	 */
	public final static int getOffset(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}

}