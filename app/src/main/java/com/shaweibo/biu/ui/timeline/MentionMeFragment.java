/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.ui.timeline;

import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaweibo.biu.R;
import com.shaweibo.biu.adapter.common.MyFragmentPagerAdapter;
import com.shaweibo.biu.ui.comments.CommentMentionMeFragment;
import com.shaweibo.biu.ui.common.BaseFragment;
import com.shaweibo.biu.ui.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *  mention me fragment include
 *  mention me in status and mention me in comments
 *
 */
public class MentionMeFragment extends BaseFragment implements MainActivity.MainRefresh {


    @InjectView(R.id.tabs)
    public TabLayout tabLayout;
    @InjectView(R.id.viewpager)
    ViewPager mViewPager ;
//    private OnFragmentInteractionListener mListener;
    StatusMentionMeFragment mStatusMetionMefragment;
    CommentMentionMeFragment mCommentMentionMeFragment;

    // TODO: Rename and change types and number of parameters
    public static MentionMeFragment newInstance() {
        MentionMeFragment fragment = new MentionMeFragment();
        return fragment;
    }
    public MentionMeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mention_me, container, false);
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
        mStatusMetionMefragment= StatusMentionMeFragment.newInstance();
        mCommentMentionMeFragment= CommentMentionMeFragment.newInstance();
        adapter.addFragment( mStatusMetionMefragment, getString(R.string.weibo));
        adapter.addFragment(mCommentMentionMeFragment, getString(R.string.comment));
        viewPager.setAdapter(adapter);
    }





    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void doRefresh() {
        if(mViewPager.getCurrentItem()==0){
            mStatusMetionMefragment.doRefresh();
        }
        else{
            mCommentMentionMeFragment.doRefresh();
        }
    }




}
