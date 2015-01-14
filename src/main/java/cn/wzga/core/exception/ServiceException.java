package cn.wzga.core.exception;

import org.springframework.dao.DataAccessException;

@SuppressWarnings("serial")
public class ServiceException extends DataAccessException {
	public ServiceException(String msg) {
		super(msg);
	}
}