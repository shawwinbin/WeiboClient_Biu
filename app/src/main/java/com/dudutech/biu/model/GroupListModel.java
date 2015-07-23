/*
 *
 * This file is part of BlackLight
 *
 * BlackLight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlackLight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlackLight.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dudutech.biu.model;

import android.os.Parcel;

import com.dudutech.biu.R;
import com.dudutech.biu.dao.timeline.StatusTimeLineDao;
import com.dudutech.biu.global.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class GroupListModel extends BaseListModel<GroupModel, GroupListModel> {
	private List<GroupModel> lists = new ArrayList<GroupModel>();

	@Override
	public int getSize() {
		return lists.size();
	}

	@Override
	public GroupModel get(int position) {
		return lists.get(position);
	}

	@Override
	public List<? extends GroupModel> getList() {
		return lists;
	}

	public void addDefaultGroupsToTop(){
		GroupModel groupModelBilateral= new GroupModel();
		groupModelBilateral.name= MyApplication.getInstance().getString(R.string.groups_bilateral);
		groupModelBilateral.idstr= StatusTimeLineDao.GROUP_BILATERAL;
		lists.add(0,groupModelBilateral);
		GroupModel groupModelAll= new GroupModel();
		groupModelAll.name= MyApplication.getInstance().getString(R.string.groups_all);
		groupModelAll.idstr= StatusTimeLineDao.GROUP_ALL;
		lists.add(0,groupModelAll);
	}

	@Override
	public void addAll(boolean toTop, GroupListModel values) {
		if (values != null && values.getSize() > 0) {
			lists.clear();
			for (GroupModel msg : values.getList()) {
					lists.add( msg);
			}
			total_number = values.total_number;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(lists);
	}

	public static final Creator<GroupListModel> CREATOR = new Creator<GroupListModel>() {
		@Override
		public GroupListModel createFromParcel(Parcel in) {
			GroupListModel ret = new GroupListModel();
			in.readTypedList(ret.lists, GroupModel.CREATOR);
			return ret;
		}

		@Override
		public GroupListModel[] newArray(int size) {
			return new GroupListModel[size];
		}
	};
}

