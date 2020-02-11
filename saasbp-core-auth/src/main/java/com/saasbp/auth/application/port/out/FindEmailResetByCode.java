package com.saasbp.auth.application.port.out;

import java.util.Optional;
import java.util.UUID;

import com.saasbp.auth.domain.EmailReset;

public interface FindEmailResetByCode {

	Optional<EmailReset> findEmailResetByCode(UUID code);

}