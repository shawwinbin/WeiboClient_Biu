/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dudutech.biu.ui.timeline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.DeviceUtil;
import com.dudutech.biu.Utils.SpannableStringUtils;
import com.dudutech.biu.Utils.StatusTimeUtils;
import com.dudutech.biu.Utils.Utility;
import com.dudutech.biu.adapter.comments.StatusComentAdapter;
import com.dudutech.biu.dao.favo.FavoDao;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.global.MyApplication;
import com.dudutech.biu.model.FavoModel;
import com.dudutech.biu.model.MessageModel;
import com.dudutech.biu.model.PicSize;
import com.dudutech.biu.ui.comments.StatusCommentFragment;
import com.dudutech.biu.ui.common.BaseActivity;
import com.dudutech.biu.ui.common.ViewPagerTabRecyclerViewFragment;
import com.dudutech.biu.ui.picture.PicsActivity;
import com.dudutech.biu.widget.FlowLayout;
import com.dudutech.biu.widget.SlidingTabLayout;
import com.dudutech.biu.widget.TagImageVIew;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is an example of ViewPager + SlidingTab + ListView/ScrollView.
 * This example shows how to handle scroll events for several different fragments.
 * <p/>
 * SlidingTabLayout and SlidingTabStrip are from google/iosched:
 * https://github.com/google/iosched
 */
public class StatusDetailActivity extends BaseActivity implements ViewPagerTabRecyclerViewFragment.HeaderViewObserve ,StatusComentAdapter.HeaderViewTouchListener {

    public static final String EXT_WEIBO = "eta_weibo";

    @InjectView(R.id.tv_time_source)
    public TextView tv_time_source;
    @InjectView(R.id.tv_username)
    public TextView tv_username;
    @InjectView(R.id.tv_content)
    public TextView tv_content;
    @InjectView(R.id.ll_like)
    public LinearLayout ll_like;
    @InjectView(R.id.fl_images)
    public FlowLayout fl_images;
    @InjectView(R.id.iv_avatar)
    public ImageView iv_avatar;

    @InjectView(R.id.fl_images_repost)
    public FlowLayout fl_images_repost;

    @InjectView(R.id.tv_like_count)
     TextView tv_like_count;

    @InjectView(R.id.pager)
     ViewPager mPager;
    @InjectView(R.id.rl_repost)
    View rl_repost;
//    @InjectView(R.id.header)
//    View mHeaderView;
    @InjectView(R.id.header_toolbar)
    View mToolbarView;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;

    @InjectView(R.id.tv_orignal_content)
    public TextView tv_orignal_content;

    private int mBaseTranslationY;
    private NavigationAdapter mPagerAdapter;
    public MessageModel mWeibo;
    private int photoMargin;
    private float imageMaxWidth;
    private int mFlexibleSpaceHeight=0;
    private int mTabHeight;

