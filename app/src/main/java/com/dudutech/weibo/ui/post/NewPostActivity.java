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
import android.os.Bundle;
import android.view.Menu;

import com.dudutech.weibo.R;
import com.dudutech.weibo.dao.post.PostDao;

import java.util.ArrayList;

public class NewPostActivity extends AbPostActivity {
    private ArrayList<String> mPics=new ArrayList<String>();


    protected ProgressDialog prog;
    public  static  void start(Context context){
        Intent intent= new Intent(context,NewPostActivity.class);

        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionbarTitle(R.string.new_post);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
        return true;
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
            String id = PostDao.uploadPicture(bmp);
            bmp.recycle();
            if (id == null || id.trim().equals("")) return false;

            pics += id;
            if (i < mPics.size() - 1) {
                pics += ",";
            }
        }

        // Upload text
        return PostDao.newPostWithMultiPics(status, pics);
    }

    protected boolean post() {

        if (mPics.size() == 0) {
            return PostDao.newPost(editText.getText().toString());
        } else {
            return postPics(editText.getText().toString());
        }
    }

    @Override
    protected void onPrePost() {
        prog = new ProgressDialog(NewPostActivity.this);
        prog.setMessage(getResources().getString(R.string.plz_wait));
        prog.setCancelable(false);
        prog.show();
    }

    @Override
    protected void onPostResult(boolean result) {
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
