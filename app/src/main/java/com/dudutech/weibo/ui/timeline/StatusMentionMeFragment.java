/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.ui.timeline;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import com.dudutech.weibo.adapter.timeline.BaseTimelinAdapter;
import com.dudutech.weibo.adapter.timeline.TimelineAdapter;
import com.dudutech.weibo.dao.timeline.ITimelineBaseDao;
import com.dudutech.weibo.dao.timeline.StatusMentionMeDao;
import com.dudutech.weibo.model.MessageListModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatusMentionMeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatusMentionMeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusMentionMeFragment extends AbsTimeLineFragment {

//    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static StatusMentionMeFragment newInstance() {
        StatusMentionMeFragment fragment = new StatusMentionMeFragment();

        return fragment;
    }

    public StatusMentionMeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected ITimelineBaseDao bindDao() {
        return new StatusMentionMeDao(getActivity());
    }

    @Override
    protected BaseTimelinAdapter bindListAdapter() {
        return new TimelineAdapter(getActivity(), (MessageListModel) mCache.getList());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
