package com.saasbp.auth.application.port.in;

public class EmailAlreadyUsedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String email;

	public EmailAlreadyUsedException(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
