package com.dudutech.biu.ui.friendship;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dudutech.biu.R;
import com.dudutech.biu.adapter.timeline.BaseTimelinAdapter;
import com.dudutech.biu.dao.relationship.AbUserListDao;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract  class  AbUserListFragment extends Fragment {


    @InjectView(R.id.user_list)
    protected RecyclerView mList;

    AbUserListDao mDao ;
    private LinearLayoutManager mManager;
    protected BaseTimelinAdapter mAdapter;

    private boolean mLoadding = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_friends_list, null);

        ButterKnife.inject(this,v);

        mDao = bindDao();

        mList.setDrawingCacheEnabled(true);
        mList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mList.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE
                | ViewGroup.PERSISTENT_SCROLLING_CACHE);
        mManager = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(mManager);

        mDao.loadFromCache();
        if (mDao.getList().getSize() == 0) {
            new Refresher().execute(true);
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
                        new AbUserListFragment.Refresher().execute(false);

                    }
                }

            }
        });



        return v;
    }


    protected abstract AbUserListDao bindDao();

    protected abstract BaseTimelinAdapter bindListAdapter();

    private class Refresher extends AsyncTask<Boolean, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mLoadding = true;
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
              mDao.load(params[0]);
            return params[0];
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                mList.smoothScrollToPosition(0);
            }
            mLoadding = false;
            mAdapter.setBottomStatus(mDao.getStatus());
            mAdapter.notifyDataSetChanged();

        }

    }
}
