package com.dudutech.weibo.ui.friendship;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dudutech.weibo.R;
import com.dudutech.weibo.adapter.friends.UserAdapter;
import com.dudutech.weibo.adapter.timeline.BaseTimelinAdapter;
import com.dudutech.weibo.dao.relationship.AbUserListDao;
import com.dudutech.weibo.dao.relationship.FriendListDao;
import com.dudutech.weibo.model.UserListModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendListFragment extends AbUserListFragment implements UserAdapter.OnItemClickListener {


    private static final String ARG_USER_ID = "arg_user_id";
    private String mUid;
    public static FriendListFragment newInstance( String  uid) {
        FriendListFragment fragment = new FriendListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, uid);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUid = getArguments().getString(ARG_USER_ID);
        }
        else {
            mUid=savedInstanceState.getString(ARG_USER_ID);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected AbUserListDao bindDao() {
        return new FriendListDao(getActivity(),mUid);
    }

    @Override
    protected BaseTimelinAdapter bindListAdapter() {
        UserAdapter adapter=new UserAdapter(getActivity(), (UserListModel) mDao.getList());
        adapter.setOnItemClickListener(this);

        return adapter ;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_USER_ID,mUid);
    }

    @Override
    public void onTtemClick(View view, int position) {
        Intent intent = new Intent();
        intent.putExtra("name", "@" + ((UserListModel)mDao.getList()).get(position).getName() + " ");
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
