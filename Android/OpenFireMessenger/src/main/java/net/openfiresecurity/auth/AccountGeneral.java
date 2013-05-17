/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.auth;

class AccountGeneral {

	/**
	 * Account type id
	 */
	public static final String ACCOUNT_TYPE = "net.openfiresecurity.messenger";

	/**
	 * Account name
	 */
	public static final String ACCOUNT_NAME = "OpenFireMessenger";

	/**
	 * Auth token types
	 */
	public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
	public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an OpenFireMessenger account";

	public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
	public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an OpenFireMessenger account";

	public static final ServerAuthenticate sServerAuthenticate = new ParseServerAuthenticate();
}
