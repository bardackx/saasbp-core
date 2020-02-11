package com.saasbp.auth.application.port.out;

import com.saasbp.auth.domain.PasswordReset;
import com.saasbp.auth.domain.User;

public interface SendPasswordResetEmail {

	void sendPasswordResetEmail(User user, PasswordReset e);

}