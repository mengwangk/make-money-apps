package com.mymobkit.ui.activity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mymobkit.R;
import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.audio.codec.Mp3Encoder;
import com.mymobkit.camera.MyMobKitCameraView;
import com.mymobkit.common.NetworkHelper;
import com.mymobkit.common.ToastUtils;
import com.mymobkit.config.AppConfig;
import com.mymobkit.config.AppConfig.MenuListSetup;
import com.mymobkit.enums.MotionDetectionAlgorithm;
import com.mymobkit.model.ActionStatus;
import com.mymobkit.model.Resolution;
import com.mymobkit.motion.detection.FaceDetector;
import com.mymobkit.motion.detection.IDetector;
import com.mymobkit.motion.detection.data.GlobalData;
import com.mymobkit.net.NanoHttpd;
import com.mymobkit.net.StreamingServer;
import com.mymobkit.net.provider.Processor;
import com.mymobkit.ui.adapter.CameraMenuItem;
import com.mymobkit.ui.base.SensorsActivity;
import com.mymobkit.ui.fragment.CameraSettingsFragment;
import com.mymobkit.video.CameraView;
import com.mymobkit.video.DataStream;
import com.mymobkit.video.OverlayView;

public final class VideoStreamingActivity extends SensorsActivity implements View.OnTouchListener, CvCameraViewListener2 {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":VideoStreamingActivity";

	// OpenCV initializer
	static {
		if (!OpenCVLoader.initDebug()) {
			// Handle initialization error
			Log.w(TAG, "[Initializer] Unable to initialize OpenCV");
		}
	}

	private Mat cameraRgbaFrame = null;
	private Mat streamedRgbaFrame = null;
	// private Mat cameraGrayFrame = null;
	private StreamingServer streamingServer = null;
	private CameraView cameraView = null;
	private OverlayView overlayView;
	// private Button exitButton;
	private TextView tvMessage1;
	// private TextView tvMessage2;
	private MyMobKitCameraView openCVCameraView;

	private int streamingPort;
	private SharedPreferences sharedPreferences;

	private static final Object streamingLock = new Object();

	private IDetector motionDetector;
	private IDetector faceDetector;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private String[] cameraMenu;
	private int chosenResolution;
	private boolean faceDetectedRequired;
	private float faceDetectionSize;
	private boolean motionDetectionRequired;
	private int motionDetectionThreshold;
	private int imageQuality;
	private String motionDetectionAlgorithm;
	private int motionDetectionContourThickness;
	private boolean streamDetectedObject;

	private static final int AUDIO_FREQUENCY = 44100;
	private static final int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
	private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private static int bufferSize = AudioRecord.getMinBufferSize(AUDIO_FREQUENCY, AUDIO_CHANNEL, AUDIO_ENCODING);
	private volatile boolean isStreamingAudio;

	private Mp3Encoder mp3Encoder;
	private AudioRecord audioRecorder = null;
	private List<DataStream> audioStreams = null;
	private List<DataStream> newAudioStreams = null;
	private AudioEncoder audioEncoder = null;

