package com.saasbp.auth.application.port.in;

import java.util.UUID;

public interface SignInUseCase {

	/**
	 * 
	 * @param email
	 * @param password
	 * @throws EmailAlreadyUsedException
	 */
	void signIn(String email, String password);

	/**
	 * 
	 * @param uuid
	 * @throws InvalidCodeException
	 */
	void confirmEmail(UUID confirmationCode);

	/**
	 * 
	 * @param email
	 * @throws UserNotFoundException
	 */
	void resendConfirmationEmail(String email);

}