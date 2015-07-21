package com.dudutech.weibo.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.dudutech.weibo.db.tables.UsersTable;

import java.util.ArrayList;
import java.util.List;
/**
 List of weibo user
 From timelines
 credits to: qii, PeterCxy
 author: shaw
 **/
public class UserListModel extends BaseListModel<UserModel, UserListModel> {

    private List<UserModel> users = new ArrayList<UserModel>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(total_number);
        dest.writeLong(previous_cursor);
        dest.writeLong(next_cursor);

        dest.writeTypedList(users);
    }

    public static final Parcelable.Creator<UserListModel> CREATOR =
            new Parcelable.Creator<UserListModel>() {
                public UserListModel createFromParcel(Parcel in) {
                    UserListModel userListModel= new UserListModel();
                    userListModel.total_number = in.readInt();
                    userListModel.previous_cursor =in.readLong();
                    userListModel.next_cursor =in.readLong();
                    userListModel.users = new ArrayList<UserModel>();
                    in.readTypedList(userListModel.users, UserModel.CREATOR);
                    return userListModel;
                }

                public UserListModel[] newArray(int size) {
                    return new UserListModel[size];
                }
            };


    @Override
    public int getSize() {
        return users.size();
    }

    @Override
    public UserModel get(int position) {
        return users.get(position);
    }

    @Override
    public List<? extends UserModel> getList() {
        return users;
    }

    @Override
    public void addAll(boolean toTop, UserListModel values) {
        if (values instanceof UserListModel && values != null && values.getSize() > 0) {
            for (UserModel user : values.getList()) {

                if (!users.contains(user)) {
                    users.add(toTop ? values.getList().indexOf(user) : users.size(),  user);
                }
            }
            total_number = values.total_number;
        }
    }
}
