package com.saasbp.auth.usecases.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.saasbp.auth.application.port.in.AuthenticationUseCase;
import com.saasbp.auth.application.port.in.CredentialsDontMatchException;
import com.saasbp.auth.application.port.out.CreateHashedPassword;
import com.saasbp.auth.application.port.out.FindUserByEmail;
import com.saasbp.auth.application.service.AuthenticationService;
import com.saasbp.auth.domain.EmailConfirmation;
import com.saasbp.auth.domain.HashedPassword;
import com.saasbp.auth.domain.User;

public class AuthenticationTest {

	private FindUserByEmail findUserByEmail;
	private AuthenticationUseCase useCase;
	private CreateHashedPassword createHashedPassword;

	@Before
	public void setup() {
		findUserByEmail = mock(FindUserByEmail.class);
		createHashedPassword = mock(CreateHashedPassword.class);
		useCase = new AuthenticationService(findUserByEmail, createHashedPassword);
	}

	@Test
	public void authenticateUserByEmailTest() {

		final UUID confirmationCode = UUID.randomUUID();
		final String email = "valid@email.com";
		final String password = "validpassword";

		final long seed = 1984;
		final byte[] hash = new byte[16];
		final byte[] salt = new byte[16];
		final Random random = new Random(seed);
		random.nextBytes(hash);
		random.nextBytes(salt);

		// given an existing user
		User t = new User();
		t.setEmail(email);
		t.setPassword(generateHashedPassword(seed));
		t.setEmailConfirmation(new EmailConfirmation(confirmationCode, true));
		when(findUserByEmail.findUserByEmail(email)).thenReturn(Optional.of(t));

		when(createHashedPassword.createHashedPassword(password, salt)).thenReturn(generateHashedPassword(seed));

		User actual = useCase.authenticateUserByEmail(email, password);

		User expected = new User();
		expected.setEmail(email);
		expected.setPassword(generateHashedPassword(seed));
		expected.setEmailConfirmation(new EmailConfirmation(confirmationCode, true));

		assertEquals(expected, actual);
	}

	@Test(expected = CredentialsDontMatchException.class)
	public void credentialsDontMatchOnAuthenticateUserByEmailTest() {

		final String email = "valid@email.com";
		final String password = "validpassword";

		final long seed = 1984;
		final byte[] hash = new byte[16];
		final byte[] salt = new byte[16];
		final Random random = new Random(seed);
		random.nextBytes(hash);
		random.nextBytes(salt);

		// given an existing user
		User t = new User();
		t.setEmail(email);
		t.setPassword(generateHashedPassword(seed));
		t.setEmailConfirmation(new EmailConfirmation(UUID.randomUUID(), true));
		when(findUserByEmail.findUserByEmail(email)).thenReturn(Optional.of(t));

		when(createHashedPassword.createHashedPassword(password, salt)).thenReturn(generateHashedPassword(seed + 1));

		useCase.authenticateUserByEmail(email, password);
	}

	@Test(expected = CredentialsDontMatchException.class)
	public void credentialsDontMatchOnFalseEmailOnAuthenticateUserByEmailTest() {

		final String email = "false@email.com";
		final String password = "validpassword";

		useCase.authenticateUserByEmail(email, password);
	}

	private HashedPassword generateHashedPassword(long seed) {
		final byte[] hash = new byte[16];
		final byte[] salt = new byte[16];
		final Random random = new Random(seed);
		random.nextBytes(hash);
		random.nextBytes(salt);
		return new HashedPassword(hash, salt);
	}
}
