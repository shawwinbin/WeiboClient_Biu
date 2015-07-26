package com.dudutech.biu.ui.timeline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.SystemBarUtils;
import com.dudutech.biu.dao.relationship.FanDao;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.UserModel;
import com.dudutech.biu.ui.common.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserHomeActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {



    private static final String ETA_USER = "eta_user";

    private UserModel mUser;


    public static void  startUserHomeActivity(Context context,UserModel userModel){

        Intent  intent =new Intent(context,UserHomeActivity.class);
        intent.putExtra(ETA_USER,userModel);
       context.startActivity(intent);

    }


    @InjectView(R.id.iv_user_cover)
    ImageView iv_user_cover;

    @InjectView(R.id.iv_avatar)
    ImageView iv_user_avatar;
//    @InjectView(R.id.tv_username)
//    TextView tv_username;
    @InjectView(R.id.tv_user_infos)
    TextView tv_user_infos;
    @InjectView(R.id.tv_user_sign)
    TextView tv_user_sign;
    @InjectView(R.id.fl_content)
    FrameLayout fl_content;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.fab_star)
    FloatingActionButton fab_star;
    @OnClick(R.id.fab_star)

    public  void  followUser(){


        String comfirmTitle= "";


        if(mUser.following){
            comfirmTitle=getString(R.string.unfollow_user_comfire);
        }
        else {
            comfirmTitle=getString(R.string.follow_user_comfire);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(comfirmTitle);
        builder.setTitle(R.string.tips);
        builder.setPositiveButton(R.string.btn_commfire, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new FanTask().execute();

            }
        });
        builder.setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }

        });
        builder.create().show();

    }

    FanDao mFanDao;



    private UserTimelineFragment mUserTimelineFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        ButterKnife.inject(this);
//        initStatusBar();
        mUser=getIntent().getParcelableExtra(ETA_USER);
        initUserinfo();
        mUserTimelineFragment = UserTimelineFragment.newInstance(mUser.id);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content, mUserTimelineFragment)
                .commit();

        appbar.addOnOffsetChangedListener(this);

        mFanDao =new FanDao(mUser.id);
        initUI();



    }


    private void initUI(){

        if(mUser.following){

            fab_star.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        else {
            fab_star.setColorFilter(getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

    }

    private void initUserinfo(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(mUser.getName());
//        tv_username.setText(mUser.getName());
        tv_user_infos.setText(mUser.location+" / "+mUser.gender);
        tv_user_sign.setText(!TextUtils.isEmpty(mUser.verified_reason)?mUser.verified_reason:mUser.description);
        ImageLoader.getInstance().displayImage(mUser.cover_image_phone,iv_user_cover, Constants.timelineListOptions);
        ImageLoader.getInstance().displayImage(mUser.avatar_large, iv_user_avatar, Constants.avatarOptions);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home, menu);
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

    private void initStatusBar(){
        if (Build.VERSION.SDK_INT == 19) {

            ViewGroup drawerRoot2 = (ViewGroup) findViewById(R.id.toolbar);
            drawerRoot2.setPadding(drawerRoot2.getPaddingLeft(),
                    SystemBarUtils.getStatusBarHeight(this),
                    drawerRoot2.getPaddingRight(),
                    drawerRoot2.getBottom());

        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        mUserTimelineFragment.setSwipeRefreshEnable(i == 0);

    }

    private class FanTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected Boolean doInBackground(Void... params) {

            UserModel user=null;
            if(mUser.following){
                user= mFanDao.unFllowUser();
            }
            else {
                user= mFanDao.followUser();
            }

            return  user!=null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result){
                Toast.makeText(UserHomeActivity.this,R.string.success,Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(UserHomeActivity.this,R.string.fail,Toast.LENGTH_LONG).show();
            }

        }

    }
}
