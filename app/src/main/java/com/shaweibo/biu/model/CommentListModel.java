/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.shaweibo.biu.Utils.SpannableStringUtils;
import com.shaweibo.biu.Utils.StatusTimeUtils;

import java.util.ArrayList;
import java.util.List;



/*
  A list of comments
*/

public class CommentListModel extends BaseListModel<CommentModel, CommentListModel>
{
	private List<CommentModel> comments = new ArrayList<CommentModel>();

	public void spanAll(Context context) {

		for (CommentModel comment : comments) {

			comment.span=SpannableStringUtils.span(context,comment.text);

//			if (comment.reply_comment != null) {
//				comment.reply_comment.span = SpannableStringUtils.getCommentSpan(context, comment.reply_comment);
//			} else if (comment.status != null) {
//				comment.status.span = SpannableStringUtils.getOrigSpan(context, comment.status);
//			}

		}
	}

	public void timestampAll(Context context) {
		StatusTimeUtils utils = StatusTimeUtils.instance(context);
		for (CommentModel comment :  getList()) {

			if (comment.status != null) {
				comment.status.millis = utils.parseTimeString(comment.status.created_at);
			}
		}
	}
	
	@Override
	public int getSize() {
		return comments.size();
	}

	@Override
	public CommentModel get(int position) {
		return comments.get(position);
	}

	@Override
	public List<? extends CommentModel> getList() {
		return comments;
	}

	@Override
	public void addAll(boolean toTop, CommentListModel values) {
		if (values instanceof CommentListModel && values != null && values.getSize() > 0) {
			for (CommentModel msg : values.getList()) {
				if (!comments.contains(msg)) {
					comments.add(toTop ? values.getList().indexOf(msg) : comments.size(),  msg);
				}
			}
			total_number = values.total_number;
		}
		
	}


	public void addAll(boolean toTop, boolean friendsOnly, CommentListModel values, String myUid) {
		addAll(toTop, values);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(total_number);
		dest.writeLong(previous_cursor);
		dest.writeLong(next_cursor);
		dest.writeTypedList(comments);
	}

	public static final Parcelable.Creator<CommentListModel> CREATOR = new Parcelable.Creator<CommentListModel>() {

		@Override
		public CommentListModel createFromParcel(Parcel in) {
			CommentListModel ret = new CommentListModel();
			ret.total_number = in.readInt();
			ret.previous_cursor = in.readLong();
			ret.next_cursor = in.readLong();
			in.readTypedList(ret.comments, CommentModel.CREATOR);

			return ret;
		}

		@Override
		public CommentListModel[] newArray(int size) {
			return new CommentListModel[size];
		}


	};
	
}
