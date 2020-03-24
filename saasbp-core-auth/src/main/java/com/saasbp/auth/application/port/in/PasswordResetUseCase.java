package com.saasbp.auth.application.port.in;

import java.util.UUID;

public interface PasswordResetUseCase {

	/**
	 * 
	 * @param email
	 * @throws UserNotFoundException
	 */
	void requestPasswordReset(String email);

	/**
	 * 
	 * @param code
	 * @param newPassword
	 * @throws InvalidCodeException
	 */
	void fulfillPasswordReset(UUID code, String newPassword);
}
