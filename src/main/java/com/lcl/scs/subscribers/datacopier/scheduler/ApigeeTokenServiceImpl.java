package com.lcl.scs.subscribers.datacopier.scheduler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.lcl.scs.subscribers.model.ApigeeToken;

public class ApigeeTokenServiceImpl {
	private String APIGEE_TOKEN_URL;
	private String APIGEE_TOKEN_CLIENT_ID;
	private String APIGEE_TOKEN_CLIENT_SECRET;
	private String APIGEE_TOKEN_GRANT_TYPE;
	private String APIGEE_TOKEN_SCOPE;
	
	public ApigeeTokenServiceImpl() {
		super();
	}

	public ApigeeTokenServiceImpl(String Apigee_TOKEN_URL, String Apigee_TOKEN_CLIENT_ID, String Apigee_TOKEN_CLIENT_SECRET,
			String Apigee_TOKEN_GRANT_TYPE, String Apigee_TOKEN_SCOPE) {
		super();
		
		APIGEE_TOKEN_URL = Apigee_TOKEN_URL;
		APIGEE_TOKEN_CLIENT_ID = Apigee_TOKEN_CLIENT_ID;
		APIGEE_TOKEN_CLIENT_SECRET = Apigee_TOKEN_CLIENT_SECRET;
		APIGEE_TOKEN_GRANT_TYPE = Apigee_TOKEN_GRANT_TYPE;
		APIGEE_TOKEN_SCOPE = Apigee_TOKEN_SCOPE;
	}



	public ApigeeToken getApigeeToken() throws Exception {
		ApigeeToken token = new ApigeeToken();

		try {
			HttpPost post = new HttpPost(APIGEE_TOKEN_URL);

			List<BasicNameValuePair> params = new ArrayList();
			params.add(new BasicNameValuePair("client_id", APIGEE_TOKEN_CLIENT_ID));
			params.add(new BasicNameValuePair("client_secret", APIGEE_TOKEN_CLIENT_SECRET));
			params.add(new BasicNameValuePair("grant_type", APIGEE_TOKEN_GRANT_TYPE));
			params.add(new BasicNameValuePair("scope", APIGEE_TOKEN_SCOPE));

			post.setEntity(new UrlEncodedFormEntity(params));

			HttpClient httpClient = HttpClientBuilder.create().build();

			HttpResponse response = httpClient.execute(post);
			int code = response.getStatusLine().getStatusCode();

			if (code == 200) {
				InputStream stream = response.getEntity().getContent();
				HttpEntity responseEntity = response.getEntity();
				String responseStr = EntityUtils.toString(responseEntity);
				JSONObject responseJSON = new JSONObject(responseStr);

				token.setToken_type(responseJSON.get("token_type").toString());
				token.setExpires_in(Integer.parseInt(responseJSON.get("expires_in").toString()));
				token.setAccess_token(responseJSON.getString("access_token"));

			} else {
				throw new Exception("Invalid Response from " + APIGEE_TOKEN_URL);
			}
		} catch (Exception ex) {
			throw ex;
		}

		return token;
	}

	public void setAPIGEE_TOKEN_URL(String Apigee_TOKEN_URL) {
		APIGEE_TOKEN_URL = Apigee_TOKEN_URL;
	}

	public void setAPIGEE_TOKEN_CLIENT_ID(String Apigee_TOKEN_CLIENT_ID) {
		APIGEE_TOKEN_CLIENT_ID = Apigee_TOKEN_CLIENT_ID;
	}

	public void setAPIGEE_TOKEN_CLIENT_SECRET(String Apigee_TOKEN_CLIENT_SECRET) {
		APIGEE_TOKEN_CLIENT_SECRET = Apigee_TOKEN_CLIENT_SECRET;
	}

	public void setAPIGEE_TOKEN_GRANT_TYPE(String Apigee_TOKEN_GRANT_TYPE) {
		APIGEE_TOKEN_GRANT_TYPE = Apigee_TOKEN_GRANT_TYPE;
	}

	public void setAPIGEE_TOKEN_SCOPE(String Apigee_TOKEN_SCOPE) {
		APIGEE_TOKEN_SCOPE = Apigee_TOKEN_SCOPE;
	}
}