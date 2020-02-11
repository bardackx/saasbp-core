package com.saasbp.common.security;

import java.util.UUID;

public class Principal {

	private UUID id;

	public Principal() {
	}

	public Principal(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public boolean isAnonymous() {
		return this.id == null;
	}

}
