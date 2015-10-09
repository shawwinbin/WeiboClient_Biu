/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.ui.timeline;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaweibo.biu.R;
import com.shaweibo.biu.Utils.Settings;
import com.shaweibo.biu.adapter.comments.DividerItemDecoration;
import com.shaweibo.biu.adapter.timeline.BaseTimelinAdapter;
import com.shaweibo.biu.dao.timeline.ITimelineBaseDao;
import com.shaweibo.biu.ui.common.BaseFragment;
import com.shaweibo.biu.ui.main.MainActivity;
import com.shaweibo.biu.ui.common.StatusContextMenuManager;


import butterknife.ButterKnife;
import butterknife.InjectView;



public abstract class AbsTimeLineFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, MainActivity.MainRefresh {
    private static final String TAG = AbsTimeLineFragment.class.getSimpleName();

    @InjectView(R.id.home_timeline)
    protected RecyclerView mList;
    private BaseTimelinAdapter mAdapter;
    private LinearLayoutManager mManager;
    protected ITimelineBaseDao mDao;
    private Settings mSettings;
    // Pull To Refresh
    protected SwipeRefreshLayout mSwipeRefresh;

    private boolean mRefreshing = false;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSettings = Settings.getInstance(getActivity().getApplicationContext());
        final View v = inflater.inflate(R.layout.fragment_timeline, null);
        ButterKnife.inject(this, v);
        mDao = bindDao();
        mDao.loadFromCache();
        mList.setDrawingCacheEnabled(true);
        mList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mList.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE
                | ViewGroup.PERSISTENT_SCROLLING_CACHE);
        mList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        mManager = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(mManager);
        // Swipe To Refresh
        bindSwipeToRefresh(getActivity(),(ViewGroup) v);
        if (isFirstCreate) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onRefresh();
                }
            }, 500);
        }



        mAdapter= bindListAdapter();
        mList.setAdapter(mAdapter);


        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = mManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = mManager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1)) {
                        //加载更多功能的代码
                        new AbsTimeLineFragment.Refresher().execute(false);

                    }
                }

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                StatusContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }

        });




        return v;
    }


    @Override
    public void doRefresh() {
        mList.scrollToPosition(0);
        mList.post(new Runnable() {
            @Override
            public void run() {

                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        if (!mRefreshing) {
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setRefreshing(true);
            }
            new Refresher().execute(true);
        }
    }




    protected void bindSwipeToRefresh(Context context,ViewGroup v) {
        mSwipeRefresh = new SwipeRefreshLayout(context);

        // Move child to SwipeRefreshLayout, and add SwipeRefreshLayout to root
        // view
        v.removeViewInLayout(mList);
        v.addView(mSwipeRefresh, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeRefresh.addView(mList, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.ptr_green, R.color.ptr_orange,
                R.color.ptr_red, R.color.ptr_blue);
    }


    protected void load(boolean param) {
        mDao.load(param);
        mDao.cache();
    }

    private class Refresher extends AsyncTask<Boolean, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRefreshing = true;
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            load(params[0]);
            return params[0];
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

//            if (result) {
//                mList.smoothScrollToPosition(0);
//            }
            mAdapter.setBottomStatus(mDao.getStatus());
            mAdapter.notifyDataSetChangedAfeterReadSetting();
            mRefreshing = false;
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setRefreshing(false);
            }


        }

    }
    public void setSwipeRefreshEnable(boolean isEnable){

        if(mSwipeRefresh==null){
            return;
        }
        mSwipeRefresh.setEnabled(isEnable);

    }

    protected abstract ITimelineBaseDao bindDao();

    protected abstract BaseTimelinAdapter  bindListAdapter();

}
