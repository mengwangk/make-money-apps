package com.mymobkit.video;

import java.util.List;

import android.hardware.Camera;
import android.util.Log;

import com.mymobkit.camera.MyMobKitCameraView;
import com.mymobkit.config.AppConfig;

public final class CameraView {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":CameraView";
	private MyMobKitCameraView openCVCameraView;
	private List<Camera.Size> supportedSizes;
	private Camera.Size procSize;

	public CameraView(MyMobKitCameraView sv) {
		openCVCameraView = sv;
		Camera.Parameters p = openCVCameraView.getParameters();
		supportedSizes = p.getSupportedPreviewSizes();
		// procSize = supportedSizes.get(supportedSizes.size() / 2);
		procSize = openCVCameraView.getResolution();
	}

	public List<Camera.Size> getSupportedPreviewSize() {
		return supportedSizes;
	}

	public int getWidth() {
		return procSize.width;
	}

	public int getHeight() {
		return procSize.height;
	}

	public void setupCamera(int width, int height) { // PreviewCallback cb) {
		Log.d(TAG, "[setupCamera] Setting up camera with " + width + "x" + height);
		procSize.width = width;
		procSize.height = height;
		openCVCameraView.setResolution(procSize);
		// openCVCameraView.setPreviewCallback(cb);
	}
}
