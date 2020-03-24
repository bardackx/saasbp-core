package com.saasbp.auth.application.service;

import java.util.UUID;

import com.saasbp.auth.application.port.in.EmailResetUseCase;
import com.saasbp.auth.application.port.in.InvalidCodeException;
import com.saasbp.auth.application.port.in.PrincipalIsNotUserException;
import com.saasbp.auth.application.port.in.UserEmailIsNotConfirmedException;
import com.saasbp.auth.application.port.in.UserNotFoundException;
import com.saasbp.auth.application.port.out.FindEmailResetByCode;
import com.saasbp.auth.application.port.out.FindUserByUuid;
import com.saasbp.auth.application.port.out.SaveEmailReset;
import com.saasbp.auth.application.port.out.SaveUser;
import com.saasbp.auth.application.port.out.SendEmailResetEmail;
import com.saasbp.auth.domain.EmailReset;
import com.saasbp.auth.domain.User;
import com.saasbp.common.security.Principal;
import com.saasbp.common.security.PrincipalService;

public class EmailResetService implements EmailResetUseCase {

	private SendEmailResetEmail sendEmailResetEmail;
	private PrincipalService principalService;
	private SaveEmailReset saveEmailReset;
	private FindEmailResetByCode findEmailResetByCode;
	private SaveUser saveUser;
	private FindUserByUuid findUserByUuid;

	public EmailResetService(SendEmailResetEmail sendEmailResetEmail, PrincipalService principalService,
			SaveEmailReset saveEmailReset, FindEmailResetByCode findEmailResetByCode, SaveUser saveUser,
			FindUserByUuid findUserByUuid) {
		super();
		this.sendEmailResetEmail = sendEmailResetEmail;
		this.principalService = principalService;
		this.saveEmailReset = saveEmailReset;
		this.findEmailResetByCode = findEmailResetByCode;
		this.saveUser = saveUser;
		this.findUserByUuid = findUserByUuid;
	}

	@Override
	public void requestEmailReset(UUID userUuid, String newEmail) {

		Principal caller = principalService.getAuthenticatedCaller();

		User user = findUserByUuid //
				.findUserByUuid(userUuid) //
				.orElseThrow(() -> new UserNotFoundException("uuid", userUuid.toString()));

		if (!caller.getId().equals(user.getUuid()))
			throw new PrincipalIsNotUserException();

		if (!user.getEmailConfirmation().isConfirmed())
			throw new UserEmailIsNotConfirmedException();

		EmailReset e = new EmailReset(userUuid, newEmail);

		saveEmailReset.saveEmailReset(e);
		sendEmailResetEmail.sendEmailResetEmail(user, e);
	}

	@Override
	public void fulfillEmailReset(UUID code) {

		EmailReset emailReset = findEmailResetByCode.findEmailResetByCode(code) //
				.orElseThrow(() -> new InvalidCodeException());

		emailReset.fulfill();

		User user = findUserByUuid //
				.findUserByUuid(emailReset.getUser()) //
				.orElseThrow(() -> new UserNotFoundException("uuid", emailReset.getUser().toString()));

		user.setEmail(emailReset.getNewEmail());

		saveEmailReset.saveEmailReset(emailReset);
		saveUser.saveUser(user);
	}

}
