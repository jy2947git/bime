package com.focaplo.myfuse.exception;

public class UserActivityException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserActivityException(String errorNoSuerUserToSubmitOrder) {
		super(errorNoSuerUserToSubmitOrder);
	}

}
