/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.ui.timeline;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dudutech.weibo.adapter.timeline.BaseTimelinAdapter;
import com.dudutech.weibo.adapter.timeline.StatusComentAdapter;
import com.dudutech.weibo.dao.timeline.ITimelineBaseDao;
import com.dudutech.weibo.dao.timeline.StatusCommentDao;
import com.dudutech.weibo.dao.timeline.StatusTimeLineDao;
import com.dudutech.weibo.model.CommentListModel;

/**
 *  weibo detail comments
 * Created by shaw on 2015/7/11.
 */
public class StatusCommentFragment extends AbsTimeLineFragment {

    private static final String ARG_STATUS_ID = "arg_status_id";

    private long mId;

    public static StatusCommentFragment newInstance( long id) {
        StatusCommentFragment fragment = new StatusCommentFragment();
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
        return new StatusCommentDao(getActivity(),mId);
    }


    @Override
    protected BaseTimelinAdapter bindListAdapter() {
        StatusComentAdapter adapter=new StatusComentAdapter(getActivity(),(CommentListModel)mCache.getList());
        adapter.setBottomCount(1);
        return adapter;
    }
}
