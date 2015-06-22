package com.dudutech.weibo.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.SystemBarTintManager;


/**
 *     
 * 项目名称：duduslim
 * 类名称：BaseSwipeBackActivity    
 * 类描述：    活动返回ACTIVity基类
 * 创建人：shaw  
 * 创建时间：2013-10-11 下午2:26:07    
 * 修改人：shaw  
 * 修改时间：2013-10-11 下午2:26:07    
 * 修改备注：    
 * @version     
 *     
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends AppCompatActivity {

	//滑动返回
//	private SwipeBackLayout mSwipeBackLayout;

	SystemBarTintManager tintManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//
//		setStatusBar();
    }


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickAgent.onPause(this);
		
	}


	@SuppressLint("ResourceAsColor")
	protected void setStatusBar() {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setStatusBarTintResource(R.color.base_actionbar);
//		SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//		listViewDrawer.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());

	}
	@TargetApi(19)

	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

}
