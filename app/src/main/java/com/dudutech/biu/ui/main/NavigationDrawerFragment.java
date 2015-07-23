package com.dudutech.biu.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dudutech.biu.R;
import com.dudutech.biu.adapter.common.GroupAdapter;
import com.dudutech.biu.dao.relationship.GroupDao;
import com.dudutech.biu.dao.timeline.StatusTimeLineDao;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.GroupModel;
import com.dudutech.biu.model.UserModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {

	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	private NavigationDrawerCallbacks mCallbacks;

	public static final int MENU_MOMENTION = 0;
	public static final int MENU_COMMENT = 1;
	public static final int MENU_WEIBO = 2;
	public static final int MENU_FAVO= 3;
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

	@InjectView(R.id.menu_favo)
	View menu_favo;

	@InjectView(R.id.menu_comment)
	View menu_comment;

	GroupDao mGroupDao;

	public String mTitleMentionMe;
	public String mTitleCommnet;

	public Vector<View> mMenuList ;

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
		mTitleMentionMe=getString(R.string.mention_me);
		mTitleCommnet=getString(R.string.comment);
		mMenuList=new Vector<View>();
		selectItem(mCurrentSelectedPosition, StatusTimeLineDao.GROUP_ALL,getString(R.string.groups_all),0,0);

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

		ButterKnife.inject(this, v);
		mGroupDao=new GroupDao(getActivity());
		mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						GroupModel group=mGroupDao.mListModel.get(position);
						selectItem(MENU_WEIBO,group.idstr,group.name,0,position);
					}
				});


		mGroupAdapter=new GroupAdapter(getActivity(),mGroupDao.mListModel);
		mDrawerListView.setAdapter(mGroupAdapter);
		menu_mention.setOnClickListener(this);
		menu_comment.setOnClickListener(this);
		menu_favo.setOnClickListener(this);
		mMenuList.add(menu_mention);
		mMenuList.add(menu_comment);
		mMenuList.add(menu_favo);
		new InitGroupsInTask().execute();
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	private void selectItem(int position ,String groupId,String title,int viewId ,int secondPosition) {
		mCurrentSelectedPosition = position;


		if (mGroupAdapter != null) {
				mGroupAdapter.setCurrentPosition(secondPosition);
			    mGroupAdapter.notifyDataSetChanged();
			}
		 toggle(viewId);

		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position,groupId, title);
		}
	}

	private void toggle(int viewId){
		for(View view : mMenuList){
			if(view.getId()==viewId){
				view.setSelected(true);
			}
			else{
				view.setSelected(false);
			}

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



	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
			case R.id.menu_mention :
				menu_mention.setSelected(true);
				selectItem(MENU_MOMENTION,"",mTitleMentionMe,id,-1);
				break;
			case R.id.menu_comment:
				selectItem(MENU_COMMENT,"",mTitleCommnet,id,-1);
				break;
			case R.id.menu_favo:
				selectItem(MENU_FAVO,"",getString(R.string.favo),id,-1);
				break;

		}

	}

	public static interface NavigationDrawerCallbacks {
		void onNavigationDrawerItemSelected(int position,String groupId,String title);
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
			mGroupDao.loadFromCache();

			if(mGroupDao.mListModel.getList().size()<3) {
				mGroupDao.load(false);
				mGroupDao.cache();
				mGroupDao.mListModel.addDefaultGroupsToTop();
			}



			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {


			mGroupAdapter.notifyDataSetChanged();

			super.onPostExecute(aVoid);


		}
	}

}
