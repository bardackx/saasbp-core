package com.saasbp.auth.domain;

import java.util.UUID;

public class User {

	private UUID uuid;
	private String email;
	private HashedPassword password;
	private EmailConfirmation emailConfirmation;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public HashedPassword getPassword() {
		return password;
	}

	public void setPassword(HashedPassword password) {
		this.password = password;
	}

	public EmailConfirmation getEmailConfirmation() {
		return emailConfirmation;
	}

	public void setEmailConfirmation(EmailConfirmation emailConfirmation) {
		this.emailConfirmation = emailConfirmation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((emailConfirmation == null) ? 0 : emailConfirmation.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (emailConfirmation == null) {
			if (other.emailConfirmation != null)
				return false;
		} else if (!emailConfirmation.equals(other.emailConfirmation))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
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
		return "User [uuid=" + uuid + ", email=" + email + ", password=" + password + ", emailConfirmation="
				+ emailConfirmation + "]";
	}

}
