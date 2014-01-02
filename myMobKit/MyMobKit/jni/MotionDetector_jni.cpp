#include <MotionDetector_jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/contrib/detection_based_tracker.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <string>
#include <vector>

#include <android/log.h>

#define LOG_TAG "MotionDetection/MotionDetector"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))

using namespace std;
using namespace cv;

inline void vector_Rect_to_Mat(vector<Rect>& v_rect, Mat& mat) {
	mat = Mat(v_rect, true);
}

JNIEXPORT jlong JNICALL Java_com_mymobkit_motion_detection_MotionDetector_nativeCreateObject(
		JNIEnv * jenv, jclass, jstring jFileName, jint faceSize) {
	LOGD(
			"Java_com_mymobkit_motion_detection_MotionDetector_nativeCreateObject enter");
	const char* jnamestr = jenv->GetStringUTFChars(jFileName, NULL);
	string stdFileName(jnamestr);
	jlong result = 0;

	try {
		DetectionBasedTracker::Parameters DetectorParams;
		if (faceSize > 0)
			DetectorParams.minObjectSize = faceSize;
		result = (jlong) new DetectionBasedTracker(stdFileName, DetectorParams);
	} catch (cv::Exception& e) {
		LOGD("nativeCreateObject caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeCreateObject caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
		return 0;
	}

	LOGD(
			"Java_com_mymobkit_motion_detection_MotionDetector_nativeCreateObject exit");
	return result;
}

JNIEXPORT void JNICALL Java_com_mymobkit_motion_detection_MotionDetector_nativeDestroyObject(
		JNIEnv * jenv, jclass, jlong thiz) {
	LOGD(
			"Java_com_mymobkit_motion_detection_MotionDetector_nativeDestroyObject enter");
	try {
		if (thiz != 0) {
			((DetectionBasedTracker*) thiz)->stop();
			delete (DetectionBasedTracker*) thiz;
		}
	} catch (cv::Exception& e) {
		LOGD("nativeestroyObject caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeDestroyObject caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
	LOGD(
			"Java_com_mymobkit_motion_detection_MotionDetector_nativeDestroyObject exit");
}

JNIEXPORT void JNICALL Java_com_mymobkit_motion_detection_MotionDetector_nativeStart(
		JNIEnv * jenv, jclass, jlong thiz) {
	LOGD("Java_com_mymobkit_motion_detection_MotionDetector_nativeStart enter");
	try {
		((DetectionBasedTracker*) thiz)->run();
	} catch (cv::Exception& e) {
		LOGD("nativeStart caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeStart caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
	LOGD("Java_com_mymobkit_motion_detection_MotionDetector_nativeStart exit");
}

JNIEXPORT void JNICALL Java_com_mymobkit_motion_detection_MotionDetector_nativeStop(
		JNIEnv * jenv, jclass, jlong thiz) {
	LOGD("Java_com_mymobkit_motion_detection_MotionDetector_nativeStop enter");
	try {
		((DetectionBasedTracker*) thiz)->stop();
	} catch (cv::Exception& e) {
		LOGD("nativeStop caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeStop caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
	LOGD("Java_com_mymobkit_motion_detection_MotionDetector_nativeStop exit");
}

JNIEXPORT void JNICALL Java_com_mymobkit_motion_detection_MotionDetector_nativeSetFaceSize(
		JNIEnv * jenv, jclass, jlong thiz, jint faceSize) {
	LOGD(
			"Java_com_mymobkit_motion_detection_MotionDetector_nativeSetFaceSize enter");
	try {
		if (faceSize > 0) {
			DetectionBasedTracker::Parameters DetectorParams =
					((DetectionBasedTracker*) thiz)->getParameters();
			DetectorParams.minObjectSize = faceSize;
			((DetectionBasedTracker*) thiz)->setParameters(DetectorParams);
		}
	} catch (cv::Exception& e) {
		LOGD("nativeStop caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeSetFaceSize caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
	LOGD(
			"Java_com_mymobkit_motion_detection_MotionDetector_nativeSetFaceSize exit");
}

JNIEXPORT void JNICALL Java_com_mymobkit_motion_detection_MotionDetector_nativeDetect(
		JNIEnv * jenv, jclass, jlong thiz, jlong imageGray, jlong faces) {
	LOGD("Java_com_mymobkit_motion_detection_MotionDetector_nativeDetect enter");
	try {
		vector<Rect> RectFaces;
		((DetectionBasedTracker*) thiz)->process(*((Mat*) imageGray));
		((DetectionBasedTracker*) thiz)->getObjects(RectFaces);
		vector_Rect_to_Mat(RectFaces, *((Mat*) faces));
	} catch (cv::Exception& e) {
		LOGD("nativeCreateObject caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeDetect caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}
	LOGD("Java_com_mymobkit_motion_detection_MotionDetector_nativeDetect exit");
}

JNIEXPORT void JNICALL Java_com_mymobkit_motion_detection_MotionDetector_nativeMotionDetect(
		JNIEnv * jenv, jclass, jlong thiz, jlong imageGray, jlong faces) {
	LOGD(
			"Java_com_mymobkit_motion_detection_MotionDetector_nativeMotionDetect enter");

	/*try {

		Mat* frame = (Mat*) imageGray;
		Mat* back;
		Mat* fore;

		const int nmixtures = 3;
		const bool bShadowDetection = true;
		const int history = 5;

		vector<vector<cv::Point> > contours;

		cv::BackgroundSubtractorMOG2 bg = new BackgroundSubtractorMOG2(5, 3,
				bShadowDetection);
		bg.operator()(frame, fore);
		cv::erode(fore, fore, cv::Mat());
		cv::dilate(fore, fore, cv::Mat());
		cv::findContours(fore, contours, CV_RETR_EXTERNAL,
				CV_CHAIN_APPROX_NONE);
		cv::drawContours(frame, contours, -1, cv::Scalar(0, 0, 255), 2);

	} catch (cv::Exception& e) {
		LOGD("nativeMotionDetect caught cv::Exception: %s", e.what());
		jclass je = jenv->FindClass("org/opencv/core/CvException");
		if (!je)
			je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je, e.what());
	} catch (...) {
		LOGD("nativeMotionDetect caught unknown exception");
		jclass je = jenv->FindClass("java/lang/Exception");
		jenv->ThrowNew(je,
				"Unknown exception in JNI code {highgui::VideoCapture_n_1VideoCapture__()}");
	}*/
	LOGD(
			"Java_com_mymobkit_motion_detection_MotionDetector_nativeMotionDetect exit");
}
