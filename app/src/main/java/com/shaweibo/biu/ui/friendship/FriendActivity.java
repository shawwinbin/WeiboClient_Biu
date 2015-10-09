package com.shaweibo.biu.ui.friendship;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shaweibo.biu.R;
import com.shaweibo.biu.adapter.common.MyFragmentPagerAdapter;
import com.shaweibo.biu.model.UserModel;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FriendActivity extends AppCompatActivity {

    @InjectView(R.id.tabs)
    public TabLayout tabLayout;
    @InjectView(R.id.viewpager_friends)
    ViewPager mViewPager ;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    FriendsFragment mFriendsFragment;
    FollowersFragment mFollwerFragment;
    private static final String ETA_USER = "eta_user";
    private UserModel mUser;

    public static void start(Context context, UserModel userModel) {

        Intent intent = new Intent(context, FriendActivity.class);
        intent.putExtra(ETA_USER, userModel);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.inject(this);
        if(savedInstanceState==null){
            mUser = getIntent().getParcelableExtra(ETA_USER);
        }
        else{
            mUser=savedInstanceState.getParcelable(ETA_USER);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(ETA_USER, mUser);

    }

    private void setupViewPager(ViewPager viewPager) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mFriendsFragment= FriendsFragment.newInstance(mUser.id);
        mFollwerFragment= FollowersFragment.newInstance(mUser.id);
        adapter.addFragment(mFriendsFragment, getString(R.string.following));
        adapter.addFragment(mFollwerFragment, getString(R.string.followers));
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
