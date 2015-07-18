/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.dao.timeline;

import android.content.Context;
import android.database.Cursor;

import com.dudutech.weibo.api.WeiboCommentApi;
import com.dudutech.weibo.db.tables.HomeTimeLineTable;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.BaseListModel;
import com.dudutech.weibo.model.CommentListModel;
import com.dudutech.weibo.model.MessageListModel;
import com.google.gson.Gson;

/**
 * Created by shaw on 2015/7/13.
 */
public abstract class BaseTimelineDao< T extends BaseListModel> implements ITimelineBaseDao {
    public Constants.LOADING_STATUS mStatus;
    protected int mCurrentPage = 0;
    protected  T mListModel;
    @Override
    public Constants.LOADING_STATUS getStatus() {
        return mStatus;
    }
    @Override
    public T getList() {
        return mListModel;
    }
    @Override
    public void loadFromCache() {
        Cursor cursor = query();
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            mListModel = (T) new Gson().fromJson(cursor.getString(cursor.getColumnIndex(HomeTimeLineTable.JSON)),getListClass());
            mCurrentPage = mListModel.getSize() / Constants.HOME_TIMELINE_PAGE_SIZE;
            spanAll(mListModel);
        } else {
            try {
                mListModel = (T) getListClass().newInstance();
            } catch (Exception e) {

            }
        }
    }

    public abstract void spanAll(T t);

    @Override
    public void load(boolean isRefresh) {
        if (isRefresh) {
            mCurrentPage = 0;
        }
        BaseListModel list = load();
        dealStatus(list);
        if(list==null){
            return;
        }
        if (isRefresh) {
            mListModel.getList().clear();
        }
        mListModel.addAll(false, list);
        spanAll(mListModel);

    }

    protected void dealStatus(BaseListModel model){
        if(model!=null){
            int count =model.getList().size();
            if(count<Constants.HOME_TIMELINE_PAGE_SIZE){
                mStatus= Constants.LOADING_STATUS.FINISH;
            }
            else {
                mStatus= Constants.LOADING_STATUS.NORMAL;
            }
        }
        else{
            mStatus= Constants.LOADING_STATUS.FAIL;
        }


    }

    public abstract BaseListModel load();


    protected abstract Class<? extends T> getListClass();
}
