/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.ui.post;

import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.DeviceUtil;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.ui.common.BaseActivity;
import com.dudutech.weibo.ui.friendship.FriendsAtActivity;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public abstract class AbPostActivity extends BaseActivity implements EmoticonFragment.OnEmoticonOnClinckListener {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.edit)
    EditText editText;
    @OnClick(R.id.edit)
    public void editClick() {
        if (fl_bottom.getVisibility() == View.VISIBLE) {
            hideBottom();
        }
    }


    @InjectView(R.id.fl_bottom)
    FrameLayout fl_bottom;

    public static final int AT_FRIEND = 1;

    @OnClick(R.id.btn_at_friend)
    public void atFriends() {
        Intent intent = new Intent(AbPostActivity.this, FriendsAtActivity.class);
        this.startActivityForResult(intent, AT_FRIEND);
    }

    @OnClick(R.id.btn_send)
    public void sumbit() {
        new Uploader().execute();
    }


    @OnClick(R.id.btn_inser_emoji)
    public void showEmoji() {

        if (fl_bottom.getVisibility() == View.GONE) {

            DeviceUtil.closeSoftInput(this, editText);
            fl_bottom.setVisibility(View.VISIBLE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        } else {
          hideBottom();
            DeviceUtil.showSoftInput(this, editText);
        }


    }


    private EmoticonFragment mEmoticonFragment;

    private Handler  mHandler;

    private final String TAG_EMOTICON = "tag_emotiocn";
    private int heightDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEmoticonFragment = (EmoticonFragment) getFragmentManager().findFragmentByTag(TAG_EMOTICON);
        if (mEmoticonFragment == null) {
            mEmoticonFragment = new EmoticonFragment();

        }
        getFragmentManager().beginTransaction().replace(R.id.fl_bottom, mEmoticonFragment, TAG_EMOTICON).commit();
        mHandler =new Handler();


    }


    protected void setActionbarTitle(int ResId) {

        getSupportActionBar().setTitle(ResId);

    }

    private void hideBottom(){
        fl_bottom.setVisibility(View.GONE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

    @Override
    public void onEmoticonClick(SpannableString emojiSpane) {
        int maxLength = Constants.MAX_WEIBO_LENGTH;
        if (editText.getText().toString().length() + emojiSpane.length() <= maxLength) {
            Spannable contentSpann = editText.getText();
            int select = editText.getSelectionStart();
            SpannableStringBuilder strBuilder = new SpannableStringBuilder();
            strBuilder.append(contentSpann.subSequence(0, select));
            strBuilder.append(emojiSpane);
            strBuilder.append(contentSpann.subSequence(select, contentSpann.length()));

            editText.setText(strBuilder);
            int length = select + emojiSpane.length();
            if (length > editText.length()) {
                length = editText.length();
            }
            editText.setSelection(length);
        } else {
            String msg = String.format(getString(R.string.weibo_length_error),
                    maxLength);

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }

    }
}
