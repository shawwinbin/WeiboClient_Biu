package com.shaweibo.biu.widget;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shaweibo.biu.R;

public class TagImageVIew extends ImageView {

	
	public boolean isDrawTag=false;
	
	public boolean isNeedChange=false;
	
	public static Bitmap  gifTag;
	
	
	public TagImageVIew(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TagImageVIew(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TagImageVIew(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		try {
			super.onDraw(canvas);

			if (!isDrawTag) {
				return;
			}
			int w = getWidth();
			int h = getHeight();

			if (gifTag == null || gifTag.isRecycled()) {
				gifTag = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_gif);
			}
			canvas.drawBitmap(gifTag, w - gifTag.getWidth(), h - gifTag.getHeight(), null);
		} catch (OutOfMemoryError e) {

		} catch (Exception e) {
		}

	}

	public boolean isDrawTag() {
		return isDrawTag;
	}

	public void setDrawTag(boolean isDrawTag) {
		this.isDrawTag = isDrawTag;
	}

	public boolean isNeedChange() {
		return isNeedChange;
	}

	public void setNeedChange(boolean isNeedChange) {
		this.isNeedChange = isNeedChange;
	}


	

}
