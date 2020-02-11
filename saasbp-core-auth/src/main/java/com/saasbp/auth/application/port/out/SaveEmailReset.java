package com.saasbp.auth.application.port.out;

import com.saasbp.auth.domain.EmailReset;

public interface SaveEmailReset {

	void saveEmailReset(EmailReset e);

}