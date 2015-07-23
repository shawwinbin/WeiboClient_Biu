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
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.Utility;
import com.dudutech.weibo.dao.post.PostDao;
import com.dudutech.weibo.global.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.OnClick;

public class NewPostActivity extends AbPostActivity {
    private ArrayList<String> mPics=new ArrayList<String>();
    private ArrayList<Bitmap> mBitmaps = new ArrayList<Bitmap>();
    public static final int PIC_RESULT = 2;
    public static final int CAMERA_RESULT = 3;

    private Uri imageFileUri = null;
    private String picPath;
    private Bitmap mBitmap;



    @OnClick(R.id.btn_inser_img)
    public void inserImg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.pic_select))
                .setItems(getResources().getStringArray(R.array.pic_picker_array), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(choosePictureIntent, PIC_RESULT);

                                break;
                            case 1:

                                imageFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        new ContentValues());
                                if (imageFileUri != null) {
                                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                                    startActivityForResult(i, CAMERA_RESULT);

                                } else {
                                    Toast.makeText(NewPostActivity.this, getString(R.string.cant_insert_album), Toast.LENGTH_SHORT).show();
                                }
                                break;

                        }
                    }
                });
        builder.create().show();
    }

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

//        for (int i = 0; i < mPics.size(); i++) {
//            Bitmap bmp=null;
//            String path = mPics.get(i);
//            if (path != null) {
//                try {
//                    bmp = BitmapFactory.decodeFile(path);
//                } catch (OutOfMemoryError e) {
//                    continue;
//                }
//            }
//            String id = PostDao.uploadPicture(bmp);
//            bmp.recycle();
//            if (id == null || id.trim().equals("")) return false;
//
//            pics += id;
//            if (i < mPics.size() - 1) {
//                pics += ",";
//            }
//        }
        pics = PostDao.uploadPicture(mBitmap);

        // Upload text
        return PostDao.newPostWithMultiPics(status, pics);
    }

    protected boolean post() {

        if (mBitmap==null) {
            return PostDao.newPost(editText.getText().toString());
        } else {
            return postPics(editText.getText().toString());
        }
    }

    @Override
    protected void onPrePost() {
        prog = new ProgressDialog(NewPostActivity.this);
        prog.setMessage(getResources().getString(R.string.sending));
        prog.setCancelable(false);
        prog.show();
    }

    @Override
    protected void onPostResult(boolean result) {

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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_RESULT:
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        editText.setText(getString(R.string.pic_share));
                        editText.setSelection(editText.getText().toString().length());
                    }

                    picPath = Utility.getPicPathFromUri(imageFileUri, this);
                    enablePicture(picPath);
                    break;
                case PIC_RESULT:
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        editText.setText(getString(R.string.pic_share));
                        editText.setSelection(editText.getText().toString().length());
                    }

                    Uri uri2 = intent.getData();

                    if (Build.VERSION.SDK_INT >= 19) {
                        try {
                            ParcelFileDescriptor parcelFileDescriptor =
                                    getContentResolver().openFileDescriptor(intent.getData(), "r");
                            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                            parcelFileDescriptor.close();
                            enablePicture(image);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                        else{
                            picPath = Utility.getPicPathFromUri(uri2, this);
                        enablePicture(picPath);
                        }


                    break;

            }

        }


    }

    private  void enablePicture(String picPath){
        iv_image_to_send.setVisibility(View.VISIBLE);
        // "file:///mnt/sdcard/image.png"  from SD card
        String url="file://"+picPath;
        mBitmap =ImageLoader.getInstance().loadImageSync(url);

//        ImageLoader.getInstance().displayImage(url, iv_image_to_send, Constants.timelineListOptions);
        iv_image_to_send.setImageBitmap(mBitmap);

    }
    private  void enablePicture(Bitmap bitmap){
        iv_image_to_send.setVisibility(View.VISIBLE);
        // "file:///mnt/sdcard/image.png"  from SD card
        mBitmap=bitmap;
        iv_image_to_send.setImageBitmap(bitmap);



    }



}
