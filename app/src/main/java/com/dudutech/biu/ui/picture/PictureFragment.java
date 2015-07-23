package com.dudutech.biu.ui.picture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES10;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.DeviceUtil;
import com.dudutech.biu.model.MessageModel;
import com.dudutech.biu.model.PicSize;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;


import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.droidsonroids.gif.GifDrawable;
import uk.co.senab.photoview.PhotoView;


/**
 * 部分代码参考自四次元 Asien
 * 
 * @author shaw
 *
 */
 public class PictureFragment extends Fragment  {

	public static PictureFragment newInstance(MessageModel.PictureUrl url) {
		PictureFragment fragment = new PictureFragment();
		
		Bundle args = new Bundle();
		args.putParcelable("url", url);
		fragment.setArguments(args);
		return fragment;
	}

	@InjectView(R.id.photoview)
	PhotoView photoView;
	@InjectView(R.id.webview)
	WebView mWebView;
	@InjectView(R.id.txtFailure)
	View viewFailure;
	@InjectView(R.id.tv_progress)
	TextView tv_progress;
	@InjectView(R.id.progressbar)
	ProgressBar  progressBar;
	@InjectView(R.id.view_progress)
	View  view_progress;
	
	private MessageModel.PictureUrl image;


    private PicSize pictureSize;

    public enum PictureStatus {
        wait, downloading, success, faild
    }

    private PictureStatus mStatus;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View mView= inflater.inflate(R.layout.fragment_picture,null);

		ButterKnife.inject(this,mView);

		mStatus = PictureStatus.wait;

		image = savedInstanceState == null ? (MessageModel.PictureUrl) getArguments().getParcelable("url")
										  : (MessageModel.PictureUrl) savedInstanceState.getSerializable("url");
		loadImage();
		return mView;
	}

	private  void loadImage(){

		String imageUrl=image.getMedium();

		File origPic= ImageLoader.getInstance().getDiskCache().get(image.getLarge());

		if(origPic.exists()||DeviceUtil.getNetworkType(getActivity())== DeviceUtil.NetWorkType.wifi){
			imageUrl=image.getLarge();
		}



		ImageLoader.getInstance().loadImage(imageUrl,null, options, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				view_progress.setVisibility(View.VISIBLE);

			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				view_progress.setVisibility(View.GONE);


				if(image.isGif()){

					loadGifPicture(imageUri);
				}
				else{
					if(loadedImage.getHeight()>1024||loadedImage.getWidth()>1024){
						loadLargePicture(imageUri);
					}
					else
					{

						loadNomalPicture(imageUri,loadedImage);
					}


				}



			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {

			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {

				String percent=  Math.round(100.0f * current / total)+"%";
				tv_progress.setText(percent);


			}
		});


	}


	private void loadNomalPicture(String url,Bitmap loadedImage){
		try {
			photoView.setImageBitmap(loadedImage);
			photoView.setVisibility(View.VISIBLE);
		}catch (OutOfMemoryError c){
			loadLargePicture(url);
		}



	}

	private void loadGifPicture(String url){

		File file = ImageLoader.getInstance().getDiskCache().get(url);
		if(file == null|| !file.exists()){
			return;
		}
		GifDrawable gifDrawable;
		try {
			gifDrawable = new GifDrawable(file);
			gifDrawable.reset();
			gifDrawable.start();
			photoView.setImageDrawable(gifDrawable);
			photoView.setVisibility(View.VISIBLE);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("url", image);
	}

	DisplayImageOptions options = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.ic_stub) // resource or bitmap
//			.showImageForEmptyUri(R.drawable.ic_empty) // resource or bitmap
//			.showImageOnFail(R.drawable.ic_error) // resource or bitmap
			.resetViewBeforeLoading(false)  // default
			.delayBeforeLoading(1000)
			.cacheInMemory(false) // default
			.cacheOnDisk(true) // default
