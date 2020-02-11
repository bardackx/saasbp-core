package com.saasbp.auth.usecases.tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.saasbp.auth.application.port.in.InvalidCode;
import com.saasbp.auth.application.port.in.PrincipalIsNotUser;
import com.saasbp.auth.application.port.in.UserEmailIsNotConfirmed;
import com.saasbp.auth.application.port.in.UserNotFound;
import com.saasbp.auth.application.port.out.FindEmailResetByCode;
import com.saasbp.auth.application.port.out.FindUserByUuid;
import com.saasbp.auth.application.port.out.SaveEmailReset;
import com.saasbp.auth.application.port.out.SaveUser;
import com.saasbp.auth.application.port.out.SendEmailResetEmail;
import com.saasbp.auth.application.service.EmailResetService;
import com.saasbp.auth.domain.EmailConfirmation;
import com.saasbp.auth.domain.EmailReset;
import com.saasbp.auth.domain.User;
import com.saasbp.common.security.Principal;
import com.saasbp.common.security.PrincipalService;

public class EmailResetTests {

	private SaveEmailReset saveEmailReset;
	private FindEmailResetByCode findEmailResetByCode;
	private SendEmailResetEmail sendEmailResetEmail;
	private PrincipalService principalService;
	private FindUserByUuid findUserByUuid;
	private SaveUser saveUser;

	private EmailResetService useCase;

	@Before
	public void setup() {

		sendEmailResetEmail = mock(SendEmailResetEmail.class);
		principalService = mock(PrincipalService.class);
		findEmailResetByCode = mock(FindEmailResetByCode.class);
		saveEmailReset = mock(SaveEmailReset.class);
		saveUser = mock(SaveUser.class);
		findUserByUuid = mock(FindUserByUuid.class);

		useCase = new EmailResetService(sendEmailResetEmail, principalService, saveEmailReset, findEmailResetByCode,
				saveUser, findUserByUuid);
	}

	@Test
	public void requestEmailResetTest() {

		final UUID uuid = UUID.randomUUID();

		final String newEmail = "new@domain.com";

		// given an authenticated principal
		Principal principal = new Principal(uuid);
		when(principalService.getAuthenticatedCaller()).thenReturn(principal);

		// given a confirmed email user
		User user = new User();
		user.setUuid(uuid);
		user.setEmail("n/a");
		user.setEmailConfirmation(new EmailConfirmation(UUID.randomUUID(), true));
		when(findUserByUuid.findUserByUuid(uuid)).thenReturn(Optional.of(user));

		// when
		useCase.requestEmailReset(uuid, newEmail);

		// then
		verify(saveEmailReset)
				.saveEmailReset(argThat(e -> uuid.equals(e.getUser()) && newEmail.equals(e.getNewEmail())));
		verify(sendEmailResetEmail).sendEmailResetEmail(any(User.class), any(EmailReset.class));
	}

	@Test
	public void fulfillEmailResetTest() {

		final UUID code = UUID.randomUUID();
		final UUID userUuid = UUID.randomUUID();
		final String newEmail = "new@domain.com";
		final Instant timestamp = Instant.now();
		final UUID emailConfirmationCode = UUID.randomUUID();

		// given an existing EmailReset for the given email
		EmailReset emailReset = new EmailReset(code, userUuid, newEmail, timestamp, false);
		when(findEmailResetByCode.findEmailResetByCode(code)).thenReturn(Optional.of(emailReset));

		// given an existing user for the given email
		User user = new User();
		user.setUuid(userUuid);
		user.setEmailConfirmation(new EmailConfirmation(emailConfirmationCode, true));
		when(findUserByUuid.findUserByUuid(userUuid)).thenReturn(Optional.of(user));

		// when
		useCase.fulfillEmailReset(code);

		// then
		verify(saveEmailReset).saveEmailReset(any(EmailReset.class));

		// then
		User expectedUser = new User();
		expectedUser.setUuid(userUuid);
		expectedUser.setEmail(newEmail);
		expectedUser.setEmailConfirmation(new EmailConfirmation(emailConfirmationCode, true));
		verify(saveUser).saveUser(expectedUser);

	}

	@Test(expected = UserEmailIsNotConfirmed.class)
	public void emailIsNotConfirmedOnResetEmailTest() {

		final UUID uuid = UUID.randomUUID();
		final String newEmail = "new@example.com";

		// given an authenticated principal
		Principal principal = new Principal(uuid);
		when(principalService.getAuthenticatedCaller()).thenReturn(principal);

		// given a user without confirmed email
		User user = new User();
		user.setUuid(uuid);
		user.setEmail("n/a");
		user.setEmailConfirmation(new EmailConfirmation(UUID.randomUUID(), false));
		when(findUserByUuid.findUserByUuid(uuid)).thenReturn(Optional.of(user));

		// when
		useCase.requestEmailReset(uuid, newEmail);
	}

	@Test(expected = UserNotFound.class)
	public void userNotFoundOnResetEmailTest() {
		final UUID userUuid = UUID.randomUUID();
		final String newEmail = "new@example.com";
		useCase.requestEmailReset(userUuid, newEmail);
	}

	@Test(expected = InvalidCode.class)
	public void invalidPasswordResetCodeOnFulfillPasswordTest() {
		useCase.fulfillEmailReset(UUID.randomUUID());
	}

	@Test(expected = PrincipalIsNotUser.class)
	public void principalIsNotUserOnRequestEmailResetTest() {

		final UUID uuid = UUID.randomUUID();

		final String newEmail = "new@domain.com";

		// given an authenticated principal
		Principal principal = new Principal(UUID.randomUUID());
		when(principalService.getAuthenticatedCaller()).thenReturn(principal);

		// given a confirmed email user
		User user = new User();
		user.setUuid(uuid);
		user.setEmail("n/a");
		user.setEmailConfirmation(new EmailConfirmation(UUID.randomUUID(), true));
		when(findUserByUuid.findUserByUuid(uuid)).thenReturn(Optional.of(user));

		// when
		useCase.requestEmailReset(uuid, newEmail);
	}
}
