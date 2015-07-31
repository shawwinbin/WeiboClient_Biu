package com.dudutech.biu.ui.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.dudutech.biu.Utils.Utility;



/**
 * Created by froger_mcs on 16.12.14.
 */
public class StatusContextMenuManager extends RecyclerView.OnScrollListener implements View.OnAttachStateChangeListener {

    private static StatusContextMenuManager instance;

    private StatusContextMenu contextMenuView;

    private boolean isContextMenuDismissing;
    private boolean isContextMenuShowing;

    public static StatusContextMenuManager getInstance() {
        if (instance == null) {
            instance = new StatusContextMenuManager();
        }
        return instance;
    }

    private StatusContextMenuManager() {

    }

public void toggleContextMenuFromView(View openingView, int feedItem, StatusContextMenu.OnStatusContextMenuItemClickListener listener) {
    if (contextMenuView == null) {
        showContextMenuFromView(openingView, feedItem, listener);
    } else {
        hideContextMenu();
    }
}

    private void showContextMenuFromView(final View openingView, int feedItem, StatusContextMenu.OnStatusContextMenuItemClickListener listener) {
        if (!isContextMenuShowing) {
            isContextMenuShowing = true;
            contextMenuView = new StatusContextMenu(openingView.getContext());
            contextMenuView.bindToItem(feedItem);
            contextMenuView.addOnAttachStateChangeListener(this);
            contextMenuView.setOnFeedMenuItemClickListener(listener);

            ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content)).addView(contextMenuView);

            contextMenuView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contextMenuView.getViewTreeObserver().removeOnPreDrawListener(this);
                    setupContextMenuInitialPosition(openingView);
                    performShowAnimation();
                    return false;
                }
            });
        }
    }

    private void setupContextMenuInitialPosition(View openingView) {
        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        int additionalBottomMargin = Utility.dpToPx(16);
        contextMenuView.setTranslationX(openingViewLocation[0] - contextMenuView.getWidth()+openingView.getWidth());
//        contextMenuView.setTranslationY(openingViewLocation[1] - contextMenuView.getHeight() - additionalBottomMargin);
        contextMenuView.setTranslationY(openingViewLocation[1]  + additionalBottomMargin);
    }

    private void performShowAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth() );
       contextMenuView.setPivotY(0);
        contextMenuView.setScaleX(0.1f);
        contextMenuView.setScaleY(0.1f);
        contextMenuView.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isContextMenuShowing = false;
                    }
                });
    }

    public void hideContextMenu() {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            performDismissAnimation();
        }
    }

    private void performDismissAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth() );
        contextMenuView.setPivotY(0);
        contextMenuView.animate()
                .scaleX(0.1f).scaleY(0.1f)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (contextMenuView != null) {
                            contextMenuView.dismiss();
                        }
                        isContextMenuDismissing = false;
                    }
                });
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (contextMenuView != null) {
            hideContextMenu();
            contextMenuView.setTranslationY(contextMenuView.getTranslationY() - dy);
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        contextMenuView = null;
    }
}
