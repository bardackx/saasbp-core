package com.saasbp.auth.application.service;

import java.util.UUID;

import com.saasbp.auth.application.port.in.InvalidCode;
import com.saasbp.auth.application.port.in.PasswordResetUseCase;
import com.saasbp.auth.application.port.in.UserEmailIsNotConfirmed;
import com.saasbp.auth.application.port.in.UserNotFound;
import com.saasbp.auth.application.port.out.CreateHashedPasswordWithRandomSalt;
import com.saasbp.auth.application.port.out.FindPasswordResetByCode;
import com.saasbp.auth.application.port.out.FindUserByEmail;
import com.saasbp.auth.application.port.out.SavePasswordReset;
import com.saasbp.auth.application.port.out.SaveUser;
import com.saasbp.auth.application.port.out.SendPasswordResetEmail;
import com.saasbp.auth.domain.PasswordReset;
import com.saasbp.auth.domain.User;

public class PasswordResetService implements PasswordResetUseCase {

	private CreateHashedPasswordWithRandomSalt hashedPasswordFactory;
	private SendPasswordResetEmail sendPasswordResetEmail;
	private SavePasswordReset savePasswordReset;
	private FindPasswordResetByCode findPasswordResetByCode;
	private FindUserByEmail findUserByEmail;
	private SaveUser saveUser;

	public PasswordResetService(CreateHashedPasswordWithRandomSalt hashedPasswordFactory,
			SendPasswordResetEmail sendPasswordResetEmail, SavePasswordReset savePasswordReset,
			FindPasswordResetByCode findPasswordResetByCode, FindUserByEmail findUserByEmail, SaveUser saveUser) {
		super();
		this.hashedPasswordFactory = hashedPasswordFactory;
		this.sendPasswordResetEmail = sendPasswordResetEmail;
		this.savePasswordReset = savePasswordReset;
		this.findPasswordResetByCode = findPasswordResetByCode;
		this.findUserByEmail = findUserByEmail;
		this.saveUser = saveUser;
	}

	@Override
	public void requestPasswordReset(String email) {

		User user = findUserByEmail //
				.findUserByEmail(email) //
				.orElseThrow(() -> new UserNotFound("email", email));

		if (!user.getEmailConfirmation().isConfirmed())
			throw new UserEmailIsNotConfirmed();

		PasswordReset e = new PasswordReset(email);

		savePasswordReset.savePasswordReset(e);
		sendPasswordResetEmail.sendPasswordResetEmail(user, e);
	}

	@Override
	public void fulfillPasswordReset(UUID code, String newPassword) {

		PasswordReset passwordReset = findPasswordResetByCode //
				.findPasswordResetByCode(code) //
				.orElseThrow(() -> new InvalidCode());

		passwordReset.fulfill();

		User user = findUserByEmail //
				.findUserByEmail(passwordReset.getEmail()) //
				.orElseThrow(() -> new UserNotFound("mail", passwordReset.getEmail()));

		user.setPassword(hashedPasswordFactory.createHashedPasswordWithRandomSalt(newPassword));

		savePasswordReset.savePasswordReset(passwordReset);
		saveUser.saveUser(user);
	}

}
