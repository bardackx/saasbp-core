package com.saasbp.auth.application.port.out;

import com.saasbp.auth.domain.User;

public interface SaveUser {

	void saveUser(User user);

}