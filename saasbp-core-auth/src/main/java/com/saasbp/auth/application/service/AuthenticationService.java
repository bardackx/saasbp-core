package com.saasbp.auth.application.service;

import com.saasbp.auth.application.port.in.AuthenticationUseCase;
import com.saasbp.auth.application.port.in.CredentialsDontMatchException;
import com.saasbp.auth.application.port.in.UserEmailIsNotConfirmedException;
import com.saasbp.auth.application.port.out.CreateHashedPassword;
import com.saasbp.auth.application.port.out.FindUserByEmail;
import com.saasbp.auth.domain.HashedPassword;
import com.saasbp.auth.domain.User;

public class AuthenticationService implements AuthenticationUseCase {

	private FindUserByEmail usersRepository;
	private CreateHashedPassword hashedPasswordFactory;

	public AuthenticationService(FindUserByEmail usersRepository, CreateHashedPassword hashedPasswordFactory) {
		this.usersRepository = usersRepository;
		this.hashedPasswordFactory = hashedPasswordFactory;
	}

	@Override
	public User authenticateUserByEmail(String email, String password) {

		User user = usersRepository.findUserByEmail(email).orElseThrow(() -> new CredentialsDontMatchException());

		if (!user.getEmailConfirmation().isConfirmed())
			throw new UserEmailIsNotConfirmedException();

		HashedPassword userPassword = user.getPassword();

		HashedPassword incomingPassword = hashedPasswordFactory.createHashedPassword(password, userPassword.getSalt());

		if (!incomingPassword.equals(userPassword))
			throw new CredentialsDontMatchException();

		return user;
	}

}
