package com.saasbp.auth.application.port.out;

import com.saasbp.auth.domain.HashedPassword;

public interface CreateHashedPassword {

	HashedPassword createHashedPassword(String password, byte[] salt);

}