package com.saasbp.auth.application.port.out;

import com.saasbp.auth.domain.EmailConfirmation;

public interface SaveEmailConfirmation {

	void saveEmailConfirmation(EmailConfirmation confirmation);

}