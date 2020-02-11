package com.saasbp.auth.application.port.out;

import com.saasbp.auth.domain.User;

public interface SendEmailConfirmationEmail {

	void sendEmailConfirmationEmail(User user);

}