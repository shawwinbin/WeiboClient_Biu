package com.shaweibo.biu.ui.friendship;

import android.os.Bundle;
import android.view.View;

import com.shaweibo.biu.adapter.friends.UserAdapter;
import com.shaweibo.biu.adapter.timeline.BaseTimelinAdapter;
import com.shaweibo.biu.dao.relationship.AbUserListDao;
import com.shaweibo.biu.dao.relationship.FollowerListDao;
import com.shaweibo.biu.model.UserListModel;
import com.shaweibo.biu.model.UserModel;
import com.shaweibo.biu.ui.timeline.UserHomeActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class FollowersFragment extends AbUserListFragment implements UserAdapter.OnItemClickListener {


    private static final String ARG_USER_ID = "arg_user_id";
    private String mUid;
    public static FollowersFragment newInstance( String  uid) {
        FollowersFragment fragment = new FollowersFragment();
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
        return new FollowerListDao(getActivity(),mUid);
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
        UserHomeActivity.start(getActivity(), (UserModel) mDao.getList().get(position));
    }
}
