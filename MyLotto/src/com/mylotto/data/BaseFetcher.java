package com.mylotto.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;

import com.mylotto.data.FetchResults.FetchStatus;
import com.mylotto.helper.StringUtils;
import com.mylotto.helper.Utils;

/**
 * Base class for all fetchers.
 * 
 * @author MEKOH
 * 
 */
public abstract class BaseFetcher {

	protected static final String CLASS_TAG = BaseFetcher.class.getSimpleName();

	protected Context context;
	protected Prefs prefs;
	protected String name;
	protected FetchResults results;
	protected static Date lastSuccessfulDrawDate;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param context
	 * @param prefs
	 */
	public BaseFetcher(final String name, final Context context, final Prefs prefs) {
		this.context = context;
		this.prefs = prefs;
		this.name = name;
		this.results = new FetchResults();
	}

	/**
	 * @param handler
	 * @param url
	 * @param headers
	 * @return
	 */
	protected FetchResults performPost(final String url, final Map<String, String> params) {
		FetchResults results = new FetchResults();
		if (!Utils.isOnline(context)) {
			results.status = FetchStatus.CONNECTION_NOT_AVAILABLE;
			return results;
		}
		BufferedReader in = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost method = new HttpPost(url);

			// data - name/value params
			List<NameValuePair> nvps = null;
			if ((params != null) && (params.size() > 0)) {
				nvps = new ArrayList<NameValuePair>();
				for (String key : params.keySet()) {
					Log.d(CLASS_TAG, "adding param: " + key + " | " + params.get(key));
					nvps.add(new BasicNameValuePair(key, params.get(key)));
				}
			}
			if (nvps != null) {
				try {
					method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				} catch (UnsupportedEncodingException e) {
					Log.e(CLASS_TAG, e.getMessage());
				}
			}
			HttpResponse response = client.execute(method);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer(StringUtils.EMPTY);
			String line = StringUtils.EMPTY;
			String lineSeparator = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + lineSeparator);
			}
			in.close();
			String page = sb.toString();

			// Remove all HTML tags
			page = page.replaceAll("<(.|\n)*?>", StringUtils.EMPTY);

			// Remove empty lines
			page = page.replaceAll("(?m)^[ \t]*\r?\n", StringUtils.EMPTY);
			results.content = page;
			results.status = FetchStatus.SUCCESSFUL;
		} catch (IOException ioEx) {
			results.status = FetchStatus.UNABLE_TO_READ;
			results.errorMsg = ioEx.getMessage();
			Log.e(CLASS_TAG, ioEx.getMessage());
		} catch (Exception ex) {
			results.status = FetchStatus.UNKNOWN_ERROR;
			results.errorMsg = ex.getMessage();
			Log.e(CLASS_TAG, ex.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	/**
	 * Grab the web page information
	 * 
	 * @param handler
	 * @param url
	 * @return
	 */
	protected FetchResults performGet(final String url) {
		FetchResults results = new FetchResults();
		if (!Utils.isOnline(context)) {
			results.status = FetchStatus.CONNECTION_NOT_AVAILABLE;
			return results;
		}
		BufferedReader in = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer(StringUtils.EMPTY);
			String line = StringUtils.EMPTY;
			String lineSeparator = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + lineSeparator);
			}
			in.close();
			String page = sb.toString();

			// Remove all HTML tags
			page = page.replaceAll("<(.|\n)*?>", StringUtils.EMPTY);

			// Remove empty lines
			page = page.replaceAll("(?m)^[ \t]*\r?\n", StringUtils.EMPTY);
			results.content = page;
			results.status = FetchStatus.SUCCESSFUL;
		} catch (URISyntaxException uriEx) {
			results.status = FetchStatus.INVALID_URI;
			results.errorMsg = uriEx.getMessage();
			Log.e(CLASS_TAG, uriEx.getMessage());
		} catch (IOException ioEx) {
			results.status = FetchStatus.UNABLE_TO_READ;
			results.errorMsg = ioEx.getMessage();
			Log.e(CLASS_TAG, ioEx.getMessage());
		} catch (Exception ex) {
			results.status = FetchStatus.UNKNOWN_ERROR;
			results.errorMsg = ex.getMessage();
			Log.e(CLASS_TAG, ex.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	/**
	 * Return the fetch results
	 * 
	 * @return Fetch results
	 */
	public FetchResults getFetchResults() {
		return this.results;
	}

	/**
	 * Return the unique name
	 * 
	 * @return Fetcher name
	 */
	public String getName() {
		return this.name;
	}

}