    public static void start(Context context, MessageModel messageModel) {
        Intent intent = new Intent(context, StatusDetailActivity.class);
        intent.putExtra(EXT_WEIBO, messageModel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);
        ButterKnife.inject(this);

        if (savedInstanceState != null) {
            mWeibo = savedInstanceState.getParcelable(EXT_WEIBO);
        } else {
            mWeibo = getIntent().getParcelableExtra(EXT_WEIBO);
        }
//        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWeibo.span= SpannableStringUtils.getSpan(this, mWeibo, true);
        photoMargin = getResources().getDimensionPixelSize(R.dimen.moment_photo_margin);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float padding = getResources().getDimension(R.dimen.LargePadding);
        imageMaxWidth = metrics.widthPixels - 2* padding;
        float smallPadding = getResources().getDimension(R.dimen.SmallPadding);

        initWeibo();

//        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));


        // When the page is selected, other fragments' scrollY should be adjusted
        // according to the toolbar status(shown/hidden)
        mSlidingTabLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("DETAIL","SLIDING TONCH");

                return false;
            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },500);
        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(), mWeibo);

        mPager.setAdapter(mPagerAdapter);

        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.white));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mPager);
        // Initialize the first Fragment's state when layout is completed.
        ScrollUtils.addOnGlobalLayoutListener(mSlidingTabLayout, new Runnable() {
            @Override
            public void run() {
                translateTab(0, false);
            }
        });


    }

    /**
     * Called by children Fragments when their scrollY are changed.
     * They all call this method even when they are inactive
     * but this Activity should listen only the active child,
     * so each Fragments will pass themselves for Activity to check if they are active.
     *
     * @param scrollY scroll position of Scrollable
     * @param s       caller Scrollable view
     */
    public void onScrollChanged(int scrollY, Scrollable s) {

        if(mFlexibleSpaceHeight==0){
            mFlexibleSpaceHeight=getHeadViewHeight();
        }

        Fragment fragment =
                getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }
        Scrollable scrollable = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollable == null) {
            return;
        }
        if (scrollable == s) {
            // This method is called by not only the current fragment but also other fragments
            // when their scrollY is changed.
            // So we need to check the caller(S) is the current fragment.
            int adjustedScrollY = Math.min(scrollY, mFlexibleSpaceHeight - mTabHeight);
            translateTab(adjustedScrollY, false);
            propagateScroll(adjustedScrollY);
        }
    }

    private void propagateScroll(int scrollY) {


        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(scrollY);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            ViewPagerTabRecyclerViewFragment f =
                    (ViewPagerTabRecyclerViewFragment) mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            f.setScrollY(scrollY, mFlexibleSpaceHeight);
            f.updateFlexibleSpace(scrollY);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weibo_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_favo:
                new FavoTask().execute();
                break;
            case R.id.action_copy:
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }


        return super.onOptionsItemSelected(item);
    }


    private void translateTab(int scrollY, boolean animated) {
        int flexibleSpaceImageHeight =getHeadViewHeight();
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        View imageView = findViewById(R.id.header_toolbar);
        View overlayView = findViewById(R.id.overlay);

        overlayView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,flexibleSpaceImageHeight));

        // Translate overlay and image
        float flexibleRange = flexibleSpaceImageHeight - getActionBarSize();
        int minOverlayTransitionY = tabHeight - overlayView.getHeight();
        ViewHelper.setTranslationY(overlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(imageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(overlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
//        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY - tabHeight) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
//        setPivotXToTitle(titleView);
//        ViewHelper.setPivotY(titleView, 0);
//        ViewHelper.setScaleX(titleView, scale);
//        ViewHelper.setScaleY(titleView, scale);

        // Translate title text
//        int maxTitleTranslationY = flexibleSpaceImageHeight - tabHeight - getActionBarSize();
//        int titleTranslationY = maxTitleTranslationY - scrollY;
//        ViewHelper.setTranslationY(titleView, titleTranslationY);

        // If tabs are moving, cancel it to start a new animation.
        ViewPropertyAnimator.animate(mSlidingTabLayout).cancel();
        // Tabs will move between the top of the screen to the bottom of the image.
        float translationY = ScrollUtils.getFloat(-scrollY + mFlexibleSpaceHeight - mTabHeight, 0, mFlexibleSpaceHeight - mTabHeight);
        if (animated) {
            // Animation will be invoked only when the current tab is changed.
            ViewPropertyAnimator.animate(mSlidingTabLayout)
                    .translationY(translationY)
                    .setDuration(200)
                    .start();
        } else {
            // When Fragments' scroll, translate tabs immediately (without animation).
            ViewHelper.setTranslationY(mSlidingTabLayout, translationY);
        }
    }




    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }



    private void initWeibo() {

        tv_content.setText(mWeibo.span);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        tv_username.setText(mWeibo.user.name);
        String url = mWeibo.user.avatar_large;

        if (!url.equals(iv_avatar.getTag())) {
            iv_avatar.setTag(url);
            ImageLoader.getInstance().displayImage(url, iv_avatar, Constants.avatarOptions);
        }

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserHomeActivity.startUserHomeActivity(StatusDetailActivity.this
                        , mWeibo.user);
            }
        });


        String source = TextUtils.isEmpty(mWeibo.source) ? "" : Utility.dealSourceString(mWeibo.source);
        tv_time_source.setText(StatusTimeUtils.instance(this).buildTimeString(mWeibo.created_at) + " | " + source);

        tv_like_count.setText("  " + Utility.getCountString(mWeibo.attitudes_count));

        if(mWeibo.retweeted_status==null){


            dealImageLayout(fl_images,imageMaxWidth,mWeibo);
            rl_repost.setVisibility(View.GONE);

        }
        else {

            fl_images.setVisibility(View.GONE);
            mWeibo.origSpan=SpannableStringUtils.getOrigSpan(this,mWeibo.retweeted_status,true);
            tv_orignal_content .setText(mWeibo.origSpan);
            tv_orignal_content.setMovementMethod(LinkMovementMethod.getInstance());
            dealImageLayout(fl_images_repost,imageMaxWidth,mWeibo.retweeted_status);
        }

    }





    @Override
    public int getHeadViewHeight() {
        if(mToolbarView!=null){
            return  mToolbarView.getHeight();
        }

        return 0;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
      return mToolbarView.dispatchTouchEvent(event);
    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"comments", "repost"};

        private int mScrollY;

        private MessageModel msg;

        public NavigationAdapter(FragmentManager fm,MessageModel msg) {
            super(fm);
            this.msg=msg;
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            // Initialize fragments.
            // Please be sure to pass scroll position to each fragments using setArguments
            //

            Fragment f =null;
            final int pattern = position % 4;
            switch (pattern) {
                case 0: {
                    f =StatusCommentFragment.newInstance(msg.id);
                    if (0 < mScrollY) {
                        Bundle args = new Bundle();
                        args.putInt(ViewPagerTabRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
                        f.setArguments(args);
                    }
                    break;
                }
                case 1: {
                    f = StatusRepostFragment.newInstance(msg.id);
                    if (0 < mScrollY) {
                        Bundle args = new Bundle();
                        args.putInt(ViewPagerTabRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
                        f.setArguments(args);
                    }
                    break;
                }


            }
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    private class FavoTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Boolean doInBackground(Void... params) {

            FavoDao dao = new FavoDao(mWeibo.id);
            FavoModel model = dao.favo();
            return model != null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Toast.makeText(StatusDetailActivity.this, R.string.success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(StatusDetailActivity.this, R.string.fail, Toast.LENGTH_LONG).show();
            }

        }

    }


    private void dealImageLayout(FlowLayout flowLayout, float maxWidth ,final MessageModel msg) {
        flowLayout.removeAllViews();
        for (int i = 0; i < 9; i++) {
            TagImageVIew imageView = new TagImageVIew(this);
            imageView.setBackgroundColor(getResources().getColor(R.color.bg_list_press));
            imageView.setVisibility(View.GONE);
            imageView.setAdjustViewBounds(true);
            flowLayout.addView(imageView);

        }
        List<MessageModel.PictureUrl> medias = msg.pic_urls;
        if (medias != null && medias.size() > 0) {
//			int size = 0;
            int mediumSize = (int) ((maxWidth - photoMargin) / 2);
            int smallSize = (int) ((maxWidth - 2 * photoMargin) / 3);

            int count = medias.size();

            for (int i = 0; i < count; i++) {

                if (i > flowLayout.getChildCount() - 1) {
                    break;
                }
                final MessageModel.PictureUrl pictureUrl = medias.get(i);
                String imgUrl = pictureUrl.getThumbnail();
                TagImageVIew imageView = (TagImageVIew) flowLayout.getChildAt(i);
                imageView.setMinimumHeight(smallSize);
                imageView.setMinimumWidth(smallSize);
                FlowLayout.LayoutParams param = new FlowLayout.LayoutParams(smallSize, smallSize);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ;
                PicSize picSize = null;
                switch (count) {
                    case 1:
                        imgUrl = pictureUrl.getMedium();

                        picSize = MyApplication.picSizeCache.get(imgUrl);
                        if (picSize != null) {
                            param = new FlowLayout.LayoutParams(picSize.getWidth(), picSize.getHeight());

                        } else {
                            param = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                        }
                        imageView.setMaxHeight((int) maxWidth);
                        imageView.setMaxWidth((int) maxWidth);
                        break;
                    case 3:
                    case 6:
                    case 9:
                        param = new FlowLayout.LayoutParams(smallSize, smallSize);
                        break;
                    case 2:
                    case 4:
                        param = new FlowLayout.LayoutParams(mediumSize, mediumSize);
                        break;
                    case 5:
                    case 7:
                        if (1 < i && i < 5) {
                            param = new FlowLayout.LayoutParams(smallSize, smallSize);
                        } else {
                            param = new FlowLayout.LayoutParams(mediumSize, mediumSize);
                        }
                        break;

                    case 8:
                        if (2 < i && i < 5) {
                            param = new FlowLayout.LayoutParams(mediumSize, mediumSize);
                        } else {
                            param = new FlowLayout.LayoutParams(smallSize, smallSize);
                        }
                        break;

                    default:
                        break;
                }

                imageView.setVisibility(View.VISIBLE);
                imageView.setLayoutParams(param);
                if (pictureUrl.isGif()) {
                    imageView.setDrawTag(true);
                }
                final int index = i;
                if (DeviceUtil.getNetworkType(this) == DeviceUtil.NetWorkType.wifi && !pictureUrl.isGif()) {
                    imgUrl = pictureUrl.getMedium();
                }
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PicsActivity.launch(StatusDetailActivity.this, msg, index);
                    }
                });


                if (!TextUtils.isEmpty(imgUrl) && !imgUrl.equals(imageView.getTag())) {

                    ImageLoadingListener imageLoadingListener = null;

                    if (count == 1 && picSize == null) {
                        imageLoadingListener = new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                int width = loadedImage.getWidth();
                                int height = loadedImage.getHeight();
                                ImageView imageView = (ImageView) view;
                                int singleImgMaxHeight = (int) (imageMaxWidth * 2 / 3);


                                if (height > singleImgMaxHeight) {
                                    height = (int) singleImgMaxHeight;
//
                                }
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                FlowLayout.LayoutParams param = new FlowLayout.LayoutParams(width, height);
                                imageView.setLayoutParams(param);
                                imageView.setImageBitmap(loadedImage);
                                PicSize picSize = new PicSize();
                                picSize.setKey(imageUri);
                                picSize.setWidth(width);
                                picSize.setHeight(height);

                                MyApplication.picSizeCache.put(picSize.getKey(), picSize);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        };
                    }

                    ImageLoader.getInstance().displayImage(imgUrl, imageView, Constants.timelineListOptions, imageLoadingListener);
                    imageView.setTag(imgUrl);

                }

            }
            flowLayout.setVisibility(View.VISIBLE);

        }


    }


}
