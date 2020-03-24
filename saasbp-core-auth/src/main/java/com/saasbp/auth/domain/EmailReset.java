package com.saasbp.auth.domain;

import java.time.Instant;
import java.util.UUID;

public class EmailReset {

	private final UUID uuid;
	private final UUID user;
	private final String newEmail;
	private final Instant timestamp;
	private boolean fulfilled;

	public EmailReset(UUID user, String newEmail) {
		this(UUID.randomUUID(), user, newEmail, Instant.now(), false);
	}

	public EmailReset(UUID uuid, UUID user, String newEmail, Instant timestamp, boolean fulfilled) {
		super();
		this.uuid = uuid;
		this.user = user;
		this.newEmail = newEmail;
		this.timestamp = timestamp;
		this.fulfilled = fulfilled;
	}

	public void fulfill() {
		if (isExpired())
			throw new ExpiredEmailResetException();
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

	public String getNewEmail() {
		return newEmail;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public boolean isFulfilled() {
		return fulfilled;
	}

	public UUID getUser() {
		return user;
	}

}
