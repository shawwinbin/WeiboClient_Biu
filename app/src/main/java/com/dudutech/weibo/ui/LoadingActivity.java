package com.dudutech.weibo.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.Utility;
import com.dudutech.weibo.cache.LoginApiCache;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginApiCache login = new LoginApiCache(this);
        if (needsLogin(login)) {
            login.logout();
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, WebLoginActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, MainActivity.class);
            i.putExtra(Intent.EXTRA_INTENT,getIntent().getIntExtra(Intent.EXTRA_INTENT,0));
            startActivity(i);
            finish();
        }

    }

    private boolean needsLogin(LoginApiCache login) {
        return login.getAccessToken() == null || Utility.isTokenExpired(login.getExpireDate());
    }

}
