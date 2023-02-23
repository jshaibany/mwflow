package com.example.one_mw.service.integrated;

public class MWServiceBulkRequestCheckerAuthorizationConnector implements ICredentialsConnector{

	private String url;
	private String userName;
	private String password;

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
