package com.saasbp.auth.application.port.out;

import java.util.Optional;
import java.util.UUID;

import com.saasbp.auth.domain.PasswordReset;

public interface FindPasswordResetByCode {

	Optional<PasswordReset> findPasswordResetByCode(UUID code);

}