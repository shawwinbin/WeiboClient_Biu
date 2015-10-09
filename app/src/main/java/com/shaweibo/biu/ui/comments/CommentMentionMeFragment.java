/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.ui.comments;

import android.os.Bundle;

import com.shaweibo.biu.adapter.timeline.BaseTimelinAdapter;
import com.shaweibo.biu.adapter.comments.CommentMeAdapter;
import com.shaweibo.biu.dao.comments.CommentsMentionMeDao;
import com.shaweibo.biu.dao.timeline.ITimelineBaseDao;
import com.shaweibo.biu.model.CommentListModel;
import com.shaweibo.biu.ui.timeline.AbsTimeLineFragment;


public class CommentMentionMeFragment extends AbsTimeLineFragment {
//    private OnFragmentInteractionListener mListener;


    public static CommentMentionMeFragment newInstance() {
        CommentMentionMeFragment fragment = new CommentMentionMeFragment();
        return fragment;
    }

    public CommentMentionMeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected ITimelineBaseDao bindDao() {
        return new CommentsMentionMeDao(getActivity());
    }

    @Override
    protected BaseTimelinAdapter bindListAdapter() {
        return new CommentMeAdapter(getActivity(), (CommentListModel) mDao.getList());
    }
}



