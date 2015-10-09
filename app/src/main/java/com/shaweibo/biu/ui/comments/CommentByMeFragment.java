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
import com.shaweibo.biu.dao.comments.CommentsByMeDao;
import com.shaweibo.biu.dao.timeline.ITimelineBaseDao;
import com.shaweibo.biu.model.CommentListModel;
import com.shaweibo.biu.ui.timeline.AbsTimeLineFragment;


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
        return new CommentMeAdapter(getActivity(), (CommentListModel) mDao.getList());
    }
}



