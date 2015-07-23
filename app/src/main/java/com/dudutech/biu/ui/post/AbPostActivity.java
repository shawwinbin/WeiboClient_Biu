/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.ui.post;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.DeviceUtil;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.ui.common.BaseActivity;
import com.dudutech.biu.ui.friendship.FriendsAtActivity;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public abstract class AbPostActivity extends BaseActivity implements EmoticonFragment.OnEmoticonOnClinckListener {


    public static final int AT_FRIEND = 1;



    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tv_text_left)
    TextView tv_text_left;

    @InjectView(R.id.ck_extra)
    CheckBox ck_extra;

    @InjectView(R.id.rl_status_info)
    RelativeLayout rl_stauts_info;

    @InjectView(R.id.iv_source)
    public ImageView iv_source;

    @InjectView(R.id.tv_status_content)
    public TextView tv_status_content;

    @InjectView(R.id.edit)
    EditText editText;
    @OnClick(R.id.edit)
    public void editClick() {
        if (fl_bottom.getVisibility() == View.VISIBLE) {
            hideBottom();
        }
    }

    @InjectView(R.id.iv_img_to_send)
    ImageView iv_image_to_send;

    @InjectView(R.id.btn_inser_img)
    ImageButton btn_inser_img;



    @InjectView(R.id.fl_bottom)
    FrameLayout fl_bottom;



    @OnClick(R.id.btn_at_friend)
    public void atFriends() {
        Intent intent = new Intent(AbPostActivity.this, FriendsAtActivity.class);
        this.startActivityForResult(intent, AT_FRIEND);
    }

    @OnClick(R.id.btn_send)
    public void sumbit() {

        int left=Constants.MAX_WEIBO_LENGTH- editText.getText().toString().length();
        if(left<0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setMessage(R.string.weibo_length_error);
             builder.setPositiveButton(R.string.btn_commfire, new DialogInterface.OnClickListener() {
                  @Override public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();

                  }
             });

            builder.create().show();
        }
        else{
            new Uploader().execute();
        }



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
    private final String TAG_EMOTICON = "tag_emotiocn";


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

        tv_text_left.setText(String.valueOf(Constants.MAX_WEIBO_LENGTH));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int left=Constants.MAX_WEIBO_LENGTH- s.toString().length();
                tv_text_left.setText(String.valueOf(left));
            }
        });

    }


    protected void setActionbarTitle(int ResId) {
        getSupportActionBar().setTitle(ResId);
    }

    private void hideBottom() {
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
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (fl_bottom.getVisibility()==View.VISIBLE) {
           hideBottom();
        } else if (!TextUtils.isEmpty(editText.getText().toString()) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.exit_comfire);
//            builder.setTitle("提示");
            builder.setPositiveButton(R.string.btn_commfire, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AbPostActivity.this.finish();
                }
            });
            builder.setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
                @Override

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
            builder.create().show();
        } else {
            super.onBackPressed();
        }



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
