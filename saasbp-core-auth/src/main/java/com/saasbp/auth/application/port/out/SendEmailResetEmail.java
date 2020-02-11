package com.saasbp.auth.application.port.out;

import com.saasbp.auth.domain.EmailReset;
import com.saasbp.auth.domain.User;

public interface SendEmailResetEmail {

	void sendEmailResetEmail(User user, EmailReset e);

}