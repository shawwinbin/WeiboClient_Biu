/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.dao.timeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dudutech.biu.dao.UrlConstants;
import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.db.tables.MentionsTimeLineTable;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.MessageListModel;
import com.dudutech.biu.dao.WeiboParameters;
import com.google.gson.Gson;

import static com.dudutech.biu.BuildConfig.DEBUG;

/**
 * Created by shaw on 2015/7/13.
 */
public class StatusMentionMeDao extends  StatusTimeLineDao{
    private static final String TAG = StatusMentionMeDao.class.getSimpleName();
    public  StatusMentionMeDao(Context context){
        super(context,"");
    }
    @Override
    public void cache() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL(Constants.SQL_DROP_TABLE + MentionsTimeLineTable.NAME);
        db.execSQL(MentionsTimeLineTable.CREATE);
        ContentValues values = new ContentValues();
        values.put(MentionsTimeLineTable.ID, 1);
        values.put(MentionsTimeLineTable.JSON, new Gson().toJson(mListModel));
        db.insert(MentionsTimeLineTable.NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public Cursor query() {
        return mHelper.getReadableDatabase().query(MentionsTimeLineTable.NAME, null, null, null, null, null, null);
    }

    @Override
    public MessageListModel load() {
        WeiboParameters params = new WeiboParameters();
        params.put("count", Constants.HOME_TIMELINE_PAGE_SIZE);
        params.put("page",  ++mCurrentPage);

        try {
            String json = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.MENTIONS, params);
            return new Gson().fromJson(json, MessageListModel.class);
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "Cannot fetch home timeline, " + e.getClass().getSimpleName());
            }
            return null;
        }

    }


}
