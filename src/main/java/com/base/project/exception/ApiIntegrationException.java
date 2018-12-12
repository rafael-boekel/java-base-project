package com.base.project.exception;

import org.springframework.http.HttpStatus;

public class ApiIntegrationException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public ApiIntegrationException(String methodKey, HttpStatus httpStatus) {
		super(httpStatus);
		this.setArgs(new String[] {methodKey, Integer.toString(httpStatus.value())});
	}

}
