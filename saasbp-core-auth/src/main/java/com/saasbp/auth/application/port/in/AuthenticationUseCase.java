package com.saasbp.auth.application.port.in;

import com.saasbp.auth.domain.User;

public interface AuthenticationUseCase {

	public User authenticateUserByEmail(String email, String password);
}
