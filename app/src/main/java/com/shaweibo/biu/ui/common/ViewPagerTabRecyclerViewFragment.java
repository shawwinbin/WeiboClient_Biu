/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shaweibo.biu.ui.common;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shaweibo.biu.R;
import com.shaweibo.biu.adapter.timeline.BaseTimelinAdapter;
import com.shaweibo.biu.dao.timeline.ITimelineBaseDao;
import com.shaweibo.biu.ui.main.MainActivity;
import com.shaweibo.biu.ui.timeline.StatusDetailActivity;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class ViewPagerTabRecyclerViewFragment extends FlexibleSpaceBaseFragment<ObservableRecyclerView> implements
        MainActivity.MainRefresh  {

    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    protected ITimelineBaseDao mDao;

    private BaseTimelinAdapter mAdapter;
    @InjectView(R.id.scroll)
    ObservableRecyclerView recyclerView;
    private boolean mRefreshing = false;
    int headViewHeight=0;
    View mView;
    private LinearLayoutManager mManager;
    Activity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.inject(this,mView);
        parentActivity = getActivity();
        mManager=new LinearLayoutManager(parentActivity);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(false);


        mDao=bindDao();
        mDao.loadFromCache();


        // TouchInterceptionViewGroup should be a parent view other than ViewPager.
        // This is a workaround for the issue #117:
        // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
        recyclerView.setTouchInterceptionViewGroup((ViewGroup) mView.findViewById(R.id.fragment_root));

        // Scroll to the specified offset after layout






        if (isFirstCreate) {
            new Refresher().execute(true);
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                View headerView = LayoutInflater.from(parentActivity).inflate(R.layout.padding, null);

                if (parentActivity instanceof HeaderViewObserve){
                    headViewHeight  =((HeaderViewObserve) parentActivity).getHeadViewHeight();
                    if(headViewHeight!=0){
                        headerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,headViewHeight));
                    }
                }
                mAdapter=bindListAdapter(headerView);
                recyclerView.setAdapter(mAdapter);

                Bundle args = getArguments();
                if (args != null && args.containsKey(ARG_SCROLL_Y)) {
                    final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
                    ScrollUtils.addOnGlobalLayoutListener(recyclerView, new Runnable() {
                        @Override
                        public void run() {
                            int offset = scrollY % headViewHeight;
                            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                            if (lm != null && lm instanceof LinearLayoutManager) {
                                ((LinearLayoutManager) lm).scrollToPositionWithOffset(0, -offset);
                            }
                        }
                    });
                    updateFlexibleSpace(scrollY, mView);
                } else {
                    updateFlexibleSpace(0, mView);
                }


            }
        });
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    int lastVisibleItem = mManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = mManager.getItemCount();

                    if (lastVisibleItem == (totalItemCount - 1)) {
                        new Refresher().execute(false);

                    }
                }

            }
        });


        return mView;
    }


    @Override
    public void doRefresh() {
        recyclerView.scrollToPosition(0);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (!mRefreshing) {
                    new Refresher().execute(true);
                }
            }
        });
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
            mAdapter.notifyDataSetChanged();
            mRefreshing = false;
//            if (mSwipeRefresh != null) {
//                mSwipeRefresh.setRefreshing(false);
//            }
        }

    }

    protected abstract ITimelineBaseDao bindDao();

    protected abstract BaseTimelinAdapter  bindListAdapter(View headView);

    public interface HeaderViewObserve{

        public int getHeadViewHeight();
    }


    @Override
    public void setScrollY(int scrollY, int threshold) {
        View view = getView();
        if (view == null) {
            return;
        }
        ObservableRecyclerView recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
        if (recyclerView == null) {
            return;
        }
        View firstVisibleChild = recyclerView.getChildAt(0);
        if (firstVisibleChild != null) {
            int offset = scrollY;
            int position = 0;
            if (threshold < scrollY) {
                int baseHeight = firstVisibleChild.getHeight();
                position = scrollY / baseHeight;
                offset = scrollY % baseHeight;
            }
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if (lm != null && lm instanceof LinearLayoutManager) {
                ((LinearLayoutManager) lm).scrollToPositionWithOffset(position, -offset);
            }
        }
    }

    @Override
    public void updateFlexibleSpace(int scrollY, View view) {
//        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);

        View recyclerViewBackground = view.findViewById(R.id.list_background);

        // Translate list background
        ViewHelper.setTranslationY(recyclerViewBackground, Math.max(0, -scrollY + headViewHeight));

        // Also pass this event to parent Activity
        StatusDetailActivity parentActivity =
                (StatusDetailActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.onScrollChanged(scrollY, (ObservableRecyclerView) view.findViewById(R.id.scroll));
        }
    }
}
