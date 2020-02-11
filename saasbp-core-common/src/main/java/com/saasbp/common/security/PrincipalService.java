package com.saasbp.common.security;

public interface PrincipalService {

	Principal getCaller();

	/**
	 * 
	 * @return
	 * @throws PrincipalIsNotAuthenticated
	 */
	default Principal getAuthenticatedCaller() {
		Principal caller = getCaller();
		if (caller.isAnonymous())
			throw new PrincipalIsNotAuthenticated();
		return caller;
	};
}
