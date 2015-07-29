/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.ui.timeline;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.View;

import com.dudutech.biu.adapter.timeline.BaseTimelinAdapter;
import com.dudutech.biu.adapter.timeline.TimelineAdapter;
import com.dudutech.biu.dao.timeline.StatusTimeLineDao;
import com.dudutech.biu.model.MessageListModel;
import com.dudutech.biu.ui.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeTimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeTimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeTimelineFragment extends AbsTimeLineFragment  implements TimelineAdapter.OnClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GROUP_Id = "arg_group_id";
    // TODO: Rename and change types of parameters
    private String mGroupId;
    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static HomeTimelineFragment newInstance(String mGroupId) {
        HomeTimelineFragment fragment = new HomeTimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GROUP_Id, mGroupId);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroupId = getArguments().getString(ARG_GROUP_Id);
        }
    }


    @Override
    protected StatusTimeLineDao bindDao() {

      return new StatusTimeLineDao(getActivity(),mGroupId);
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
