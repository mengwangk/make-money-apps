package com.mymobkit.motion.detection;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG;

public final class BackgroundSubtractorDetector extends BaseDetector implements IDetector {

	private static final double LEARNING_RATE = 0.1 ;

	private static final int MIXTURES = 4;
	
	private static final int HISTORY = 3;
	
	private static final double BACKGROUND_RATIO = 0.8;

	// ring image buffer
	private Mat buf = null;
	private BackgroundSubtractorMOG bg;
	private Mat fgMask = new Mat();
	
	public BackgroundSubtractorDetector() {
		bg = new BackgroundSubtractorMOG(HISTORY, MIXTURES, BACKGROUND_RATIO);
	}
	
	public BackgroundSubtractorDetector(int backgroundRatio){
		bg = new BackgroundSubtractorMOG(HISTORY, MIXTURES, (backgroundRatio / 100.0));
	}

	@Override
	public Mat detect(Mat source) {

		// get current frame size
		Size size = source.size(); 
		
		// allocate images at the beginning or
		// reallocate them if the frame size is changed
		if (buf == null || buf.width() != size.width || buf.height() != size.height) {
			if (buf == null) {
				buf = new Mat(size, CvType.CV_8UC1);
				buf = Mat.zeros(size, CvType.CV_8UC1);
			}
		}
		
		// convert frame to gray scale
		Imgproc.cvtColor(source, buf, Imgproc.COLOR_RGBA2RGB);
		
		bg.apply(buf, fgMask, LEARNING_RATE); //apply() exports a gray image by definition
		
		//Imgproc.erode(fgMask, fgMask, new Mat());
		//Imgproc.dilate(fgMask, fgMask, new Mat());
		
	    //Imgproc.cvtColor(fgMask, silh, Imgproc.COLOR_GRAY2RGBA);
	    
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(fgMask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		Imgproc.drawContours(source, contours, -1, contourColor, contourThickness);
		if (contours.size() > 0) {
			targetDetected = true;
		} else {
			targetDetected = false;
		}
		return source;
	}

}
