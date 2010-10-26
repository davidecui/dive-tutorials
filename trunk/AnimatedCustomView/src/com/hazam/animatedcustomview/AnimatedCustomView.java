package com.hazam.animatedcustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimatedCustomView extends View {

	private static final String QUOTE = "Nobody uses Java anymore. It's this big heavyweight ball and chain.";

	private Animation anim = null;
	private final PointF center = new PointF();
	private int textSize = 25;
	private Paint paint = null;

	private Path path = null;

	public AnimatedCustomView(Context context) {
		super(context);
		init();
	}

	public AnimatedCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AnimatedCustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		paint = new Paint();
		paint.setColor(Color.YELLOW);
		paint.setTextSize(textSize);
		paint.setAntiAlias(true);
		/*anim = new RotateAnimation(0.0f, 360.0f, center.x, center.y);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(10000L);
		AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
		anim.setInterpolator(adi);*/
		anim = AnimationUtils.loadAnimation(getContext(), R.anim.round_and_round);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			center.x = (right - left) >> 1;
			center.y = (bottom - top) >> 1;
			startAnimation(anim);
			createCircle();
		}
	}

	private void createCircle() {
		path = new Path();
		int r = (int) Math.min(center.x, center.y) - textSize;
		path.addCircle(center.x, center.y, r, Direction.CW);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawTextOnPath(QUOTE, path, 0, 0, paint);
	}
}
