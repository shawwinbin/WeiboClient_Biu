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
import android.os.Bundle;
import android.text.TextUtils;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.SpannableStringUtils;
import com.dudutech.weibo.dao.post.CommentStatusDao;
import com.dudutech.weibo.dao.post.PostDao;
import com.dudutech.weibo.model.CommentModel;
import com.dudutech.weibo.model.MessageModel;

public class PostNewRepostActivity extends AbPostActivity {
    public static  final  String EXT_WEIBO = "eta_weibo";
    private MessageModel mWeibo;
    private CommentModel mComment;

    protected ProgressDialog prog;


    public  static  void start(Context context ,MessageModel messageModel ){
        Intent intent= new Intent(context,PostNewRepostActivity.class);
        intent.putExtra(EXT_WEIBO, messageModel);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionbarTitle(R.string.comment_on);
        mWeibo=getIntent().getParcelableExtra(EXT_WEIBO);
        mWeibo.span = SpannableStringUtils.getOrigSpan(this,mWeibo);
        if(mWeibo.retweeted_status!=null) {
            editText.setText("//" + mWeibo.span);
            editText.setSelection(0);
        }

    }




    protected boolean post() {
        String text= editText.getText().toString();
        if(mWeibo==null|| TextUtils.isEmpty(text)){
            return false;
        }
        return PostDao.newRepost(mWeibo.id,text,0);

//        CommentStatusDao dao =new CommentStatusDao(mWeibo.idstr,comment);


    }

    @Override
    protected void onPrePost() {
        prog=new ProgressDialog(PostNewRepostActivity.this);
        prog.setMessage(getResources().getString(R.string.plz_wait));
        prog.setCancelable(false);
        prog.show();

    }

    @Override
    protected void onPostResult(boolean result) {

        if (result) {
            finish();
        } else {
            new AlertDialog.Builder(this)
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
