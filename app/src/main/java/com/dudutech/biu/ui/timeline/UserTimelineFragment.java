/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.ui.timeline;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.dudutech.biu.dao.timeline.StatusTimeLineDao;
import com.dudutech.biu.dao.timeline.FriendTimeLineDao;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserTimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserTimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserTimelineFragment extends HomeTimelineFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_UID = "agr_uid";


    // TODO: Rename and change types of parameters

    private String mUid;
    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static UserTimelineFragment newInstance(String uid) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_UID, uid);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUid = getArguments().getString(ARG_UID);
        }

    }

//    @Override
//    protected void bindSwipeToRefresh(Context context,ViewGroup v) {
//        mSwipeRefresh = new SwipeRefreshLayout(context);
//
//        ViewGroup rootView= (ViewGroup) getActivity().findViewById(R.id.main_content);
//        // Move child to SwipeRefreshLayout, and add SwipeRefreshLayout to root
//        // view
////        v.removeViewInLayout(mList);
//        rootView.addView(mSwipeRefresh, ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
////        mSwipeRefresh.addView(mList, ViewGroup.LayoutParams.MATCH_PARENT,
////                ViewGroup.LayoutParams.MATCH_PARENT);
//        mSwipeRefresh.canChildScrollUp();
//        mSwipeRefresh.setOnRefreshListener(this);
//        mSwipeRefresh.setColorSchemeResources(R.color.ptr_green, R.color.ptr_orange,
//                R.color.ptr_red, R.color.ptr_blue);
//    }

    @Override
    protected StatusTimeLineDao bindDao() {

      return new FriendTimeLineDao(getActivity(),mUid);
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
