/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.ui.post;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dudutech.weibo.R;
import com.dudutech.weibo.model.CommentModel;
import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.ui.common.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewPostActivity extends BaseActivity {
    public static  final  String EXT_WEIBO = "eta_weibo";
    public static  final  String EXT_COMMENT = "eta_comment";
    public static  final  String EXT_POST_FLAG = "eta_post_flag";
    public static  final  int FLAG_POST = 0;
    public static  final  int FLAG_COMMENT = 1;
    public static  final  int FLAG_REPOST = 2;
    public static  final  int FLAG_REPLY = 3;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    public  static  void start(Context context ,MessageModel messageModel ,CommentModel commentModel,int flag){
        Intent intent= new Intent(context,NewPostActivity.class);
        intent.putExtra(EXT_WEIBO,messageModel);
        intent.putExtra(EXT_COMMENT,messageModel);
        intent.putExtra(EXT_POST_FLAG,flag);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
