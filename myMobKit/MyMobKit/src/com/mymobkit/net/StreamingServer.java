package com.mymobkit.net;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.mymobkit.config.AppConfig;
import com.mymobkit.net.provider.Processor;

public final class StreamingServer extends MyMobKitServer {
	
	//private static final String MIME_JSON = "application/json";
	
	private static final String TAG = AppConfig.LOG_TAG_APP + ":StreamingServer";

	private Map<String, Processor<Map<String, String>, Map<String, String>, InputStream>> streamingServices = new HashMap<String, Processor<Map<String, String>, Map<String, String>, InputStream>>();

	private Map<String, Processor<Map<String, String>, Map<String, String>, String>> processorServices = new HashMap<String, Processor<Map<String, String>, Map<String, String>, String>>();

	public static final String START_PAGE = "live.html";
	
	public void registerStreaming(String uri, Processor<Map<String, String>, Map<String, String>, InputStream> service) {
		if (service != null && !TextUtils.isEmpty(uri))
			streamingServices.put(uri.toLowerCase(), service);
	}

	public void registerProcessor(String uri, Processor<Map<String, String>, Map<String, String>, String> service) {
		if (service != null)
			processorServices.put(uri, service);
	}

	public StreamingServer(String host, int port, File wwwroot, boolean quiet) {
		super(host, port, wwwroot, quiet);
		this.setCustomStartPage(START_PAGE);
	}

	public StreamingServer(String host, int port, AssetManager wwwroot, boolean quiet) {
		super(host, port, wwwroot, quiet);
		this.setCustomStartPage(START_PAGE);
	}

	@Override
	public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
		Log.d(TAG, "[server] HTTP request >>" + method + " '" + uri + "' " + "   " + parms);

		if (uri.startsWith("/processor/")) {
			return serveService(uri, method, headers, parms, files);
		} else if (uri.startsWith("/audio_stream/")) {
			return serveStream(uri, method, headers, parms, files, true);
		} else if (uri.startsWith("/video_stream/")) {
			return serveStream(uri, method, headers, parms, files, false);
		} else if (uri.startsWith("/video/")) {
			return serveStream(uri, method, headers, parms, files, true);
		} else {
			return super.serve(uri, method, headers, parms, files);
		}
	}

	public Response serveStream(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files, 
			boolean isStreaming) {
		Processor<Map<String, String>, Map<String, String>, InputStream> processor = streamingServices.get(uri.toLowerCase());
		if (processor == null)
			return null;

		InputStream ins;
		ins = processor.process(header, parms);
		if (ins == null)
			return null;

		Random rnd = new Random();
		String etag = Integer.toHexString(rnd.nextInt());
		String mime = parms.get("mime");
		//if (mime == null)
		//	mime = "application/octet-stream";
		Response res = new Response(Response.Status.OK, mime, ins);
		res.addHeader("ETag", etag);
		res.isStreaming = isStreaming;
		return res;
	}

	public Response serveService(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
		Processor<Map<String, String>, Map<String, String>, String> processor = processorServices.get(uri);
		if (processor == null)
			return null;

		String msg = processor.process(header, parms);
		if (msg == null)
			return null;

		Response res = new Response(Response.Status.OK, MIME_PLAINTEXT, msg);
		return res;
	}
}
