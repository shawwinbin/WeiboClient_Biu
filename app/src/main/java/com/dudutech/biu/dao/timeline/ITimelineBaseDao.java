/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.dao.timeline;

import android.database.Cursor;

import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.BaseListModel;

/**
 *  timeline dao interface
 * Created by shaw on 2015/7/11.
 */
public interface  ITimelineBaseDao {

    public void loadFromCache();
    public void cache();
    public void load(boolean isRefresh);
    public Cursor query();
    public BaseListModel getList();
    public Constants.LOADING_STATUS getStatus();

}
