package com.saasbp.auth.domain;

import java.util.UUID;

public class EmailConfirmation {

	private final UUID code;
	private boolean confirmed;

	public EmailConfirmation(UUID code, boolean confirmed) {
		this.code = code;
		this.confirmed = confirmed;
	}

	public EmailConfirmation() {
		this(UUID.randomUUID(), false);
	}

	public UUID getCode() {
		return code;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + (confirmed ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailConfirmation other = (EmailConfirmation) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (confirmed != other.confirmed)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmailConfirmation [code=" + code + ", confirmed=" + confirmed + "]";
	}

}
