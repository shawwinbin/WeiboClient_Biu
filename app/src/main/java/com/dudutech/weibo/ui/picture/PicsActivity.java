package com.dudutech.weibo.ui.picture;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.SystemBarUtils;
import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.ui.BaseActivity;

import javax.crypto.KeyGenerator;

import butterknife.InjectView;


public class PicsActivity extends BaseActivity implements OnPageChangeListener {

	public static void launch(Activity from, MessageModel bean, int index) {
		Intent intent  = new Intent(from, PicsActivity.class);
		intent.putExtra("bean", bean);
		intent.putExtra("index", index);
		from.startActivity(intent);
	}
	
	@InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView( R.id.layToolbar)
    ViewGroup layToolbar;
	
	private MessageModel mBean;
	private int index;

    MyViewPagerAdapter myViewPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//        AisenUtils.setPicStatusBar(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pics);
		
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mBean = savedInstanceState == null ? (MessageModel) getIntent().getSerializableExtra("bean")
										   : (MessageModel) savedInstanceState.getSerializable("bean");
		index = savedInstanceState == null ? getIntent().getIntExtra("index", 0)
										: savedInstanceState.getInt("index", 0);

        myViewPagerAdapter = new MyViewPagerAdapter(getFragmentManager());
		viewPager.setAdapter(myViewPagerAdapter);
		viewPager.setOnPageChangeListener(this);
		viewPager.setCurrentItem(index);
		if (size() > 1 && getSupportActionBar() != null)
            getSupportActionBar().setTitle(String.format("%d/%d", index + 1, size()));
		else if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(String.format("%d/%d", 1, 1));

//		getSupportActionBar()().setBackgroundColor(Color.TRANSPARENT);

        if (Build.VERSION.SDK_INT >= 19) {
            layToolbar.setPadding(layToolbar.getPaddingLeft(),
                                        layToolbar.getPaddingTop() + SystemBarUtils.getStatusBarHeight(this),
                                        layToolbar.getPaddingRight(),
                                        layToolbar.getPaddingBottom());
        }
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt("index", index);
		outState.putParcelable("bean", mBean);
	}
	
	private int size() {
		if (mBean.retweeted_status != null) {
			return mBean.retweeted_status.pic_urls.size();
		}
		
		return mBean.pic_urls.size();
	}
	
	private MessageModel.PictureUrl getPicture(int index) {
		if (mBean.retweeted_status  != null) {
			return mBean.retweeted_status .pic_urls.get(index);
		}
		
		return mBean.pic_urls.get(index);
	}

    public MessageModel.PictureUrl getCurrent() {
        return getPicture(viewPager.getCurrentItem());
    }
	
	protected Fragment newFragment(int position) {
		return PictureFragment.newInstance(getPicture(position));
	}
	
	class MyViewPagerAdapter extends FragmentPagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = getFragmentManager().findFragmentByTag(makeFragmentName(position));
			if (fragment == null) {
				fragment = newFragment(position);
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			return size();
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			
//			Fragment fragment = getFragmentManager().findFragmentByTag(makeFragmentName(position));
//			if (fragment != null)
//				mCurTransaction.remove(fragment);
		}

//		@Override
//		protected String makeFragmentName(int position) {
//			return KeyGenerator.generateMD5(getPicture(position).getThumbnail_pic());
//		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int index) {
		this.index = index;
		
        getSupportActionBar().setTitle(String.format("%d/%d", index + 1, size()));

        PictureFragment fragment = (PictureFragment) myViewPagerAdapter.getItem(index);
        if (fragment != null)
            fragment.onStripTabRequestData();
	}

//    @Override
//    protected int configTheme() {
//        return R.style.AppTheme_Pics;
//    }

}
