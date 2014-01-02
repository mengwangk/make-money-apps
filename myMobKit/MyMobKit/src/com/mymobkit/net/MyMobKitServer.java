package com.mymobkit.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.mymobkit.config.AppConfig;

/**
 * myMobKit server implementation.
 * 
 */
public class MyMobKitServer extends HttpServer {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":MyMobKitServer";

	protected AssetManager assetManager;

	protected String customStartPage;

	/**
	 * @param host
	 * @param port
	 * @param quiet
	 */
	public MyMobKitServer(String host, int port, AssetManager wwwroot, boolean quiet) {
		super(host, port, quiet);
		this.assetManager = wwwroot;
		this.customStartPage = "";
	}

	/**
	 * @param host
	 * @param port
	 * @param wwwroot
	 * @param quiet
	 */
	public MyMobKitServer(String host, int port, File wwwroot, boolean quiet) {
		super(host, port, wwwroot, quiet);
		this.assetManager = null;
		this.customStartPage = "";
	}

	@Override
	public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
		return super.serve(uri, method, header, parms, files);
	}

	@Override
	protected Response serve(HTTPSession session) {
		return super.serve(session);
	}

	public void setCustomStartPage(String pageName) {
		this.customStartPage = pageName;
	}
	
	public String getCustomStartPage(){
		return this.customStartPage;
	}

	@Override
	protected Response serveAssets(String uri, Map<String, String> header) {
		Response res = null;

		// Remove URL arguments
		uri = uri.trim().replace(File.separatorChar, '/');
		if (uri.startsWith("/")) {
			uri = uri.substring(1, uri.length());
		}
		if (uri.indexOf('?') >= 0)
			uri = uri.substring(0, uri.indexOf('?'));

		// Prohibit getting out of current directory
		if (uri.startsWith("..") || uri.endsWith("..") || uri.indexOf("../") >= 0) {
			res = new Response(Response.Status.FORBIDDEN, MIME_PLAINTEXT,
					"FORBIDDEN: Won't serve ../ for security reasons.");
		}

		if (uri.endsWith("/") || uri.equalsIgnoreCase("")) {
			if (!TextUtils.isEmpty(getCustomStartPage())) {
				uri = uri + getCustomStartPage();
			} else {
				uri = uri + "index.html";
			}
		}

		InputStream assetFile = null;
		try {
			assetFile = assetManager.open(uri);
		} catch (IOException ex) {
			assetFile = null;
		}
		if (res == null && assetFile == null) {
			res = new Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
					"Error 404, file not found.");
		}

		try {
			if (res == null) {
				// Get MIME type from file name extension, if possible
				String mime = null;
				int dot = uri.lastIndexOf('.');
				if (dot >= 0)
					mime = (String) MIME_TYPES.get(uri.substring(dot + 1).toLowerCase());
				if (mime == null)
					mime = MIME_DEFAULT_BINARY;

				// Calculate etag
				String etag = Integer.toHexString((uri + "" + assetFile.available()).hashCode());

				// Support (simple) skipping:
				long startFrom = 0;
				long endAt = -1;
				String range = header.get("range");
				if (range != null)
				{
					if (range.startsWith("bytes="))
					{
						range = range.substring("bytes=".length());
						int minus = range.indexOf('-');
						try {
							if (minus > 0)
							{
								startFrom = Long.parseLong(range.substring(0, minus));
								endAt = Long.parseLong(range.substring(minus + 1));
							}
						} catch (NumberFormatException nfe) {
						}
					}
				}

				// Change return code and add Content-Range header when skipping
				// is requested
				long fileLen = assetFile.available();
				if (range != null && startFrom >= 0)
				{
					if (startFrom >= fileLen)
					{
						res = new Response(Response.Status.RANGE_NOT_SATISFIABLE, MIME_PLAINTEXT, "");
						res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
						res.addHeader("ETag", etag);
					}
					else
					{
						if (endAt < 0)
							endAt = fileLen - 1;
						long newLen = endAt - startFrom + 1;
						if (newLen < 0)
							newLen = 0;

						final long dataLen = newLen;
						assetFile.skip(startFrom);

						res = new Response(Response.Status.PARTIAL_CONTENT, mime, assetFile);
						res.addHeader("Content-Length", "" + dataLen);
						res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
						res.addHeader("ETag", etag);
					}
				}
				else
				{
					res = new Response(Response.Status.OK, mime, assetFile);
					res.addHeader("Content-Length", "" + fileLen);
					res.addHeader("ETag", etag);
				}

			}
		} catch (IOException ioe)
		{
			res = new Response(Response.Status.FORBIDDEN, MIME_PLAINTEXT, "FORBIDDEN: Reading file failed.");
		}
		res.addHeader("Accept-Ranges", "bytes"); // Announce that the file
													// server accepts partial
													// content requestes
		return res;
	}
}
