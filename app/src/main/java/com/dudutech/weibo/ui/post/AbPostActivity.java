/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.ui.post;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dudutech.weibo.R;
import com.dudutech.weibo.ui.common.BaseActivity;
import com.dudutech.weibo.ui.friendship.FriendsAtActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public abstract class AbPostActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.edit)
    EditText editText;
    public static final int AT_FRIEND = 1;
    @OnClick(R.id.btn_at_friend)
    public void atFriends() {
      Intent intent= new Intent(AbPostActivity.this, FriendsAtActivity.class);
        this.startActivityForResult(intent,AT_FRIEND);
    }

    @OnClick(R.id.btn_send)
    public void sumbit() {
        new Uploader().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    protected void setActionbarTitle(int ResId) {

        getSupportActionBar().setTitle(ResId);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected abstract boolean post();

    protected abstract void onPrePost();

    protected abstract void onPostResult(boolean result);


    private class Uploader extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            onPrePost();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return post();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            onPostResult(result);

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AT_FRIEND:
                    String name = intent.getStringExtra("name");
                    String ori = editText.getText().toString();
                    int index = editText.getSelectionStart();
                    StringBuilder stringBuilder = new StringBuilder(ori);
                    stringBuilder.insert(index, name);
                    editText.setText(stringBuilder.toString());
                    editText.setSelection(index + name.length());
                    break;
            }

        }
    }
}
