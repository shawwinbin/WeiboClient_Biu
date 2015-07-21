/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/*
 A list of reposts
*/

public class RepostListModel extends MessageListModel
{
	private List<MessageModel> reposts = new ArrayList<MessageModel>();

	@Override
	public int getSize() {
		return reposts.size();
	}

	@Override
	public MessageModel get(int position) {
		return reposts.get(position);
	}

	@Override
	public List<? extends MessageModel> getList() {
		return reposts;
	}

	@Override
	public void addAll(boolean toTop, MessageListModel values) {
		if (values != null && values.getSize() > 0) {
			for (MessageModel msg : values.getList()) {
				if (!reposts.contains(msg)) {
					reposts.add(toTop ? values.getList().indexOf(msg) : reposts.size(), msg);
				}
			}
			total_number = values.total_number;
		}
	}

//	@Override
//	public void addAll(boolean toTop, boolean friendsOnly, MessageListModel values, String myUid) {
//		addAll(toTop, values);
//	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(total_number);
		dest.writeLong(previous_cursor);
		dest.writeLong(next_cursor);
		dest.writeTypedList(reposts);
	}

	public static final Parcelable.Creator<MessageListModel> CREATOR = new Parcelable.Creator<MessageListModel>() {

		@Override
		public RepostListModel createFromParcel(Parcel in) {
			RepostListModel ret = new RepostListModel();
			ret.total_number = in.readInt();
			ret.previous_cursor = in.readLong();
			ret.next_cursor = in.readLong();
			in.readTypedList(ret.reposts, MessageModel.CREATOR);

			return ret;
		}

		@Override
		public RepostListModel[] newArray(int size) {
			return new RepostListModel[size];
		}

	};
}
