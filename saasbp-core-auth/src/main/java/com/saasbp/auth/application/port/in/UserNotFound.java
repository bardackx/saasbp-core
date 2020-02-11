package com.saasbp.auth.application.port.in;

public class UserNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String field;
	private final String value;

	public UserNotFound(String field, String value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}

}
