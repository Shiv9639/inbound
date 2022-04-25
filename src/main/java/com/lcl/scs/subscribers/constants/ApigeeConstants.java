package com.lcl.scs.subscribers.constants;

public class ApigeeConstants {

	private ApigeeConstants() {
		super();
	}

	//token details from environment variables
	private static final String APIGEE_TOKEN_URL_VAIRABLE = System.getenv("APIGEE_TOKEN_URL");
	private static final String APIGEE_TOKEN_CLIENT_ID_VAIRABLE = System.getenv("APIGEE_TOKEN_CLIENT_ID");
	private static final String APIGEE_TOKEN_CLIENT_SECRET_VAIRABLE = System.getenv("APIGEE_TOKEN_CLIENT_SECRET");
	private static final String APIGEE_TOKEN_GRANT_TYPE_VAIRABLE = System.getenv("APIGEE_TOKEN_GRANT_TYPE");
	private static final String APIGEE_TOKEN_SCOPE_VAIRABLE = System.getenv("APIGEE_TOKEN_SCOPE");
	
	
	//corresponding enum for token...
	public enum APIGEE_TOKEN {
		
		URL(APIGEE_TOKEN_URL_VAIRABLE),
		CLIENT_ID(APIGEE_TOKEN_CLIENT_ID_VAIRABLE),
		CLIENT_SECRET(APIGEE_TOKEN_CLIENT_SECRET_VAIRABLE),
		GRANT_TYPE(APIGEE_TOKEN_GRANT_TYPE_VAIRABLE),
		SCOPE(APIGEE_TOKEN_SCOPE_VAIRABLE)
		;

		private APIGEE_TOKEN(String value) {
			this.value = value;
		}

		private String value;

		public String getValue() {
			return value;
		}

	}
}