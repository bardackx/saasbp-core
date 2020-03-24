package com.saasbp.auth.usecases.tests;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.saasbp.auth.application.port.in.EmailAlreadyUsedException;
import com.saasbp.auth.application.port.in.InvalidCodeException;
import com.saasbp.auth.application.port.in.SignInUseCase;
import com.saasbp.auth.application.port.in.UserNotFoundException;
import com.saasbp.auth.application.port.out.CreateHashedPasswordWithRandomSalt;
import com.saasbp.auth.application.port.out.FindEmailConfirmationByCode;
import com.saasbp.auth.application.port.out.FindUserByEmail;
import com.saasbp.auth.application.port.out.IsEmailBeingUsed;
import com.saasbp.auth.application.port.out.SaveEmailConfirmation;
import com.saasbp.auth.application.port.out.SaveUser;
import com.saasbp.auth.application.port.out.SendEmailConfirmationEmail;
import com.saasbp.auth.application.service.SignInService;
import com.saasbp.auth.domain.EmailConfirmation;
import com.saasbp.auth.domain.User;

/**
 * 
 * Acceptance Tests
 * <ul>
 * <li>for BDD (a.k.a. Conversations with the stakeholders)
 * <li>Demonstrate and document business requirements
 * <li>�Business� documentation: What does the system do?
 * <li>Test a use case in isolation, very fast (no GUI, no DB, etc.)
 * <li>Use your favourite BDD framework (we use Yatspec)
 * </ul>
 * 
 * @author barda
 *
 */
public class SignInTest {

	private SendEmailConfirmationEmail sendConfirmationEmail;
	private CreateHashedPasswordWithRandomSalt createHashedPasswordWithRandomSalt;

	private SignInUseCase useCase;

	private SaveUser saveUser;
	private SaveEmailConfirmation saveEmailConfirmation;
	private IsEmailBeingUsed isEmailBeingUsed;
	private FindUserByEmail findUserByEmail;
	private FindEmailConfirmationByCode findEmailConfirmationByCode;

	@Before
	public void setup() {

		sendConfirmationEmail = mock(SendEmailConfirmationEmail.class);
		createHashedPasswordWithRandomSalt = mock(CreateHashedPasswordWithRandomSalt.class);

		saveUser = mock(SaveUser.class);
		saveEmailConfirmation = mock(SaveEmailConfirmation.class);
		isEmailBeingUsed = mock(IsEmailBeingUsed.class);
		findUserByEmail = mock(FindUserByEmail.class);
		findEmailConfirmationByCode = mock(FindEmailConfirmationByCode.class);

		useCase = new SignInService(createHashedPasswordWithRandomSalt, sendConfirmationEmail, isEmailBeingUsed,
				saveUser, findUserByEmail, findEmailConfirmationByCode, saveEmailConfirmation);
	}

	// Se espera una EmailAlreadyUsedException ...
	@Test(expected = EmailAlreadyUsedException.class)
	public void emailAlreadyUsedDurignSignInTest() {

		final String email = "random@email.com";

		// dado que el email proporcionado ya está usado ...
		when(isEmailBeingUsed.isEmailBeingUsed(email)).thenReturn(true);

		useCase.signIn(email, "randomPassword123");
	}

	@Test(expected = UserNotFoundException.class)
	public void userNotFoundOnSendConfirmationEmailTest() {
		useCase.resendConfirmationEmail("random@email.com");
	}

	@Test
	public void sendConfirmationEmailTest() {

		final UUID uuid = UUID.randomUUID();
		final String email = "random@email.com";

		User user = new User();
		user.setUuid(uuid);
		user.setEmail(email);

		when(findUserByEmail.findUserByEmail(email)).thenReturn(Optional.of(user));

		useCase.resendConfirmationEmail(email);

		verify(sendConfirmationEmail)
				.sendEmailConfirmationEmail(argThat(e -> uuid.equals(e.getUuid()) && email.equals(e.getEmail())));
	}

	@Test(expected = InvalidCodeException.class)
	public void invalidEmailConfirmationCodeOnConfirmEmail() {
		useCase.confirmEmail(UUID.randomUUID());
	}

	@Test
	public void confirmEmail() {

		final UUID uuid = UUID.randomUUID();

		EmailConfirmation confirmation = new EmailConfirmation(uuid, false);
		when(findEmailConfirmationByCode.findEmailConfirmationByCode(uuid)).thenReturn(Optional.of(confirmation));

		useCase.confirmEmail(uuid);

		EmailConfirmation expected = new EmailConfirmation(uuid, true);
		verify(saveEmailConfirmation).saveEmailConfirmation(expected);
	}

	@Test
	public void signInTest() {

		final String email = "random@email.com";
		final String password = "randomPassword123";

		useCase.signIn(email, password);

		verify(saveUser).saveUser(argThat(user -> user.getUuid() != null && email.equals(user.getEmail())
				&& !user.getEmailConfirmation().isConfirmed()));

		verify(sendConfirmationEmail).sendEmailConfirmationEmail(argThat(user -> user.getUuid() != null
				&& email.equals(user.getEmail()) && !user.getEmailConfirmation().isConfirmed()));

	}
}
