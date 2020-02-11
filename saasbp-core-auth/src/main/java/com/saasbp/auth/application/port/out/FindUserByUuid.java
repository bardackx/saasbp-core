package com.saasbp.auth.application.port.out;

import java.util.Optional;
import java.util.UUID;

import com.saasbp.auth.domain.User;

public interface FindUserByUuid {

	Optional<User> findUserByUuid(UUID uuid);

}
