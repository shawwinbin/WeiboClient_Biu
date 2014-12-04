package com.dudutech.weibo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;




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
public abstract class BaseActivity extends Activity {

	//滑动返回
//	private SwipeBackLayout mSwipeBackLayout;
	private ImageButton btn_back;
	private TextView tv_title;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
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


}
