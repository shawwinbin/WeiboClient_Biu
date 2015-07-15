/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.ui.post;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dudutech.weibo.R;
import com.dudutech.weibo.api.PostApi;
import com.dudutech.weibo.model.CommentModel;
import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.ui.common.BaseActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewPostActivity extends BaseActivity {
    public static  final  String EXT_WEIBO = "eta_weibo";
    public static  final  String EXT_COMMENT = "eta_comment";
    public static  final  String EXT_POST_FLAG = "eta_post_flag";
    public static  final  int FLAG_POST = 0;
    public static  final  int FLAG_COMMENT = 1;
    public static  final  int FLAG_REPOST = 2;
    public static  final  int FLAG_REPLY = 3;

    private MessageModel mWeibo;

    private CommentModel mComment;

    private int mFlag;

    private ArrayList<String> mPics=new ArrayList<String>();


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.edit)
    EditText editText;
//    @InjectView(R.id.btn_send)
    @OnClick(R.id.btn_send)
    public  void  sumbit(){
        new Uploader().execute();
    }

//    ImageButton btn_send;


    public  static  void start(Context context ,MessageModel messageModel ,CommentModel commentModel,int flag){
        Intent intent= new Intent(context,NewPostActivity.class);
        intent.putExtra(EXT_WEIBO, messageModel);
        intent.putExtra(EXT_COMMENT, messageModel);
        intent.putExtra(EXT_POST_FLAG, flag);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFlag= getIntent().getIntExtra(EXT_POST_FLAG, FLAG_POST);
        mWeibo=getIntent().getParcelableExtra(EXT_WEIBO);
        mComment= getIntent().getParcelableExtra(EXT_COMMENT);


        initUI();

    }


    private  void initUI(){
        switch (mFlag){
            case   FLAG_POST :
                getSupportActionBar().setTitle(R.string.new_post);

                break;
            case   FLAG_COMMENT :
                getSupportActionBar().setTitle(R.string.comment_on);
                break;
            case   FLAG_REPOST :
                getSupportActionBar().setTitle(R.string.repost_weibo);
                break;
            case   FLAG_REPLY :
                getSupportActionBar().setTitle(R.string.reply_to);
                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        switch (id ){
            case R.id.action_settings :
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private boolean postPics(String status) {
        // Upload pictures first
        String pics = "";

        for (int i = 0; i < mPics.size(); i++) {
            Bitmap bmp=null;
            String path = mPics.get(i);
            if (path != null) {
                try {
                    bmp = BitmapFactory.decodeFile(path);
                } catch (OutOfMemoryError e) {
                    continue;
                }
            }
            String id = PostApi.uploadPicture(bmp);
            bmp.recycle();
            if (id == null || id.trim().equals("")) return false;

            pics += id;
            if (i < mPics.size() - 1) {
                pics += ",";
            }
        }

        // Upload text
        return PostApi.newPostWithMultiPics(status, pics, "");
    }

    protected boolean post() {

        if (mPics.size() == 0) {
            return PostApi.newPost(editText.getText().toString(), "");
        } else {
            return postPics(editText.getText().toString());
        }
    }



    private class Uploader extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog prog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            prog = new ProgressDialog(NewPostActivity.this);
            prog.setMessage(getResources().getString(R.string.plz_wait));
            prog.setCancelable(false);
            prog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return post();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            prog.dismiss();

            if (result) {
                finish();
            } else {
                new AlertDialog.Builder(NewPostActivity.this)
                        .setMessage(R.string.send_fail)
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        }

    }
}
