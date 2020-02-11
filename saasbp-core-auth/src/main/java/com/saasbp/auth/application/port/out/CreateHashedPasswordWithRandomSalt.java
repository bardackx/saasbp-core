package com.saasbp.auth.application.port.out;

import com.saasbp.auth.domain.HashedPassword;

public interface CreateHashedPasswordWithRandomSalt {

	HashedPassword createHashedPasswordWithRandomSalt(String password);

}