package com.saasbp.auth.application.port.out;

import com.saasbp.auth.domain.PasswordReset;

public interface SavePasswordReset {

	void savePasswordReset(PasswordReset e);

}