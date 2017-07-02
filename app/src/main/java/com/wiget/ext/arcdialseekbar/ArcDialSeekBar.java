package com.wiget.ext.arcdialseekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ArcDialSeekBar extends View
{
	private static final String TAG = "ArcDialSeekBar";
	/**
	 * least touch size
	 */
	private static final float MIN_TOUCH_DP = 48;
	private static int DEFAULT_TEXT_SIZE = 16;
	private static final int DEFAULT_INNER_DIVIDER_COUNT = 5;
	private static final int DEFAULT_LONG_MARK_COUNT = 9;
	private static final int DEFAULT_SHORT_MARK_LEN = 16;
	private static final int DEFAULT_LONG_MARK_LEN = 24;
	private static final float DEFAULT_START_ANGLE = 90f;
	private static final float DEFAULT_END_ANGLE = 90f;
	private static final float DEFAULT_STROKE_WIDTH = 4;
    private static final String COLOR_MODE_SINGLE = "single";
    private static final String COLOR_MODE_GRADIENT = "gradient";
    private static final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#009688");

	/**
	 * Used to scale the dp units to pixels
	 */
	private final float DENSITY = getResources().getDisplayMetrics().density;

	/**
	 *  default progress background color 
	 */
	private int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#ffaaaaaa");

	/**
	 * center X of the arc 
	 */
	private float mCenterX;
	
	/**
	 * center Y of the arc 
	 */
	private float mCenterY;
	
	/**
	 * the width of the scale mark
	 */
	private float mStrokeWidth;

	/**
	 * the paint of the scale mark's progress 
	 */
	private Paint mArcProgressLinePaint;
	
	/**
	 * the paint of the scale mark's background 
	 */
	private Paint mArcLinePaint;
	
	/**
	 * the paint of indicator text 
	 */
	private Paint mTextPaint ; 
	
	/**
	 * radius of arc
	 */
	private float mRadius;
	
	/**
	 * start angle of arc
	 */
	private float mStartAngle = DEFAULT_START_ANGLE;
	
	/**
	 * end angle of arc 
	 */
	private float mEndAngle = DEFAULT_END_ANGLE;

	/**
	 * the length of short mark
	 */
	private float mShortMarkLen = DEFAULT_SHORT_MARK_LEN;
	/**
	 * the length of long mark
	 * */
	private float mLongMarkLen = DEFAULT_LONG_MARK_LEN;
	/**
	 * count of long mark
	 * */
	private int mLongMarkCount = DEFAULT_LONG_MARK_COUNT;

	/**
	 * count of divider between two long mark
	 * */
	private int mInnerDividerCount = DEFAULT_INNER_DIVIDER_COUNT;

	/**
	 * background of arc color
	 * */
    private int mArcBackGroundColor ;

	/**
	 * current progress
	 * */
    private int mProgress ;

	/**
	 * max value of seek bar,this value depend on mLongMarkCount and mInnerDividerCount
	 * mMax = mInnerDividerCount*(mLongMarkCount-1);
	 * note: this value can be assigned in layout file. if you want to change it,
	 * please set mLongMarkCount or mInnerDividerCount
	 * */
    private int mMax ;
    /**
	 * touchable state ,if false ,this view should be regarded as progress bar ,or seek bar
	 * */
    private boolean bTouchEnabled ;

	/**
	 * if true ,show indicator text
	 * */
    private boolean bShowText ;
	/**
	 * indicator text size
	 * */
	private float mIndicatorTextSize ;

	/**
	 * indicator text color
	 * */
	private int mIndicatorTextColor;

	/**
	 * tiny angle of step when draw a short or long mark
	 * */
    private float mStepAngle ;

	/**
	 * total angle from start to end
	 * */
    private float mTotalDegree ; 
    /**
     * rotate angle of gradient shader
     * */
	private float mGradientRotateAngle;

    /**
     * single ,gradient
     */
    private String mColorMode ;

    private int mArcProgressColor = DEFAULT_PROGRESS_COLOR;

	/**
	 * arc progress colors for the gradient shader
	 * */
    private int[] mArcColors = new int[]{
		    getResources().getColor(R.color.colorAccent1),
            getResources().getColor(R.color.colorAccent2),
            getResources().getColor(R.color.colorAccent3),
            getResources().getColor(R.color.colorAccent4),
            getResources().getColor(R.color.colorAccent5)
    };

    private String[] mIndicatorText ;

	public ArcDialSeekBar( Context context )
	{
		super( context );
		init(null,0);
	}

	public ArcDialSeekBar( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		init(attrs,0);
	}

	public ArcDialSeekBar( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		init(attrs,defStyle);
	}

	private void initAttributes(TypedArray attrs)
	{
		mStrokeWidth = attrs.getDimension(R.styleable.ArcDialSeekBar_line_stroke_width,DEFAULT_STROKE_WIDTH);
		mArcBackGroundColor = attrs.getColor(R.styleable.ArcDialSeekBar_background_color,DEFAULT_BACKGROUND_COLOR);
		mStartAngle = attrs.getFloat(R.styleable.ArcDialSeekBar_start_angle,DEFAULT_START_ANGLE);
		mEndAngle = attrs.getFloat(R.styleable.ArcDialSeekBar_end_angle,DEFAULT_END_ANGLE);
		bTouchEnabled = attrs.getBoolean(R.styleable.ArcDialSeekBar_touch_enable,true);
		bShowText = attrs.getBoolean(R.styleable.ArcDialSeekBar_show_text,true);
		mIndicatorTextColor = attrs.getColor(R.styleable.ArcDialSeekBar_text_color,Color.BLACK);
		mIndicatorTextSize = attrs.getDimensionPixelSize(R.styleable.ArcDialSeekBar_text_size,DEFAULT_TEXT_SIZE);
		mShortMarkLen = attrs.getDimension(R.styleable.ArcDialSeekBar_short_mark_len,DEFAULT_SHORT_MARK_LEN);
		mLongMarkLen = attrs.getDimension(R.styleable.ArcDialSeekBar_long_mark_len,DEFAULT_LONG_MARK_LEN);
		mLongMarkCount = attrs.getInt(R.styleable.ArcDialSeekBar_long_mark_count,DEFAULT_LONG_MARK_COUNT);
		mInnerDividerCount = attrs.getInt(R.styleable.ArcDialSeekBar_inner_divider_count,DEFAULT_INNER_DIVIDER_COUNT);
        mColorMode = attrs.getString(R.styleable.ArcDialSeekBar_color_mode);
        if(COLOR_MODE_SINGLE.equals(mColorMode)){
            mColorMode = COLOR_MODE_SINGLE;
            mProgress = attrs.getColor(R.styleable.ArcDialSeekBar_progress_color,DEFAULT_PROGRESS_COLOR);
        }else{
            mColorMode = COLOR_MODE_GRADIENT;
        }
	}

	private void init(AttributeSet attrs,int defStyle) {
		// TODO Auto-generated method stub
		final TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.ArcDialSeekBar, defStyle, 0);
		initAttributes(attrArray);
		attrArray.recycle();

		while(mStartAngle > 360f){
			mStartAngle -= 360f;
		}

		while(mEndAngle > 360f){
			mEndAngle -= 360f;
		}

		mMax = mInnerDividerCount*(mLongMarkCount-1);
		mArcLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mArcLinePaint.setStyle(Paint.Style.STROKE);
		mArcLinePaint.setStrokeWidth(mStrokeWidth);
		mArcLinePaint.setColor(mArcBackGroundColor);
		
		mArcProgressLinePaint = new Paint( Paint.ANTI_ALIAS_FLAG );
		mArcProgressLinePaint.setStyle( Paint.Style.STROKE );
		mArcProgressLinePaint.setStrokeWidth(mStrokeWidth);
		mArcProgressLinePaint.setColor(mArcProgressColor);

		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setStyle(Paint.Style.STROKE);
		mTextPaint.setColor(mIndicatorTextColor);
		mTextPaint.setTextSize(mIndicatorTextSize);

	    if(mStartAngle == mEndAngle){
	    	mStepAngle = (float)((360f/mMax)/360f*Math.PI*2);
	    	mTotalDegree = 360f;
			mGradientRotateAngle = mStartAngle;
	    }else if(mStartAngle < mEndAngle){
	    	mStepAngle= (float)((mEndAngle-mStartAngle)/mMax/360f*Math.PI*2);
	    	mTotalDegree = mEndAngle-mStartAngle;
			mGradientRotateAngle = mStartAngle - (360f-(mEndAngle-mStartAngle))/2f;
	    }else{
	    	mStepAngle = (float)(((360f-mStartAngle)+mEndAngle)/mMax/360f*Math.PI*2);
	    	mTotalDegree = (360f-mStartAngle)+mEndAngle;
			mGradientRotateAngle = (mStartAngle-mEndAngle)/2f+mEndAngle;
	    }
	    if(COLOR_MODE_GRADIENT.equals(mColorMode)) {
            float step = 1f / (mArcColors.length-1);
            float[] positions = new float[mArcColors.length];
            for (int i = 0; i < mArcColors.length; i++) {
                positions[i] = step * i;
            }
            //{ 0.0f, 0.25f, 0.5f, 0.75f, 1f};
            SweepGradient gradient = new SweepGradient(0, 0, mArcColors, positions);
            Matrix matrix = new Matrix();
            matrix.setRotate(mGradientRotateAngle, 0, 0);
            gradient.setLocalMatrix(matrix);
            mArcProgressLinePaint.setShader(gradient);
        }
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
		int width = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
		int min = Math.min(width,height);
		setMeasuredDimension(min,min);
		mRadius = min*.5f;
		mCenterX = width/2;
		mCenterY = height/2;
	}

	@Override
	public void onDraw( Canvas canvas )
	{
		super.onDraw( canvas );
		float a = (float)(mStartAngle/360f*Math.PI*2);
		//Log.d(TAG,"a====>"+a+",step===>"+mStepAngle);
		canvas.translate(mCenterX, mCenterY);
		//draw background progress
		for( int n = 0; n<mMax+1; a += mStepAngle, ++n ){
			final float cos = (float)Math.cos(a);
			final float sin = (float)Math.sin(a);
			final float r;
			if(n % mInnerDividerCount > 0){
				r = mRadius-mShortMarkLen;
			}else{
				r = mRadius-mLongMarkLen;
				if(bShowText){
                    if(mIndicatorText != null && mIndicatorText.length == mLongMarkCount){
                        canvas.drawText(mIndicatorText[n/mInnerDividerCount],
                                (r - 20) * cos - 10, (r - 20) * sin + 6, mTextPaint);
                    }else {
                        canvas.drawText(String.valueOf(n),
                                (r - 20) * cos - 10, (r - 20) * sin + 6, mTextPaint);
                    }
				}
			}
			canvas.drawLine(r*cos,r*sin,mRadius*cos,mRadius*sin,mArcLinePaint);
		}
		
		//draw progress
		a = (float)(mStartAngle/360f*Math.PI*2);
		if(mProgress > 0){
			for( int n = 0; n<mProgress+1; a += mStepAngle, ++n ){
				final float cos = (float)Math.cos(a);
				final float sin = (float)Math.sin(a);
				final float r;
				if(n % mInnerDividerCount > 0){
					r = mRadius-mShortMarkLen;
				}else{
					r = mRadius-mLongMarkLen;
				}
				canvas.drawLine(r*cos,r*sin,mRadius*cos,mRadius*sin,mArcProgressLinePaint);
			}
		}
	}
	
	public void setProgress(int progress)
	{
		if(progress >=0 && progress <= mMax){
			this.mProgress = progress;
		}else{
			this.mProgress = mMax;
		}
		invalidate();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(!bTouchEnabled){
			return false;
		}
//		Convert coordinates to our internal coordinate system
		float x = event.getX() - mRadius;
		float y = event.getY() - mRadius;
		float minimumTouchTarget = MIN_TOUCH_DP * DENSITY;
		
		// Get the distance from the center of the circle in terms of a radius
		float touchEventRadius = (float) Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
		Log.d(TAG, "x==>"+x+",y==>"+y+",touchRadius==>"+touchEventRadius);
		float touchAngle = (float) ((Math.atan2(y, x) / Math.PI * 180f) % 360f);
		touchAngle = (touchAngle < 0 ? 360f + touchAngle : touchAngle);
		Log.d(TAG, "touchAngle==>"+touchAngle);
		float cwDistanceFromStart = touchAngle - mStartAngle;
		cwDistanceFromStart = (cwDistanceFromStart < 0 ? 360f + cwDistanceFromStart : cwDistanceFromStart);
		
		float cwDistanceFromEnd = touchAngle - mEndAngle;
		cwDistanceFromEnd = (cwDistanceFromEnd < 0 ? 360f + cwDistanceFromEnd : cwDistanceFromEnd);
		Log.d(TAG, "cwDistanceFromStart==>"+cwDistanceFromStart+",cwDistanceFromEnd"+cwDistanceFromEnd);
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				float d = Math.abs(touchEventRadius-mRadius);
				if(d<=minimumTouchTarget/2){
					setProgressByAngle(touchAngle);
				}
				break;
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		return super.onTouchEvent(event);
	}

	/** 
	 * set progress value based on touched angle 
	 * @param angle
	 */
	private void setProgressByAngle(float angle) {		
		
		if(mStartAngle == mEndAngle){
			int progress;
			if(angle<=mStartAngle){
				progress = Math.round((float)mMax * (360f-mStartAngle+angle) / mTotalDegree);
			}else{
				progress = Math.round((float)mMax * (angle-mStartAngle) / mTotalDegree);
			}

			setProgress(progress);
		}else if(mStartAngle<mEndAngle){
			if(angle >= mStartAngle && angle<=mEndAngle){
				int progress = Math.round((float)mMax * (angle-mStartAngle) / mTotalDegree);
				setProgress(progress);
			}
		}else{
			if(angle>=mStartAngle){
				int progress = Math.round((float)mMax * (angle-mStartAngle) / mTotalDegree);
				setProgress(progress);
			}else if(angle<=mEndAngle){
				int progress = Math.round((float)mMax * (360-mStartAngle+angle) / mTotalDegree);
				setProgress(progress);
			}
		}	
	}

    /**
     * @param state
     */
    public void setTouchEnable(boolean state)
	{
		this.bTouchEnabled = state;
	}

    /**
     * @param color
     */
    public void setTextColor(int color)
	{
		mIndicatorTextColor = color;
	}

    /**
     * @param colors
     */
    public void setGradientColors(int[] colors)
	{
		this.mArcColors = colors;
		float step = 1f/(mArcColors.length-1);
		float[] positions = new float[mArcColors.length];
		for(int i=0;i<mArcColors.length;i++){
			positions[i] = step*i;
		}
		SweepGradient gradient = new SweepGradient(0, 0, mArcColors,positions);
		Matrix matrix = new Matrix();
		matrix.setRotate(mGradientRotateAngle, 0, 0);
		gradient.setLocalMatrix(matrix);
		mArcProgressLinePaint.setShader(gradient);
	}

    /**
     * @param dimens
     */
    public void setShortMarkLength(float dimens)
	{
		this.mShortMarkLen = dimens;
	}

    /**
     * @param dimens
     */
    public void setLongMarkLength(float dimens)
	{
		this.mLongMarkLen = dimens ;
	}

    /**
     * @param count
     */
    public void setLongMarkCount(int count)
	{
		this.mLongMarkCount = count;
	}

    /**
     * setting the indicator texts needs the length is equal to mLongMarkCount
     * @param texts
     */
	public void setIndicatorText(String[] texts)
    {
        if(texts.length == mLongMarkCount) {
            this.mIndicatorText = texts;
        }
    }

    public int getLongMarkCount()
    {
        return this.mLongMarkCount;
    }

}

