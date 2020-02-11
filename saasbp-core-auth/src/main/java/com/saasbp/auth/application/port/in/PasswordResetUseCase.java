package com.saasbp.auth.application.port.in;

import java.util.UUID;

public interface PasswordResetUseCase {

	/**
	 * 
	 * @param email
	 * @throws UserNotFound
	 */
	void requestPasswordReset(String email);

	/**
	 * 
	 * @param code
	 * @param newPassword
	 * @throws InvalidCode
	 */
	void fulfillPasswordReset(UUID code, String newPassword);
}
