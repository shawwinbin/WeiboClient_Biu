package com.dudutech.weibo.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dudutech.weibo.R;
import com.dudutech.weibo.adapter.common.GroupAdapter;
import com.dudutech.weibo.dao.relationship.GroupDao;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.UserModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {

	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	private NavigationDrawerCallbacks mCallbacks;

	public static final int MENU_MOMENTION = 0;
	public static final int MENU_COMMENT = 1;
	public static final int MENU_WEIBO = 2;
	public int mCurrentSelectedPosition = MENU_WEIBO;



	@InjectView(R.id.tv_name)
	TextView tv_name;
	@InjectView(R.id.iv_user_avatar)
	ImageView iv_user_avatar;
	@InjectView(R.id.iv_user_bg)
	ImageView iv_user_bgs;
	@InjectView(R.id.drawer_list)
	ListView mDrawerListView;
	@InjectView(R.id.menu_mention)
	View menu_mention;

	GroupDao mGroupDao;

	public GroupAdapter mGroupAdapter;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
		}

		selectItem(mCurrentSelectedPosition,"");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_drawer,container, false);

		ButterKnife.inject(this,v);
		mGroupDao=new GroupDao();

		mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						selectItem(position, "");
					}
				});


		mGroupAdapter=new GroupAdapter(getActivity(),mGroupDao.mModel);

//		mDrawerListView.setAdapter(new ArrayAdapter<String>(getActionBar()
//				.getThemedContext(),
//				android.R.layout.simple_list_item_activated_1,
//				android.R.id.text1, new String[]{
//				getString(R.string.title_section1),
//				getString(R.string.title_section2),
//				getString(R.string.title_section3),}));
//		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

		menu_mention.setOnClickListener(this);

		new InitGroupsInTask().execute();

		return v;
	}

	private void selectItem(int position ,String groupId) {
		mCurrentSelectedPosition = position;
//		if (mDrawerListView != null) {
//			mDrawerListView.setItemChecked(position, true);
//		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position,groupId);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	private ActionBar getActionBar() {
		return ((AppCompatActivity) getActivity()).getSupportActionBar();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
			case R.id.menu_mention :
				menu_mention.setSelected(true);
				selectItem(MENU_MOMENTION,"");
				break;
			case R.id.menu_comment:
				selectItem(MENU_COMMENT,"");
				break;


		}

	}

	public static interface NavigationDrawerCallbacks {
		void onNavigationDrawerItemSelected(int position,String groupId);
	}

	public void setHeadView(UserModel user){
		if(user==null){
			return;
		}
		tv_name.setText(user.getName());

		ImageLoader.getInstance().displayImage(user.avatar_large, iv_user_avatar,Constants.avatarOptions);
		ImageLoader.getInstance().displayImage(user.cover_image_phone, iv_user_bgs,Constants.timelineListOptions);

	}

	private class InitGroupsInTask extends AsyncTask<Void, Object, Void> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void[] params) {
			// Username first
			mGroupDao.getGroups();


			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {

			mGroupAdapter=new GroupAdapter(getActivity(),mGroupDao.mModel);

			mDrawerListView.setAdapter(mGroupAdapter);
			mGroupAdapter.notifyDataSetChanged();

			super.onPostExecute(aVoid);


		}
	}

}
