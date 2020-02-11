package com.saasbp.auth.application.service;

import java.util.UUID;

import com.saasbp.auth.application.port.in.EmailAlreadyUsedException;
import com.saasbp.auth.application.port.in.InvalidCode;
import com.saasbp.auth.application.port.in.SignInUseCase;
import com.saasbp.auth.application.port.in.UserNotFound;
import com.saasbp.auth.application.port.out.CreateHashedPasswordWithRandomSalt;
import com.saasbp.auth.application.port.out.FindEmailConfirmationByCode;
import com.saasbp.auth.application.port.out.FindUserByEmail;
import com.saasbp.auth.application.port.out.IsEmailBeingUsed;
import com.saasbp.auth.application.port.out.SaveEmailConfirmation;
import com.saasbp.auth.application.port.out.SaveUser;
import com.saasbp.auth.application.port.out.SendEmailConfirmationEmail;
import com.saasbp.auth.domain.EmailConfirmation;
import com.saasbp.auth.domain.HashedPassword;
import com.saasbp.auth.domain.User;

public class SignInService implements SignInUseCase {

	private CreateHashedPasswordWithRandomSalt hashedPasswordProvider;
	private SendEmailConfirmationEmail sendNotificationEmail;
	private IsEmailBeingUsed isEmailBeingUsed;
	private SaveUser saveUser;
	private FindUserByEmail findUserByEmail;
	private FindEmailConfirmationByCode findEmailConfirmationByCode;
	private SaveEmailConfirmation saveEmailConfirmation;

	public SignInService(CreateHashedPasswordWithRandomSalt hashedPasswordProvider,
			SendEmailConfirmationEmail sendNotificationEmail, IsEmailBeingUsed isEmailBeingUsed, SaveUser saveUser,
			FindUserByEmail findUserByEmail, FindEmailConfirmationByCode findEmailConfirmationByCode,
			SaveEmailConfirmation saveEmailConfirmation) {
		super();
		this.hashedPasswordProvider = hashedPasswordProvider;
		this.sendNotificationEmail = sendNotificationEmail;
		this.isEmailBeingUsed = isEmailBeingUsed;
		this.saveUser = saveUser;
		this.findUserByEmail = findUserByEmail;
		this.findEmailConfirmationByCode = findEmailConfirmationByCode;
		this.saveEmailConfirmation = saveEmailConfirmation;
	}

	public void signIn(String email, String password) {

		if (isEmailBeingUsed.isEmailBeingUsed(email))
			throw new EmailAlreadyUsedException(email);

		HashedPassword hashedPassword = hashedPasswordProvider.createHashedPasswordWithRandomSalt(password);

		User user = new User();
		user.setUuid(UUID.randomUUID());
		user.setPassword(hashedPassword);
		user.setEmail(email);
		user.setEmailConfirmation(new EmailConfirmation());

		saveUser.saveUser(user);

		sendNotificationEmail.sendEmailConfirmationEmail(user);
	}

	public void resendConfirmationEmail(String email) {

		User user = findUserByEmail //
				.findUserByEmail(email) //
				.orElseThrow(() -> new UserNotFound("email", email));

		sendNotificationEmail.sendEmailConfirmationEmail(user);
	}

	public void confirmEmail(UUID uuid) {

		EmailConfirmation confirmation = findEmailConfirmationByCode //
				.findEmailConfirmationByCode(uuid) //
				.orElseThrow(() -> new InvalidCode());

		confirmation.setConfirmed(true);

		saveEmailConfirmation.saveEmailConfirmation(confirmation);
	}

}