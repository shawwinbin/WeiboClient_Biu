/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.ui.comments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaweibo.biu.R;
import com.shaweibo.biu.adapter.common.MyFragmentPagerAdapter;
import com.shaweibo.biu.ui.common.BaseFragment;
import com.shaweibo.biu.ui.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *  mention me fragment include
 *  mention me in status and mention me in comments
 *
 */
public class CommentMeFragment extends BaseFragment implements MainActivity.MainRefresh{


    @InjectView(R.id.tabs)
    public TabLayout tabLayout;
    @InjectView(R.id.viewpager_comments)
    ViewPager mViewPager ;
//    private OnFragmentInteractionListener mListener;
    CommentToMeFragment mCommentToMeFragment;
    CommentByMeFragment mCommentByMeFragment;

    // TODO: Rename and change types and number of parameters
    public static CommentMeFragment newInstance() {
        CommentMeFragment fragment = new CommentMeFragment();
        return fragment;
    }
    public CommentMeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

    }



    private void setupViewPager(ViewPager viewPager) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager());
        mCommentToMeFragment= CommentToMeFragment.newInstance();
        mCommentByMeFragment= CommentByMeFragment.newInstance();
        adapter.addFragment(mCommentToMeFragment, getString(R.string.comments_to_me));
        adapter.addFragment(mCommentByMeFragment, getString(R.string.comments_by_me));
        viewPager.setAdapter(adapter);
    }


    @Override
    public void doRefresh() {
        if(mViewPager.getCurrentItem()==0){
            mCommentToMeFragment.doRefresh();
        }
        else{
            mCommentByMeFragment.doRefresh();
        }
    }



}
