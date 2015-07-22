/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.ui.timeline;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;

import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.support.design.widget.TabLayout;
import android.widget.TextView;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.StatusTimeUtils;
import com.dudutech.weibo.Utils.Utility;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.ui.comments.StatusCommentFragment;
import com.dudutech.weibo.widget.FlowLayout;
import com.dudutech.weibo.widget.LetterImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WeiboDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    public static  final  String EXT_WEIBO="eta_weibo";

    @InjectView(R.id.tv_time)
    public TextView tv_time;
    @InjectView(R.id.tv_username)
    public TextView tv_username;
    @InjectView(R.id.tv_from)
    public TextView tv_from;
    @InjectView(R.id.tv_content)
    public TextView tv_content;
    @InjectView(R.id.ll_comment)
    public LinearLayout ll_comment;
    @InjectView(R.id.ll_like)
    public LinearLayout ll_like;
    @InjectView(R.id.ll_repost)
    public LinearLayout ll_repost;
    @InjectView(R.id.fl_images)
    public FlowLayout fl_images;
    @InjectView(R.id.iv_avatar)
    public LetterImageView iv_avatar;

    @InjectView(R.id.tv_comment_count)
    public  TextView tv_comment_count;
    @InjectView(R.id.tv_repost_count)
    public  TextView tv_repost_count;
    @InjectView(R.id.tv_like_count)
    public  TextView tv_like_count;
    @InjectView(R.id.tabs)
    public TabLayout tabLayout;
    @InjectView(R.id.viewpager)
    ViewPager mViewPager ;
//    @InjectView(R.id.collapsing_toolbar)
//    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;

    StatusCommentFragment mStatusCommentFragment;

    StatusRepostFragment mStatusRepostFragment;


    public  MessageModel mWeibo;


    public  static  void start(Context context ,MessageModel  messageModel){
        Intent  intent= new Intent(context,WeiboDetailActivity.class);
        intent.putExtra(EXT_WEIBO,messageModel);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_detail);
        ButterKnife.inject(this);
        if(savedInstanceState!=null){
            mWeibo= savedInstanceState.getParcelable(EXT_WEIBO);
        }
        else{
            mWeibo=getIntent().getParcelableExtra(EXT_WEIBO);

        }

        appbar.addOnOffsetChangedListener(this);
        initWeibo(mWeibo);

        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        StatusCommentFragment mStatusCommentFragment= StatusCommentFragment.getInstance(mWeibo.id);
//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fl_content, mStatusCommentFragment)
//                .commit();



    }
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getFragmentManager());
         mStatusCommentFragment= StatusCommentFragment.newInstance(mWeibo.id);
         mStatusRepostFragment= StatusRepostFragment.newInstance(mWeibo.id);
        adapter.addFragment( mStatusCommentFragment, "comment");
        adapter.addFragment(mStatusRepostFragment, "repost");
        viewPager.setAdapter(adapter);
    }

    private void initWeibo(final  MessageModel msg){

       tv_content.setText(msg.text);
      tv_username.setText(msg.user.name);
        String url = msg.user.avatar_large;
        iv_avatar.setOval(true);
        iv_avatar.setLetter(msg.user.name.charAt(0));
        if(!url.equals(iv_avatar.getTag())) {
           iv_avatar.setTag(url);
            ImageLoader.getInstance().displayImage(url, iv_avatar, Constants.avatarOptions);
        }

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserHomeActivity.startUserHomeActivity(WeiboDetailActivity.this
                        ,msg.user);
            }
        });


        if (!msg.source.isEmpty()) {
            tv_from.setText(Html.fromHtml(msg.source));
        }

       tv_time.setText(StatusTimeUtils.instance(this).buildTimeString(msg.created_at));
        tv_comment_count.setText(Utility.getCountString(msg.comments_count));
       tv_like_count.setText("  " + Utility.getCountString(msg.attitudes_count));
        tv_repost_count.setText(Utility.getCountString(msg.reposts_count));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weibo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        boolean enable=i==0;
        if(mViewPager.getCurrentItem()==0){
            mStatusCommentFragment.setSwipeRefreshEnable(enable);
        }
        else {
            mStatusRepostFragment.setSwipeRefreshEnable(enable);
        }

    }

    static class Adapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public Adapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
}
