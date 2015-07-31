/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.ui.post;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.SpannableStringUtils;
import com.dudutech.biu.dao.post.PostDao;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.CommentModel;
import com.dudutech.biu.model.MessageModel;
import com.nostra13.universalimageloader.core.ImageLoader;

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
        setActionbarTitle(R.string.repost_weibo);
        mWeibo=getIntent().getParcelableExtra(EXT_WEIBO);
        mWeibo.origSpan = SpannableStringUtils.getOrigSpan(this,mWeibo);
        if(mWeibo.retweeted_status!=null) {
            editText.setText("//" + mWeibo.origSpan);
            editText.setSelection(0);
        }
        initUI();
    }

    private void initUI(){

        rl_stauts_info.setVisibility(View.VISIBLE);
        ck_extra.setVisibility(View.VISIBLE);
        iv_image_to_send.setVisibility(View.GONE);
        ck_extra.setText(R.string.comment_meanwhile);
        String statusImgUrl= mWeibo.thumbnail_pic;
        if(TextUtils.isEmpty(statusImgUrl)&&mWeibo.retweeted_status!=null){
            statusImgUrl=mWeibo.retweeted_status.thumbnail_pic;
        }
        if(TextUtils.isEmpty(statusImgUrl)){
            statusImgUrl=mWeibo.user.avatar_large;
        }
        ImageLoader.getInstance().displayImage(statusImgUrl, iv_source, Constants.timelineListOptions);

        tv_status_content.setText(mWeibo.origSpan);

    }




    protected boolean post() {
        String text= editText.getText().toString();
        if(mWeibo==null|| TextUtils.isEmpty(text)){
            return false;
        }


        return PostDao.newRepost(mWeibo.id,text,ck_extra.isChecked()?1:0);

//        CommentStatusDao dao =new CommentStatusDao(mWeibo.idstr,comment);


    }

    @Override
    protected void onPrePost() {
        prog=new ProgressDialog(PostNewRepostActivity.this);
        prog.setMessage(getResources().getString(R.string.sending));
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
