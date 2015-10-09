package com.shaweibo.biu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

public class StickerImageSpan extends ImageSpan {
	
	  public StickerImageSpan(Drawable d) {
		super(d);
		// TODO Auto-generated constructor stub
	}
	  
	  
	  

	public StickerImageSpan(Context context, Bitmap b, int verticalAlignment) {
		super(context, b, verticalAlignment);
		// TODO Auto-generated constructor stub
	}




	public StickerImageSpan(Context context, Bitmap b) {
		super(context, b);
		// TODO Auto-generated constructor stub
	}




	public StickerImageSpan(Context context, int resourceId,
			int verticalAlignment) {
		super(context, resourceId, verticalAlignment);
		// TODO Auto-generated constructor stub
	}




	public StickerImageSpan(Context context, int resourceId) {
		super(context, resourceId);
		// TODO Auto-generated constructor stub
	}




	public StickerImageSpan(Context context, Uri uri, int verticalAlignment) {
		super(context, uri, verticalAlignment);
		// TODO Auto-generated constructor stub
	}




	public StickerImageSpan(Context context, Uri uri) {
		super(context, uri);
		// TODO Auto-generated constructor stub
	}




	public StickerImageSpan(Drawable d, int verticalAlignment) {
		super(d, verticalAlignment);
		// TODO Auto-generated constructor stub
	}




	public StickerImageSpan(Drawable d, String source, int verticalAlignment) {
		super(d, source, verticalAlignment);
		// TODO Auto-generated constructor stub
	}




	public StickerImageSpan(Drawable d, String source) {
		super(d, source);
		// TODO Auto-generated constructor stub
	}




	public int getSize(Paint paint, CharSequence text, int start, int end,  
              FontMetricsInt fm) {  
          Drawable d = getDrawable();  
          Rect rect = d.getBounds();  
          if (fm != null) {  
              FontMetricsInt fmPaint=paint.getFontMetricsInt();  
              int fontHeight = fmPaint.bottom - fmPaint.top;  
              int drHeight=rect.bottom-rect.top;  
                
              int top= drHeight/2 - fontHeight/4;  
              int bottom=drHeight/2 + fontHeight/4;  
                
              fm.ascent=-bottom;  
              fm.top=-bottom;  
              fm.bottom=top;  
              fm.descent=top;  
          }  
          return rect.right;  
      }  
        
      @Override  
      public void draw(Canvas canvas, CharSequence text, int start, int end,  
              float x, int top, int y, int bottom, Paint paint) {  
          Drawable b = getDrawable();  
          canvas.save();  
          int transY = 0;  
          transY = ((bottom-top) - b.getBounds().bottom)/2+top;  
          canvas.translate(x, transY);  
          b.draw(canvas);  
          canvas.restore();  
      }  
    

}
