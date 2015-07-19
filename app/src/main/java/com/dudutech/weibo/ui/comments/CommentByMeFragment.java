/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.ui.comments;

import android.os.Bundle;

import com.dudutech.weibo.adapter.timeline.BaseTimelinAdapter;
import com.dudutech.weibo.adapter.timeline.CommentMeAdapter;
import com.dudutech.weibo.dao.comments.CommentsByMeDao;
import com.dudutech.weibo.dao.comments.CommentsToMeDao;
import com.dudutech.weibo.dao.timeline.ITimelineBaseDao;
import com.dudutech.weibo.model.CommentListModel;
import com.dudutech.weibo.ui.timeline.AbsTimeLineFragment;


public class CommentByMeFragment extends AbsTimeLineFragment {
//    private OnFragmentInteractionListener mListener;


    public static CommentByMeFragment newInstance() {
        CommentByMeFragment fragment = new CommentByMeFragment();
        return fragment;
    }

    public CommentByMeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected ITimelineBaseDao bindDao() {
        return new CommentsByMeDao(getActivity());
    }

    @Override
    protected BaseTimelinAdapter bindListAdapter() {
        return new CommentMeAdapter(getActivity(), (CommentListModel) mCache.getList());
    }
}



