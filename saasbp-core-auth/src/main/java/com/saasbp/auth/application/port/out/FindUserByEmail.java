package com.saasbp.auth.application.port.out;

import java.util.Optional;

import com.saasbp.auth.domain.User;

public interface FindUserByEmail {

	Optional<User> findUserByEmail(String email);

	

}