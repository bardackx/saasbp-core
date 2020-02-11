package com.saasbp.auth.domain;

import java.util.Arrays;

/**
 * <a href= "https://www.baeldung.com/java-password-hashing">Source</a>
 * 
 * @author barda
 * 
 */
public class HashedPassword {

	private final byte[] hash;
	private final byte[] salt;

	public HashedPassword(byte[] hash, byte[] salt) {
		this.hash = hash;
		this.salt = salt;
	}

	public byte[] getHash() {
		return hash;
	}

	public byte[] getSalt() {
		return salt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(hash);
		result = prime * result + Arrays.hashCode(salt);
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
		HashedPassword other = (HashedPassword) obj;
		if (!Arrays.equals(hash, other.hash))
			return false;
		if (!Arrays.equals(salt, other.salt))
			return false;
		return true;
	}

}
