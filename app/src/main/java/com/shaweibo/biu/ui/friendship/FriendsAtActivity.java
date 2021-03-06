package com.shaweibo.biu.ui.friendship;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.shaweibo.biu.R;
import com.shaweibo.biu.dao.login.LoginDao;
import com.shaweibo.biu.ui.common.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FriendsAtActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    FriendAtFragment friendListFragment;
    private final String  TAG_AT_FRIEND="at_friends";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_at);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        friendListFragment= FriendAtFragment.newInstance(LoginDao.getInstance(this).getUid());
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,friendListFragment,TAG_AT_FRIEND).commit();
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


}
