package com.rishabh.eventplanner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Enumeration;

import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

public class GetCalendarService {

	/** Email of the Service Account */
	private static final String SERVICE_ACCOUNT_EMAIL = "770954577993-6nuv4i0bjn2o40bft654v2fpc0sf340i@developer.gserviceaccount.com";

	/**
	 * Build and returns a Drive service object authorized with the service
	 * accounts that act on behalf of the given user.
	 * 
	 * @param userEmail
	 *            The email of the user.
	 * @return Drive service object that is ready to make requests.
	 */
	public Calendar getDriveService() throws GeneralSecurityException,
			IOException, URISyntaxException {
		String p12Password = "notasecret";
		PrivateKey key = null;
		try {
			KeyStore keystore = KeyStore.getInstance("PKCS12");
			Log.v("service", keystore.toString());
			keystore.load(MainActivity.is, p12Password.toCharArray());

			Enumeration<String> alias = keystore.aliases();
			Log.v("service", alias.toString());
			key = (PrivateKey) keystore.getKey(alias.nextElement(),
					p12Password.toCharArray());
			// Log.v("key",key.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();

		Log.v("a", "passed");
		GoogleCredential credential;
		Log.v("Key not null", "Hurrah!!");
		credential = new GoogleCredential.Builder()
				.setTransport(httpTransport)
				.setJsonFactory(jsonFactory)
				.setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
				.setServiceAccountScopes(Arrays.asList(CalendarScopes.CALENDAR))
				.setServiceAccountPrivateKey(key).build();
		Log.v("B", "passed b");

		Calendar service = new Calendar.Builder(httpTransport, jsonFactory,
				null).setHttpRequestInitializer(credential).build();

		return service;
	}

}
