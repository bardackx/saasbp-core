package com.saasbp.auth.application.port.in;

import java.util.UUID;

public interface EmailResetUseCase {

	/**
	 * 
	 * @param uuid
	 * @param newEmail
	 * @throws UserNotFound
	 */
	void requestEmailReset(UUID uuid, String newEmail);

	/**
	 * 
	 * @param code
	 * @param newPassword
	 * @throws InvalidCode
	 */
	void fulfillEmailReset(UUID code);
}
