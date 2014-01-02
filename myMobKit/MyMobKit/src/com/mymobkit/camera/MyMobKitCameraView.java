package com.mymobkit.camera;

import java.io.FileOutputStream;
import java.util.List;

import org.opencv.android.JavaCameraView;

import com.mymobkit.config.AppConfig;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;

public class MyMobKitCameraView extends JavaCameraView implements PictureCallback {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":MyMobKitCameraView";

	private String pictureFileName;

	public MyMobKitCameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public List<String> getEffectList() {
		return mCamera.getParameters().getSupportedColorEffects();
	}

	public boolean isEffectSupported() {
		return (mCamera.getParameters().getColorEffect() != null);
	}

	public String getEffect() {
		return mCamera.getParameters().getColorEffect();
	}

	public void setEffect(String effect) {
		Camera.Parameters params = mCamera.getParameters();
		params.setColorEffect(effect);
		mCamera.setParameters(params);
	}

	public List<Size> getResolutionList() {
		return mCamera.getParameters().getSupportedPreviewSizes();
	}

	public void setCamera(int type) {
		disconnectCamera();
		setCameraIndex(type);
		connectCamera(getWidth(), getHeight());
	}

	public void setResolution(Size resolution) {
		disconnectCamera();
		mMaxHeight = resolution.height;
		mMaxWidth = resolution.width;
		connectCamera(getWidth(), getHeight());
	}

	public Parameters getParameters() {
		return mCamera.getParameters();
	}

	public void setParameters(Parameters params) {
		mCamera.setParameters(params);
	}

	public void setPreviewCallback(PreviewCallback cb) {
		mCamera.setPreviewCallback(cb);
	}

	public Size getResolution() {
		return mCamera.getParameters().getPreviewSize();
	}

	public void takePicture(final String fileName) {
		Log.i(TAG, "Taking picture");

		this.pictureFileName = fileName;

		// Post view and JPG are sent in the same buffers if the queue is not
		// empty when performing a capture.
		// Clear up buffers to avoid mCamera.takePicture to be stuck because of
		// a memory issue
		mCamera.setPreviewCallback(null);

		// PictureCallback is implemented by the current class
		mCamera.takePicture(null, null, this);
	}

	public void autoFocus(AutoFocusCallback cb) {
		mCamera.autoFocus(cb);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.i(TAG, "Saving a bitmap to file");

		// The camera preview was automatically stopped. Start it again.
		mCamera.startPreview();
		mCamera.setPreviewCallback(this);

		// Write the image in a file (in jpeg format)
		try {
			FileOutputStream fos = new FileOutputStream(pictureFileName);
			fos.write(data);
			fos.close();
		} catch (java.io.IOException e) {
			Log.e(TAG, "[onPictureTaken] Exception in photoCallback", e);
		}

	}
}