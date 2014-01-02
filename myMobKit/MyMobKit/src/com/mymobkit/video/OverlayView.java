package com.mymobkit.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public final class OverlayView extends View {
	private Bitmap targetBMP = null;
	private Rect targetRect = null;

	public OverlayView(Context c, AttributeSet attr) {
		super(c, attr);
	}

	public void drawResult(Bitmap bmp) {
		if (targetRect == null)
			targetRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
		targetBMP = bmp;
		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (targetBMP != null) {
			canvas.drawBitmap(targetBMP, null, targetRect, null);
		}
	}

}
