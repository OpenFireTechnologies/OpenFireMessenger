/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.auth;

public interface ServerAuthenticate {
	public String userSignUp(final String name, final String email,
			final String pass) throws Exception;

	public String userSignIn(final String user, final String email,
			final String pass) throws Exception;
}
