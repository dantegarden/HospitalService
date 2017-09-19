package com.dvt.HospitalService.commons.exception;

import org.springframework.dao.DataAccessException;

/**
 * @ClassName: BiException
 * @Description: 自定义业务异常
 *
 */
public class BiException extends DataAccessException {

	private static final long serialVersionUID = 1L;
	
	public BiException(String msg) {
		super(msg);
	}
	
	public BiException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
