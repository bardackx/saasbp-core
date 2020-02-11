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
import com.saasbp.auth.application.port.in.UserEmailIsNotConfirmed;
import com.saasbp.auth.application.port.in.UserNotFound;
import com.saasbp.auth.application.port.out.CreateHashedPasswordWithRandomSalt;
import com.saasbp.auth.application.port.out.FindPasswordResetByCode;
import com.saasbp.auth.application.port.out.FindUserByEmail;
import com.saasbp.auth.application.port.out.SavePasswordReset;
import com.saasbp.auth.application.port.out.SaveUser;
import com.saasbp.auth.application.port.out.SendPasswordResetEmail;
import com.saasbp.auth.application.service.PasswordResetService;
import com.saasbp.auth.domain.EmailConfirmation;
import com.saasbp.auth.domain.HashedPassword;
import com.saasbp.auth.domain.PasswordReset;
import com.saasbp.auth.domain.User;

public class PasswordResetTest {

	private CreateHashedPasswordWithRandomSalt createHashedPasswordWithRandomSalt;
	private SendPasswordResetEmail sendPasswordResetEmail;
	private SavePasswordReset savePasswordReset;
	private FindPasswordResetByCode findPasswordResetByCode;
	private FindUserByEmail findUserByEmail;
	private SaveUser saveUser;

	private PasswordResetService useCase;

	@Before
	public void setup() {

		saveUser = mock(SaveUser.class);
		findUserByEmail = mock(FindUserByEmail.class);
		savePasswordReset = mock(SavePasswordReset.class);
		findPasswordResetByCode = mock(FindPasswordResetByCode.class);
		createHashedPasswordWithRandomSalt = mock(CreateHashedPasswordWithRandomSalt.class);
		sendPasswordResetEmail = mock(SendPasswordResetEmail.class);

		useCase = new PasswordResetService(createHashedPasswordWithRandomSalt, sendPasswordResetEmail,
				savePasswordReset, findPasswordResetByCode, findUserByEmail, saveUser);
	}

	@Test
	public void resetPasswordTest() {

		final String email = "name@server.com";

		// given a User with a confirmed email
		User user = new User();
		user.setEmail(email);
		user.setEmailConfirmation(new EmailConfirmation(UUID.randomUUID(), true));
		user.setPassword(new HashedPassword(new byte[16], new byte[16]));
		when(findUserByEmail.findUserByEmail(email)).thenReturn(Optional.of(user));

		// when a password reset is requested for that email
		useCase.requestPasswordReset(email);

		// verify that a PasswordReset was saved with that email
		verify(savePasswordReset).savePasswordReset(argThat(e -> email.equals(e.getEmail())));

		// verify that an email was sent
		verify(sendPasswordResetEmail).sendPasswordResetEmail( //
				any(User.class), //
				argThat(e -> email.equals(e.getEmail())));
	}

	@Test
	public void fulfillPasswordTest() {

		final UUID emailConfirmationCode = UUID.randomUUID();
		final UUID code = UUID.randomUUID();
		final String email = "name@domain.com";
		final String password = "N3w_P4s5W0rD";
		final Instant timestamp = Instant.now();

		// given an unconfirmed PasswordReset
		PasswordReset passwordReset = new PasswordReset(code, email, timestamp, false);
		when(findPasswordResetByCode.findPasswordResetByCode(code)).thenReturn(Optional.of(passwordReset));

		// given a User with a confirmed email
		User user = new User();
		user.setEmail(email);
		user.setEmailConfirmation(new EmailConfirmation(emailConfirmationCode, true));
		when(findUserByEmail.findUserByEmail(email)).thenReturn(Optional.of(user));

		// when fulfillPasswordReset is called for that password reset with a valid
		// password
		useCase.fulfillPasswordReset(code, password);

		// verify that that HashedPassword was generated
		verify(createHashedPasswordWithRandomSalt).createHashedPasswordWithRandomSalt(password);

		// verify that that PasswordReset was fulfilled
		PasswordReset expectedPasswordReset = new PasswordReset(code, email, timestamp, true);
		verify(savePasswordReset).savePasswordReset(expectedPasswordReset);

		// verify that that User password changed
		User expectedSaveUser = new User();
		expectedSaveUser.setEmail(email);
		expectedSaveUser.setEmailConfirmation(new EmailConfirmation(emailConfirmationCode, true));
		verify(saveUser).saveUser(expectedSaveUser);
	}

	@Test(expected = UserEmailIsNotConfirmed.class)
	public void emailIsNotConfirmedOnResetPasswordTest() {

		final String email = "name@example.com";

		// given a user without confirmed email
		User user = new User();
		user.setEmail(email);
		user.setEmailConfirmation(new EmailConfirmation(UUID.randomUUID(), false));
		when(findUserByEmail.findUserByEmail(email)).thenReturn(Optional.of(user));

		// when requestPasswordReset is called for that email
		useCase.requestPasswordReset(email);
	}

	@Test(expected = UserNotFound.class)
	public void userNotFoundOnResetPasswordTest() {
		useCase.requestPasswordReset("non-existing@gmail.com");
	}

	@Test(expected = InvalidCode.class)
	public void invalidPasswordResetCodeOnFulfillPasswordTest() {
		useCase.fulfillPasswordReset(UUID.randomUUID(), "N3w_P4s5W0rD");
	}

}