	private List<DataStream> videoStreams = null;
	private List<DataStream> newVideoStreams = null;
	private volatile boolean isStreamingVideo;
	private VideoEncoder videoEncoder = null;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_video_streaming);

		cameraMenu = getResources().getStringArray(R.array.camera_video_menu);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		CameraMenuAdapter adapter = new CameraMenuAdapter(this);
		for (String menuItem : cameraMenu) {
			adapter.add(new CameraMenuItem(menuItem));
		}
		drawerList.setAdapter(adapter);
		drawerList.setOnItemClickListener(new DrawerItemClickListener());
		drawerToggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close
				) {
					public void onDrawerClosed(View view) {
						invalidateOptionsMenu();
					}

					public void onDrawerOpened(View drawerView) {
						invalidateOptionsMenu();
					}
				};
		drawerLayout.setDrawerListener(drawerToggle);
		/*
		 * if (savedInstanceState == null) { selectItem(0, null); }
		 */

		// exitButton = (Button) findViewById(R.id.btn_exit);
		// exitButton.setOnClickListener(exitAction);
		tvMessage1 = (TextView) findViewById(R.id.tv_message1);
		// tvMessage2 = (TextView) findViewById(R.id.tv_message2);

		sharedPreferences = getSharedPreferences(CameraSettingsFragment.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		streamingPort = Integer.valueOf(sharedPreferences.getString(CameraSettingsFragment.KEY_VIDEO_STREAMING_PORT, getString(R.string.default_video_streaming_port)));
		faceDetectedRequired = sharedPreferences.getBoolean(CameraSettingsFragment.KEY_FACE_DETECTION, Boolean.valueOf(getString(R.string.default_face_detection)));
		motionDetectionRequired = sharedPreferences.getBoolean(CameraSettingsFragment.KEY_MOTION_DETECTION, Boolean.valueOf(getString(R.string.default_motion_detection)));
		imageQuality = sharedPreferences.getInt(CameraSettingsFragment.KEY_VIDEO_STREAMING_IMAGE_QUALITY, Integer.valueOf(getString(R.string.default_video_streaming_image_quality)));
		motionDetectionThreshold = sharedPreferences.getInt(CameraSettingsFragment.KEY_MOTION_DETECTION_THRESHOLD, Integer.valueOf(getString(R.string.default_motion_detection_threshold)));
		faceDetectionSize = Float.valueOf(sharedPreferences.getString(CameraSettingsFragment.KEY_FACE_DETECTION_SIZE, getString(R.string.default_face_detection_size)));
		motionDetectionAlgorithm = sharedPreferences.getString(CameraSettingsFragment.KEY_MOTION_DETECTION_ALGORITHM, getString(R.string.default_motion_detection_algorithm));
		motionDetectionContourThickness = sharedPreferences.getInt(CameraSettingsFragment.KEY_MOTION_DETECTION_CONTOUR_THICKNESS, Integer.valueOf(getString(R.string.default_motion_detection_contour_thickness)));
		streamDetectedObject = sharedPreferences.getBoolean(CameraSettingsFragment.KEY_STREAM_DETECTED_OBJECT, Boolean.valueOf(getString(R.string.default_stream_detected_object)));

		motionDetector = MotionDetectionAlgorithm.getDetector(motionDetectionAlgorithm, motionDetectionThreshold);
		motionDetector.setContourThickness(motionDetectionContourThickness);
		faceDetector = new FaceDetector(faceDetectionSize);

		mp3Encoder = new Mp3Encoder();
		System.loadLibrary("natpmp");
		initVideo();
		initAudio();
		initCamera();
	}

	
	private void release(){
		
		isStreamingAudio = false;

		if (streamingServer != null)
			streamingServer.stop();

		for (DataStream audioStream : audioStreams) {
			audioStream.release();
		}
		audioRecorder.release();

		isStreamingVideo = false;
		for (DataStream videoStream : videoStreams) {
			videoStream.release();
		}

		if (openCVCameraView != null)
			openCVCameraView.disableView();
		
		MyMobKitApp.setSurveillanceMode(false);

		finish();
	}

	
	@Override
	public void onPause() {
		super.onPause();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		MyMobKitApp.setSurveillanceMode(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		release();
		/*if (openCVCameraView != null)
			openCVCameraView.disableView();*/
	}

	@Override
	public boolean onTouch(View v, MotionEvent evt) {
		return false;
	}

	private void initVideo() {
		isStreamingVideo = false;
		if (videoStreams == null)
			videoStreams = new ArrayList<DataStream>(1);
		if (newVideoStreams == null)
			newVideoStreams = new ArrayList<DataStream>(1);
	}

	private void initAudio() {

		isStreamingAudio = false;

		int minTargetSize = 4410 * 2; // 0.1 seconds buffer size
		if (minTargetSize < bufferSize) {
			minTargetSize = bufferSize;
		}
		if (audioRecorder == null) {
			audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, AUDIO_FREQUENCY, AUDIO_CHANNEL, AUDIO_ENCODING, minTargetSize);
		}
		if (audioStreams == null)
			audioStreams = new ArrayList<DataStream>(1);

		if (newAudioStreams == null)
			newAudioStreams = new ArrayList<DataStream>(1);

	}

	private void initCamera() {
		openCVCameraView = (MyMobKitCameraView) findViewById(R.id.surface_camera);
		openCVCameraView.setVisibility(SurfaceView.VISIBLE);
		openCVCameraView.setCvCameraViewListener(this);
		openCVCameraView.enableView();
	}

	private boolean setupStreamingServer() {
		if (streamingServer != null && streamingServer.isAlive()) return true;
		String ipAddr = NetworkHelper.getLocalIPAddress();
		if (ipAddr != null) {
			try {
				streamingServer = new StreamingServer(null, streamingPort, this.getAssets(), false);
				streamingServer.registerProcessor("/processor/query", queryProcessor);
				streamingServer.registerProcessor("/processor/setup", setupProcessor);
				streamingServer.registerStreaming("/video_stream/live.jpg", captureProcessor);
				streamingServer.registerStreaming("/video/live.mjpg", videoProcessor);
				streamingServer.registerStreaming("/audio_stream/live.mp3", broadcastProcessor);
				streamingServer.start();
			} catch (IOException e) {
				streamingServer = null;
			}
		}
		if (streamingServer != null) {
			tvMessage1.setText("http://" + ipAddr + ":" + streamingPort);
			NatPMPClient natQuery = new NatPMPClient();
			natQuery.start();
			return true;
		} else {
			ToastUtils.r(this, getString(R.string.msg_error), Toast.LENGTH_SHORT);
			tvMessage1.setText(getString(R.string.msg_error));
			// tvMessage2.setVisibility(View.GONE);
			return false;
		}

	}

	// private OnClickListener exitAction = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// onPause();
	// }
	// };

	private Processor<Map<String, String>, Map<String, String>, String> queryProcessor = new Processor<Map<String, String>, Map<String, String>, String>() {

		@Override
		public String process(Map<String, String> headers, Map<String, String> params) {
			List<Resolution> resList = new ArrayList<Resolution>(5);
			List<Camera.Size> supportSize = cameraView.getSupportedPreviewSize();
			resList.add(new Resolution(cameraView.getWidth(), cameraView.getHeight()));
			for (int i = 0; i < supportSize.size(); i++) {
				resList.add(new Resolution(supportSize.get(i).width, supportSize.get(i).height));
			}
			return new Gson().toJson(resList);
		}
	};

	private Processor<Map<String, String>, Map<String, String>, String> setupProcessor = new Processor<Map<String, String>, Map<String, String>, String>() {

		@Override
		public String process(Map<String, String> headers, Map<String, String> params) {
			int width = Integer.parseInt(params.get("width"));
			int height = Integer.parseInt(params.get("height"));
			cameraView.setupCamera(width, height);
			return ActionStatus.OK;
		}
	};

	private Processor<Map<String, String>, Map<String, String>, InputStream> broadcastProcessor = new Processor<Map<String, String>, Map<String, String>, InputStream>() {

		@Override
		public InputStream process(Map<String, String> headers, Map<String, String> params) {

			Random rnd = new Random();
			String etag = Integer.toHexString(rnd.nextInt());
			DataStream audioStream = new DataStream("com.mymobkit" + etag);

			audioStream.prepare(128, 8192);
			InputStream is = null;
			try {
				is = audioStream.getInputStream();
			} catch (IOException e) {
				audioStream.release();
				return null;
			}

			newAudioStreams.add(audioStream);

			int state = audioRecorder.getState();
			if (state != AudioRecord.RECORDSTATE_RECORDING) {
				audioRecorder.startRecording();
			}

			if (!isStreamingAudio) {
				audioEncoder = new AudioEncoder();
				audioEncoder.start();
			}

			params.put("mime", "application/octet-stream");
			return is;
		}
	};

	private byte[] shortToByte(short[] sData, int size) {
		int shortArrsize = size;
		byte[] bytes = new byte[shortArrsize * 2];
		for (int i = 0; i < shortArrsize; i++) {
			bytes[i * 2] = (byte) (sData[i] & 0x00FF);
			bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
			sData[i] = 0;
		}
		return bytes;
	}

	private class AudioEncoder extends Thread {

		@Override
		public void run() {

			if (isStreamingAudio)
				return;
			isStreamingAudio = true;

			byte[] audioData = new byte[1024 * 16];
			byte[] mp3Data = new byte[1024 * 8];
			int readSize = 4410 * 2;

			mp3Encoder.nativeOpenEncoder();

			List<DataStream> invalidAudioStreams = new ArrayList<DataStream>(1);
			while (true) {
				if (!isStreamingAudio)
					break;

				int len = audioRecorder.read(audioData, 0, readSize);
				if (len == AudioRecord.ERROR_INVALID_OPERATION || len == AudioRecord.ERROR_BAD_VALUE) {
					break;
				}
				len = mp3Encoder.nativeEncodingPCM(audioData, len, mp3Data);
				for (DataStream audioStream : audioStreams) {
					try {
						OutputStream os = audioStream.getOutputStream();
						if (os != null)
							os.write(mp3Data, 0, len);
						os.flush();
					} catch (IOException e) {
						Log.e(TAG, "[run] Error writing stream", e);
						invalidAudioStreams.add(audioStream);
						break;
					} catch (Exception e) {
						Log.e(TAG, "[run] General exception", e);
						invalidAudioStreams.add(audioStream);
						break;
					}
				}
				if (invalidAudioStreams.size() > 0) {
					for (DataStream audioStream : invalidAudioStreams) {
						audioStream.release();
						audioStreams.remove(audioStream);
					}
					invalidAudioStreams.clear();
				}

				if (newAudioStreams.size() > 0) {
					audioStreams.addAll(newAudioStreams);
					newAudioStreams.clear();
				}
			}
			mp3Encoder.nativeCloseEncoder();
			isStreamingAudio = false;
		}

	}

	private class VideoEncoder extends Thread {

		@Override
		public void run() {

			if (isStreamingVideo)
				return;
			isStreamingVideo = true;

			List<DataStream> invalidVideoStreams = new ArrayList<DataStream>(1);
			while (true) {
				if (!isStreamingVideo)
					break;

				for (DataStream videoStream : videoStreams) {
					try {
						OutputStream os = videoStream.getOutputStream();
						if (os != null) {
							MatOfByte mat = convertRgbaToJpeg();
							if (mat == null) break;
							byte[] data = mat.toArray();
							os.write(("Content-type: image/jpeg\r\n" +
									"Content-Length: " + data.length +
									"\r\n\r\n").getBytes());
							os.write(data);
							os.write(("\r\n--" + NanoHttpd.MULTIPART_BOUNDARY + "\r\n").getBytes());
							os.flush();
						}
					} catch (IOException e) {
						Log.e(TAG, "[run] Error writing stream", e);
						invalidVideoStreams.add(videoStream);
						break;
					} catch (Exception e) {
						Log.e(TAG, "[run] General exception", e);
						invalidVideoStreams.add(videoStream);
						break;
					}
				}
				if (invalidVideoStreams.size() > 0) {
					for (DataStream videoStream : invalidVideoStreams) {
						videoStream.release();
						videoStreams.remove(videoStream);
					}
					invalidVideoStreams.clear();
				}
				if (newVideoStreams.size() > 0) {
					videoStreams.addAll(newVideoStreams);
					newVideoStreams.clear();
				}
			}
			isStreamingVideo = false;
		}

	}

	static private native String nativeQueryInternet();

	private class NatPMPClient extends Thread {
		String queryResult;
		Handler handleQueryResult = new Handler(getMainLooper());

		@Override
		public void run() {
			queryResult = nativeQueryInternet();
			if (queryResult.startsWith("error:")) {
				handleQueryResult.post(new Runnable() {
					@Override
					public void run() {
						// tvMessage1.setText(getString(R.string.msg_access_query_error));
						ToastUtils.r(VideoStreamingActivity.this, getString(R.string.msg_access_query_error), Toast.LENGTH_SHORT);
					}
				});
			} else {
				handleQueryResult.post(new Runnable() {
					@Override
					public void run() {
						// tvMessage1.setText(queryResult);
						ToastUtils.r(VideoStreamingActivity.this, queryResult, Toast.LENGTH_SHORT);
					}
				});
			}
		}
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		//MyMobKitApp.setSurveillanceMode(true);
		
		if (cameraView != null) return;
		
		cameraView = new CameraView(openCVCameraView);

		if (setupStreamingServer()) {
			int wid = cameraView.getWidth();
			int hei = cameraView.getHeight();
			cameraView.setupCamera(wid, hei);
		}

		overlayView = (OverlayView) findViewById(R.id.surface_overlay);
		overlayView.setOnTouchListener(this);

		releaseFrames();
		setupFrames(width, height);
	}

	@Override
	public void onCameraViewStopped() {
		// releaseFrames();
	}

	private void setupFrames(int width, int height) {
		cameraRgbaFrame = new Mat(height, width, CvType.CV_8UC4);
		streamedRgbaFrame = new Mat(height, width, CvType.CV_8UC4);
	}

	private void releaseFrames() {
		releaseFrame(cameraRgbaFrame);
		releaseFrame(streamedRgbaFrame);
	}

	private void releaseFrame(Mat frame) {
		if (frame != null) {
			frame.release();
			frame = null;
		}
	}

	private Processor<Map<String, String>, Map<String, String>, InputStream> captureProcessor = new Processor<Map<String, String>, Map<String, String>, InputStream>() {

		@Override
		public InputStream process(Map<String, String> headers, Map<String, String> params) {
			// if (streamedRgbaFrame == null)
			// return null;

			MatOfByte matOfByte = convertRgbaToJpeg();
			if (matOfByte != null) {
				InputStream is = new ByteArrayInputStream(matOfByte.toArray());
				params.put("mime", "image/jpeg");
				return is;
			}
			return null;
		}
	};

	private MatOfByte convertRgbaToJpeg() {
		MatOfByte matOfByte = new MatOfByte();
		boolean ret = false;
		// JPEG quality
		MatOfInt matParams = new MatOfInt(Highgui.IMWRITE_JPEG_QUALITY, imageQuality);
		synchronized (streamingLock) {
			try {
				ret = Highgui.imencode(".jpg", streamedRgbaFrame, matOfByte, matParams);
			} catch (Exception ex) {
				Log.e(TAG, "[convertRgbaToJpeg] Error encoding JPEG", ex);
				return null;
			}
		}
		if (ret) {
			return matOfByte;
		} else {
			return null;
		}
	}

	private Processor<Map<String, String>, Map<String, String>, InputStream> videoProcessor = new Processor<Map<String, String>, Map<String, String>, InputStream>() {

		@Override
		public InputStream process(Map<String, String> headers, Map<String, String> params) {
			Random rnd = new Random();
			String etag = Integer.toHexString(rnd.nextInt());
			DataStream videoStream = new DataStream("com.mymobkit" + etag);

			videoStream.prepare(128, 8192);
			InputStream is = null;
			try {
				is = videoStream.getInputStream();
				OutputStream os = videoStream.getOutputStream();
				os.write(("Server: myMobKit Server\r\n" +
						"Connection: close\r\n" +
						"Cache-Control: no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0\r\n" +
						"Pragma: no-cache\r\n" +
						"Expires: -1\r\n" +
						"Access-Control-Allow-Origin: *\r\n" +
						"Content-Type: multipart/x-mixed-replace; " +
						"boundary=" + NanoHttpd.MULTIPART_BOUNDARY + "\r\n" +
						"\r\n" +
						"--" + NanoHttpd.MULTIPART_BOUNDARY + "\r\n").getBytes());
				os.flush();
			} catch (IOException e) {
				videoStream.release();
				return null;
			}

			newVideoStreams.add(videoStream);

			if (!isStreamingVideo) {
				videoEncoder = new VideoEncoder();
				videoEncoder.start();
			}
			return is;
		}
	};

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		cameraRgbaFrame = inputFrame.rgba();

		if (!streamDetectedObject) {
			synchronized (streamingLock) {
				// if (streamedRgbaFrame != null)
				// releaseFrame(streamedRgbaFrame);
				Imgproc.cvtColor(cameraRgbaFrame, streamedRgbaFrame, Imgproc.COLOR_RGBA2BGRA);
			}
		}
		if (faceDetectedRequired)
			cameraRgbaFrame = faceDetector.detect(inputFrame);

		if (motionDetectionRequired) {
			if (!GlobalData.isPhoneInMotion()) {
				cameraRgbaFrame = motionDetector.detect(cameraRgbaFrame);
			}
		}

		if (streamDetectedObject) {
			synchronized (streamingLock) {
				// if (streamedRgbaFrame != null)
				// releaseFrame(streamedRgbaFrame);
				Imgproc.cvtColor(cameraRgbaFrame, streamedRgbaFrame, Imgproc.COLOR_RGBA2BGRA);
			}
		}
		return cameraRgbaFrame;

	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			CameraMenuItem item = (CameraMenuItem) parent.getItemAtPosition(position);
			selectItem(position, item);
		}
	}

	private void selectItem(int position, CameraMenuItem item) {

		switch (position) {
		case 0:
			// Toggle URL display
			int visibility = tvMessage1.getVisibility();
			if (visibility == View.VISIBLE) {
				item.setLabel(this.getString(R.string.camera_url_show));
				tvMessage1.setVisibility(View.INVISIBLE);
			} else {
				item.setLabel(this.getString(R.string.camera_url_hide));
				tvMessage1.setVisibility(View.VISIBLE);
			}
			break;
		case 1:
			// Toggle camera
			toggleCamera(item);
			break;
		case 2:
			// Resolutions
			configureResolution();
			break;
		case 3:
			// Settings
			Intent returnIntent = getIntent();
			returnIntent.putExtra(AppConfig.FUNCTION_PARAM, MenuListSetup.CAMERA_SETTINGS.getCode());
			setResult(RESULT_OK, returnIntent);
			release();
			break;
		case 4:
			// Quit
			release();
			break;
		}

		// update selected item and title, then close the drawer
		drawerList.setItemChecked(position, true);
		drawerLayout.closeDrawer(drawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
		MyMobKitApp.setSurveillanceMode(true);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	private void toggleCamera(CameraMenuItem item) {

		if (Camera.getNumberOfCameras() == 1) {
			return;
		}
		String label = item.getLabel();
		if (label.equalsIgnoreCase(getString(R.string.camera_front))) {
			openCVCameraView.setCamera(CameraBridgeViewBase.CAMERA_ID_FRONT);
			item.setLabel(getString(R.string.camera_back));
		} else {
			openCVCameraView.setCamera(CameraBridgeViewBase.CAMERA_ID_BACK);
			item.setLabel(getString(R.string.camera_front));
		}
	}

	private void configureResolution() {
		final List<Size> resolutions = openCVCameraView.getResolutionList();
		String[] possibleSizes = new String[resolutions.size()];
		int idx = 0;
		Size currentResolution = openCVCameraView.getResolution();
		for (int i = 0; i < resolutions.size(); i++) {
			Size s = resolutions.get(i);
			possibleSizes[i] = s.width + " x " + s.height;
			if (s.height == currentResolution.height && s.width == currentResolution.width)
				idx = i;
		}
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setSingleChoiceItems(possibleSizes, idx, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				chosenResolution = item;
			}
		});

		alert.setIcon(R.drawable.ic_launcher);
		alert.setTitle(R.string.label_title_resolution);

		alert.setPositiveButton(R.string.label_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Size chosenSize = resolutions.get(chosenResolution);
				openCVCameraView.setResolution(chosenSize);
			}
		});
		alert.setNegativeButton(R.string.label_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();
	}

	/**
	 * 
	 * Camera menu adapter.
	 * 
	 */
	public class CameraMenuAdapter extends ArrayAdapter<CameraMenuItem> {

		public CameraMenuAdapter(Context context) {
			super(context, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, null);
			TextView title = (TextView) convertView.findViewById(R.id.menu_label);
			CameraMenuItem item = getItem(position);
			title.setText(item.getLabel());
			return convertView;
		}
	}

}
