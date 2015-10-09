/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.ui.timeline;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.shaweibo.biu.adapter.timeline.BaseTimelinAdapter;
import com.shaweibo.biu.adapter.timeline.TimelineAdapter;
import com.shaweibo.biu.dao.favo.FavoTimeLineDao;
import com.shaweibo.biu.dao.timeline.StatusTimeLineDao;
import com.shaweibo.biu.model.MessageListModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoTimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoTimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoTimelineFragment extends AbsTimeLineFragment  implements TimelineAdapter.OnClickListener {

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static FavoTimelineFragment newInstance() {
        FavoTimelineFragment fragment = new FavoTimelineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FavoTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected StatusTimeLineDao bindDao() {

      return new FavoTimeLineDao(getActivity());
    }

    @Override
    protected BaseTimelinAdapter bindListAdapter() {
        TimelineAdapter adapter = new TimelineAdapter(getActivity(), (MessageListModel) mDao.getList()
        );
        adapter.setBottomCount(1);
        adapter.setOnClickListenner(this);

        return adapter;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        mListener = null;
    }

    @Override
    public void onTtemClick(View view, int position) {
        StatusDetailActivity.start(getActivity(), ((MessageListModel) mDao.getList()).get(position));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
