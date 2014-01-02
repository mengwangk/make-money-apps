package com.mymobkit.model;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public final class LoginUser {

	/**
	 * Create the key.
	 * 
	 * @param email
	 * @return
	 */
	public static Key<LoginUser> key(String email){
		return Key.create(LoginUser.class, email);
	}
	/**
	 * Use this method to normalize email addresses for lookup 
	 */
	public static String normalize(String email) {
		return email.toLowerCase();
	}
	
	@Id
	private String normalizedEmail;

	private String email;
	private String nickName;
	private Date created;
	private Date lastLogin;

	/**
	 * Only for use by Objectify.
	 */
	private LoginUser() {

	}

	public LoginUser(final String email) {
		this.email = email;
		this.normalizedEmail = normalize(email);
		this.created = new Date();
	}

	public void loggedIn() {
		this.lastLogin = new Date();
	}

	public Date getCreated() {
		return created;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public String getEmail() {
		return email;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	
	@Override
	public String toString() {
		return "LoginUser [normalizedEmail=" + normalizedEmail + ", email=" + email + ", nickName=" + nickName + ", created=" + created + ", lastLogin=" + lastLogin + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((normalizedEmail == null) ? 0 : normalizedEmail.hashCode());
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
		LoginUser other = (LoginUser) obj;
		if (normalizedEmail == null) {
			if (other.normalizedEmail != null)
				return false;
		} else if (!normalizedEmail.equals(other.normalizedEmail))
			return false;
		return true;
	}

	
}
