package com.saasbp.common.security;

public interface PrincipalService {

	Principal getCaller();

	/**
	 * 
	 * @return
	 * @throws PrincipalIsNotAuthenticatedException
	 */
	default Principal getAuthenticatedCaller() {
		Principal caller = getCaller();
		if (caller.isAnonymous())
			throw new PrincipalIsNotAuthenticatedException();
		return caller;
	};
}
