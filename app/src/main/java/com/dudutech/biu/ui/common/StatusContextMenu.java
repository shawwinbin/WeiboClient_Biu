package com.dudutech.biu.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.Utility;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by froger_mcs on 15.12.14.
 */
public class StatusContextMenu extends LinearLayout {
    private static final int CONTEXT_MENU_WIDTH = Utility.dpToPx(240);

    private int feedItem = -1;

    private OnStatusContextMenuItemClickListener onItemClickListener;

    public StatusContextMenu(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu, this, true);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void bindToItem(int feedItem) {
        this.feedItem = feedItem;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject(this);
    }

    public void dismiss() {
        ((ViewGroup) getParent()).removeView(StatusContextMenu.this);
    }

    @OnClick(R.id.btn_favo)
    public void onFavoClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onFavoClick(feedItem);
        }
    }

    @OnClick(R.id.btn_copy)
    public void onCopyClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCopyClick(feedItem);
        }
    }


    @OnClick(R.id.btn_cancle)
    public void onCancelClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCancelClick(feedItem);
        }
    }

    public void setOnFeedMenuItemClickListener(OnStatusContextMenuItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnStatusContextMenuItemClickListener {
        public void onFavoClick(int feedItem);

        public void onCopyClick(int feedItem);


        public void onCancelClick(int feedItem);
    }
}