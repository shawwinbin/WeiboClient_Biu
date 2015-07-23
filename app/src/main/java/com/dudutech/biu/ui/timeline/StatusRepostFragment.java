/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.ui.timeline;

import android.os.Bundle;

import com.dudutech.biu.adapter.timeline.BaseTimelinAdapter;
import com.dudutech.biu.adapter.timeline.StatusRepostAdapter;
import com.dudutech.biu.dao.timeline.ITimelineBaseDao;
import com.dudutech.biu.dao.timeline.StatusRepostDao;
import com.dudutech.biu.model.RepostListModel;

/**
 *  weibo detail comments
 * Created by shaw on 2015/7/11.
 */
public class StatusRepostFragment extends AbsTimeLineFragment {

    private static final String ARG_STATUS_ID = "arg_status_id";

    private long mId;

    public static StatusRepostFragment newInstance( long id) {
        StatusRepostFragment fragment = new StatusRepostFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_STATUS_ID, id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getLong(ARG_STATUS_ID);
        }
        else {
            mId=savedInstanceState.getLong(ARG_STATUS_ID);
        }
    }

    @Override
    protected ITimelineBaseDao bindDao() {
        return new StatusRepostDao(getActivity(),mId);
    }


    @Override
    protected BaseTimelinAdapter bindListAdapter() {
        StatusRepostAdapter adapter=new StatusRepostAdapter(getActivity(),(RepostListModel) mDao.getList());
        adapter.setBottomCount(1);
        return adapter;
    }
}