//			.preProcessor(...)
//			.postProcessor(...)
//	        .extraForDownloader(...)
	        .imageScaleType(ImageScaleType.NONE) // default
			.bitmapConfig(Bitmap.Config.ARGB_8888) // default
//	       .decodingOptions(...)
	        .displayer(new SimpleBitmapDisplayer()) // default
//			.handler(new Handler()) // default
			.build();


	

	@SuppressLint("SetJavaScriptEnabled")
	private void loadLargePicture(String url) {
//        photoView.setImageDrawable(getResources().getDrawable(R.drawable.bg_timeline_loading));
		File file = ImageLoader.getInstance().getDiskCache().get(url);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		String str1 = "file://" + file.getAbsolutePath().replace("/mnt/sdcard/", "/sdcard/");
		String str2 = "<html>\n<head>\n     <style>\n          html,body{background:#3b3b3b;margin:0;padding:0;}          *{-webkit-tap-highlight-color:rgba(0, 0, 0, 0);}\n     </style>\n     <script type=\"text/javascript\">\n     var imgUrl = \""
				+ str1
				+ "\";"
				+ "     var objImage = new Image();\n"
				+ "     var realWidth = 0;\n"
				+ "     var realHeight = 0;\n"
				+ "\n"
				+ "     function onLoad() {\n"
				+ "          objImage.onload = function() {\n"
				+ "               realWidth = objImage.width;\n"
				+ "               realHeight = objImage.height;\n"
				+ "\n"
				+ "               document.gagImg.src = imgUrl;\n"
				+ "               onResize();\n"
				+ "          }\n"
				+ "          objImage.src = imgUrl;\n"
				+ "     }\n"
				+ "\n"
				+ "     function onResize() {\n"
				+ "          var scale = 1;\n"
				+ "          var newWidth = document.gagImg.width;\n"
				+ "          if (realWidth > newWidth) {\n"
				+ "               scale = realWidth / newWidth;\n"
				+ "          } else {\n"
				+ "               scale = newWidth / realWidth;\n"
				+ "          }\n"
				+ "\n"
				+ "          hiddenHeight = Math.ceil(30 * scale);\n"
				+ "          document.getElementById('hiddenBar').style.height = hiddenHeight + \"px\";\n"
				+ "          document.getElementById('hiddenBar').style.marginTop = -hiddenHeight + \"px\";\n"
				+ "     }\n"
				+ "     </script>\n"
				+ "</head>\n"
				+ "<body onload=\"onLoad()\" onresize=\"onResize()\" onclick=\"Android.toggleOverlayDisplay();\">\n"
				+ "     <table style=\"width: 100%;height:100%;\">\n"
				+ "          <tr style=\"width: 100%;\">\n"
				+ "               <td valign=\"middle\" align=\"center\" style=\"width: 100%;\">\n"
				+ "                    <div style=\"display:block\">\n"
				+ "                         <img name=\"gagImg\" src=\"\" width=\"100%\" style=\"\" />\n"
				+ "                    </div>\n"
				+ "                    <div id=\"hiddenBar\" style=\"position:absolute; width: 0%; background: #3b3b3b;\"></div>\n"
				+ "               </td>\n" + "          </tr>\n" + "     </table>\n" + "</body>\n" + "</html>";
		mWebView.loadDataWithBaseURL("file:///android_asset/", str2, "text/html", "utf-8", null);

		mWebView.setTag(new Object());
		mWebView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mWebView.setVisibility(View.VISIBLE);
			}
		}, 500);
	}


	public static int getBitmapMaxWidthAndMaxHeight() {
		// 2014-08-26 最大高度改小一点
        int[] maxSizeArray = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);

        if (maxSizeArray[0] == 0) {
            GLES10.glGetIntegerv(GL11.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);
        }
        
        if (maxSizeArray[0] > 0)
        	return maxSizeArray[0];
        
        return 1280;
    }
	
	public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }
	




    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
