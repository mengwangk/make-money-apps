package com.mymobkit.net;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.mymobkit.config.AppConfig;
import com.mymobkit.net.provider.Processor;

public final class ControlPanelService extends MyMobKitServer {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":ControlPanelService";

	private Map<String, Processor<Map<String, String>, Map<String, String>, String>> processorServices = new HashMap<String, Processor<Map<String, String>, Map<String, String>, String>>();

	//public static final String START_PAGE = "control_panel.html";

	public void registerService(String uri, Processor<Map<String, String>, Map<String, String>, String> service) {
		if (service != null && !TextUtils.isEmpty(uri))
			processorServices.put(uri.toLowerCase(), service);
	}

	public ControlPanelService(String host, int port, File wwwroot, boolean quiet) {
		super(host, port, wwwroot, quiet);
		//this.setCustomStartPage(START_PAGE);
	}

	public ControlPanelService(String host, int port, AssetManager wwwroot, boolean quiet) {
		super(host, port, wwwroot, quiet);
		//this.setCustomStartPage(START_PAGE);
	}

	@Override
	public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
		Log.d(TAG, "[server] HTTP request >>" + method + " '" + uri + "' " + "   " + parms);

		if (uri.startsWith("/services/")) {
			return serveService(uri, method, headers, parms, files);
		} else {
			return super.serve(uri, method, headers, parms, files);
		}
	}

	public Response serveService(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
		Processor<Map<String, String>, Map<String, String>, String> processor = processorServices.get(uri.toLowerCase());
		if (processor == null)
			return null;

		String msg = processor.process(header, parms);
		if (msg == null)
			return null;

		Response res = new Response(Response.Status.OK, MIME_PLAINTEXT, msg);
		return res;
	}
}
