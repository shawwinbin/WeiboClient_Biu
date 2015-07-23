package com.dudutech.biu.ui.main;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.SystemBarUtils;

import com.dudutech.biu.dao.login.LoginDao;
import com.dudutech.biu.dao.user.UserDao;
import com.dudutech.biu.model.UserModel;
import com.dudutech.biu.ui.comments.CommentMeFragment;
import com.dudutech.biu.ui.common.BaseActivity;
import com.dudutech.biu.ui.post.NewPostActivity;
import com.dudutech.biu.ui.timeline.FavoTimelineFragment;
import com.dudutech.biu.ui.timeline.HomeTimelineFragment;
import com.dudutech.biu.ui.timeline.MentionMeFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks ,View.OnClickListener{



	public final static  String TAG = "MainActivity";


	@InjectView(R.id.toolbar)
	Toolbar toolbar;
	@InjectView(R.id.fab)
	FloatingActionButton  fab;

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;
	private ActionBarHelper mActionBar;
	private UserDao mUserCache;
	private UserModel mUser;
	private LoginDao mLoginCache;

	public final static  String FRG_TAG_PRE_SUFIX = "main_frg_";

	public final static  String FRG_TAG_MENTION_ME = FRG_TAG_PRE_SUFIX+"mention_me";

	public final static  String FRG_TAG_COMMENT = FRG_TAG_PRE_SUFIX+"comment";
	public final static  String FRG_TAG_FAVO = FRG_TAG_PRE_SUFIX+"favo";
	public   String mCurrentPositon = "";


	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id){
			case R.id.fab:

				break;
		}

	}
	public static interface Refresher {
        void doRefresh();
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		mNavigationDrawerFragment = new NavigationDrawerFragment();
		setSupportActionBar(toolbar);
		mActionBar = new ActionBarHelper();
		mActionBar.init();
		initStatusBar();
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.navigation_drawer, mNavigationDrawerFragment)
				.commit();
		setUpDrawer();
		mUserCache = new UserDao(this);
		mLoginCache = LoginDao.getInstance(this);

		new InitializerTask().execute();

		fab.setOnClickListener(this);
	}


	private void initStatusBar(){
		if (Build.VERSION.SDK_INT >= 19) {
			ViewGroup drawerRoot = (ViewGroup) findViewById(R.id.fl_drawer_root);
			drawerRoot.setPadding(drawerRoot.getPaddingLeft(),
					SystemBarUtils.getStatusBarHeight(this),
					drawerRoot.getPaddingRight(),
					drawerRoot.getBottom());
		}
		if (Build.VERSION.SDK_INT >= 19) {
			ViewGroup rootMain = (ViewGroup) findViewById(R.id.rl_main_root);
			rootMain.setPadding(rootMain.getPaddingLeft(),
					rootMain.getPaddingTop(),
					rootMain.getPaddingRight(),
					rootMain.getBottom() + SystemBarUtils.getNavigationBarHeight(this));
		}

	}

	private void setUpDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerLayout.setDrawerListener(new MyDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerToggle = new ActionBarDrawerToggle(this,
				mDrawerLayout,
				R.string.navigation_drawer_open,
				R.string.navigation_drawer_close
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position ,String groupId ,String title) {

		try {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		} catch (NullPointerException e) {

		}

		FragmentTransaction ft= getFragmentManager()
				.beginTransaction();
		Fragment lastFragment=getFragmentManager().findFragmentByTag(mCurrentPositon);
		Fragment currentFragment=null;

		switch (position){
			case NavigationDrawerFragment.MENU_WEIBO :
				mCurrentPositon=FRG_TAG_PRE_SUFIX+groupId;
				currentFragment=getFragmentManager().findFragmentByTag(mCurrentPositon);
				if(currentFragment==null) {
					currentFragment = HomeTimelineFragment.newInstance(groupId);
					ft.add(R.id.container, currentFragment, mCurrentPositon);
				}

				break;
			case NavigationDrawerFragment.MENU_MOMENTION :
				mCurrentPositon=FRG_TAG_MENTION_ME;
				currentFragment=getFragmentManager().findFragmentByTag(mCurrentPositon);
				if(currentFragment==null){
					currentFragment=MentionMeFragment.newInstance() ;
					ft.add(R.id.container, currentFragment, mCurrentPositon);

				}

				break;
			case NavigationDrawerFragment.MENU_COMMENT :
				mCurrentPositon=FRG_TAG_COMMENT;
				currentFragment=getFragmentManager().findFragmentByTag(mCurrentPositon);
				if(currentFragment==null){
					currentFragment= CommentMeFragment.newInstance() ;
					ft.add(R.id.container, currentFragment, mCurrentPositon);

				}
				break;
			case NavigationDrawerFragment.MENU_FAVO :
				mCurrentPositon=FRG_TAG_FAVO;
				currentFragment=getFragmentManager().findFragmentByTag(mCurrentPositon);
				if(currentFragment==null){
					currentFragment= FavoTimelineFragment.newInstance() ;
					ft.add(R.id.container, currentFragment, mCurrentPositon);

				}
				break;
		}


		if(lastFragment!=null){
			ft.hide(lastFragment);
		}
		if(currentFragment!=null) {
			ft.show(currentFragment);
			ft.commit();
		}
		mTitle=title;


	}



	public void restoreActionBar() {
		mActionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		if (!isDrawerOpen()) {
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		int id = item.getItemId();
		switch (id ){
			case R.id.action_settings :
				return true;
			case R.id.action_new_post :

				NewPostActivity.start(this);

				return true;
		}



		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}



	private class MyDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			mDrawerToggle.onDrawerOpened(drawerView);
			mActionBar.onDrawerOpened();
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mDrawerToggle.onDrawerClosed(drawerView);
			mActionBar.onDrawerClosed();
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	private class ActionBarHelper {
		private final ActionBar mActionBar;
		private CharSequence mDrawerTitle;
		private CharSequence mTitle;

		ActionBarHelper() {
			mActionBar = getSupportActionBar();
		}

		public void init() {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(false);
			mTitle = mDrawerTitle = getTitle();
		}

		public void onDrawerClosed() {
			mActionBar.setTitle(mTitle);
		}

		public void onDrawerOpened() {
			mActionBar.setTitle(mDrawerTitle);
		}

		public void setTitle(CharSequence title) {
			mTitle = title;
			mActionBar.setTitle(mTitle);
		}
	}

	private class InitializerTask extends AsyncTask<Void, Object, Void> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void[] params) {
			// Username first
			mUser = mUserCache.getUser(mLoginCache.getUid());


			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			if(mUser!=null){
				Log.i(TAG,mUser.getName());
				initNavigationFragment();
			}
		}
	}

	private void initNavigationFragment(){

		if(mNavigationDrawerFragment==null){
			return;
		}
		mNavigationDrawerFragment.setHeadView(mUser);

	}

	protected void newPost() {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
//		i.setClass(getActivity(), NewPostActivity.class);
		startActivity(i);
	}


}
