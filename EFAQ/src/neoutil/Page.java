/**
 * File-Name:Page.java
 * 
 * Created on 2011-4-28 下午02:18:15
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * 
 *          Copyright (c) 2009, Peking University
 * 
 * 
 */
package neoutil;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * @param <T>
 *            Page's type.
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * @version 1.0 2011-4-28 下午02:18:15
 */
public class Page<T> {


	public static final String	ASC			= "asc";
	public static final String	DESC		= "desc";

	// -- page paragram --//
	protected int				pageNo		= 1;
	protected int				pageSize	= 1;
	protected String			orderBy		= null;
	protected String			order		= null;
	protected boolean			autoCount	= true;

	// -- return result --//
	protected List<T>			result		= Collections.emptyList();
	protected long				totalCount	= -1;


	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}


	/**
	 * get the page number
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * set the page number
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	public Page<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	/**
	 * get the page size
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * set the page size
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = 1;
		}
	}

	public Page<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/**
	 * get the first this page.
	 */
	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}

	/**
	 * get by order
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * set by order
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	public Page<T> orderBy(final String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}

	/**
	 * get the direction of order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * set the direction of order
	 * 
	 * @param order
	 *            
	 */
	public void setOrder(final String order) {
		// 检查order字符串的合法值
		String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
				throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
			}
		}

		this.order = StringUtils.lowerCase(order);
	}

	public Page<T> order(final String theOrder) {
		setOrder(theOrder);
		return this;
	}


	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}


	public boolean isAutoCount() {
		return autoCount;
	}

	
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	public Page<T> autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}



	
	public List<T> getResult() {
		return result;
	}


	public void setResult(final List<T> result) {
		this.result = result;
	}


	public long getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}


	public long getTotalPages() {
		if (totalCount < 0) {
			return -1;
		}

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}


	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}


	public int getNextPage() {
		if (isHasNext()) {
			return pageNo + 1;
		}
		else {
			return pageNo;
		}
	}


	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}


	public int getPrePage() {
		if (isHasPre()) {
			return pageNo - 1;
		}
		else {
			return pageNo;
		}
	}

}
