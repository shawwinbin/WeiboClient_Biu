package com.shaweibo.biu.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.shaweibo.biu.Utils.Utility;
import com.shaweibo.biu.dao.emoticons.EmoticonsDao;
import com.shaweibo.biu.dao.login.LoginDao;
import com.shaweibo.biu.ui.main.MainActivity;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginDao login = LoginDao.getInstance(this);
        EmoticonsDao.getInstance();
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

    private boolean needsLogin(LoginDao login) {
        return login.getAccessToken() == null || Utility.isTokenExpired(login.getExpireDate());
    }

}
