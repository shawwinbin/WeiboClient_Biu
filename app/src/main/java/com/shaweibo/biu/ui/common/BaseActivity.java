package com.shaweibo.biu.ui.common;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.shaweibo.biu.R;
import com.shaweibo.biu.Utils.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;


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
////
//		setStatusBar();
    }


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		
	}
	protected int getActionBarSize() {
		TypedValue typedValue = new TypedValue();
		int[] textSizeAttr = new int[]{R.attr.actionBarSize};
		int indexOfAttrTextSize = 0;
		TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
		int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
		a.recycle();
		return actionBarSize;
	}


}
