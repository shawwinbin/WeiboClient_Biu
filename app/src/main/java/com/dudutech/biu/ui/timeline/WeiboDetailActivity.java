/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.ui.timeline;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.design.widget.TabLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.DeviceUtil;
import com.dudutech.biu.Utils.SpannableStringUtils;
import com.dudutech.biu.Utils.StatusTimeUtils;
import com.dudutech.biu.Utils.Utility;
import com.dudutech.biu.dao.favo.FavoDao;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.global.MyApplication;
import com.dudutech.biu.model.FavoModel;
import com.dudutech.biu.model.MessageModel;
import com.dudutech.biu.model.PicSize;
import com.dudutech.biu.model.UserModel;
import com.dudutech.biu.ui.comments.StatusCommentFragment;
import com.dudutech.biu.ui.common.BaseActivity;
import com.dudutech.biu.ui.picture.PicsActivity;
import com.dudutech.biu.widget.FlowLayout;
import com.dudutech.biu.widget.LetterImageView;
import com.dudutech.biu.widget.TagImageVIew;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WeiboDetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {


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
    public TextView tv_like_count;
    @InjectView(R.id.tabs)
    public TabLayout tabLayout;
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.rl_repost)
    View rl_repost;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;

    @InjectView(R.id.tv_orignal_content)
    public TextView tv_orignal_content;

    StatusCommentFragment mStatusCommentFragment;

    StatusRepostFragment mStatusRepostFragment;

    public MessageModel mWeibo;

    private int photoMargin;
    private float imageMaxWidth;


    public static void start(Context context, MessageModel messageModel) {
        Intent intent = new Intent(context, WeiboDetailActivity.class);
        intent.putExtra(EXT_WEIBO, messageModel);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_detail);
        ButterKnife.inject(this);
        if (savedInstanceState != null) {
            mWeibo = savedInstanceState.getParcelable(EXT_WEIBO);
        } else {
            mWeibo = getIntent().getParcelableExtra(EXT_WEIBO);
        }

        mWeibo.span=SpannableStringUtils.getSpan(this,mWeibo,true);
        photoMargin = getResources().getDimensionPixelSize(R.dimen.moment_photo_margin);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float padding = getResources().getDimension(R.dimen.NormalMargin);
        imageMaxWidth = metrics.widthPixels - 4 * padding;
        float smallPadding = getResources().getDimension(R.dimen.SmallPadding);

        appbar.addOnOffsetChangedListener(this);
        initWeibo();

        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        mStatusCommentFragment = StatusCommentFragment.newInstance(mWeibo.id);
        mStatusRepostFragment = StatusRepostFragment.newInstance(mWeibo.id);
        adapter.addFragment(mStatusCommentFragment, getString(R.string.comment) + " ( " + Utility.getCountString(mWeibo.comments_count) + " ) ");
        adapter.addFragment(mStatusRepostFragment, getString(R.string.repost) + " ( " + Utility.getCountString(mWeibo.reposts_count) + " ) ");
        viewPager.setAdapter(adapter);
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

                UserHomeActivity.startUserHomeActivity(WeiboDetailActivity.this
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
            dealImageLayout(fl_images_repost, imageMaxWidth, mWeibo.retweeted_status);
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

//        boolean enable = i == 0;
//        if (mViewPager.getCurrentItem() == 0) {
//            mStatusCommentFragment.setSwipeRefreshEnable(enable);
//        } else {
//            mStatusRepostFragment.setSwipeRefreshEnable(enable);
//        }

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
                Toast.makeText(WeiboDetailActivity.this, R.string.success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(WeiboDetailActivity.this, R.string.fail, Toast.LENGTH_LONG).show();
            }

        }

    }

    /**
     * 图片处理
     */
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
                // 图片超过九张
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
                        PicsActivity.launch(WeiboDetailActivity.this, msg, index);
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

                                // 放入内存
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
