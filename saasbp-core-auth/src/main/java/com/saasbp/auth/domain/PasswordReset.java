package com.saasbp.auth.domain;

import java.time.Instant;
import java.util.UUID;

public class PasswordReset {

	private UUID uuid;
	private String email;
	private Instant timestamp;
	private boolean fulfilled;

	public PasswordReset(String email) {
		this(UUID.randomUUID(), email, Instant.now(), false);
	}

	public PasswordReset(UUID uuid, String email, Instant timestamp, boolean fulfilled) {
		super();
		this.uuid = uuid;
		this.email = email;
		this.timestamp = timestamp;
		this.fulfilled = fulfilled;
	}

	public void fulfill() {
		if (isExpired())
			throw new ExpiredPasswordReset();
		fulfilled = true;
	}

	private boolean isExpired() {
		return Instant.now().isAfter(getExpirationDate());
	}

	private Instant getExpirationDate() {
		return timestamp.plusSeconds(60 * 5);
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getEmail() {
		return email;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public boolean isFulfilled() {
		return fulfilled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (fulfilled ? 1231 : 1237);
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		PasswordReset other = (PasswordReset) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fulfilled != other.fulfilled)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PasswordReset [uuid=" + uuid + ", email=" + email + ", timestamp=" + timestamp + ", fulfilled="
				+ fulfilled + "]";
	}

}
