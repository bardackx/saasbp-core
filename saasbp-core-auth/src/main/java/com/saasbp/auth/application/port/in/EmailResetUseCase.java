package com.saasbp.auth.application.port.in;

import java.util.UUID;

public interface EmailResetUseCase {

	/**
	 * 
	 * @param uuid
	 * @param newEmail
	 * @throws UserNotFoundException
	 */
	void requestEmailReset(UUID uuid, String newEmail);

	/**
	 * 
	 * @param code
	 * @param newPassword
	 * @throws InvalidCodeException
	 */
	void fulfillEmailReset(UUID code);
}
