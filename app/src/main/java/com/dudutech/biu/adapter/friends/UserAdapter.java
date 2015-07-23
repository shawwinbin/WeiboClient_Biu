package com.dudutech.biu.adapter.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudutech.biu.R;
import com.dudutech.biu.adapter.timeline.BaseTimelinAdapter;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.UserListModel;
import com.dudutech.biu.model.UserModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015-7-21.
 */
public class UserAdapter extends BaseTimelinAdapter<UserListModel> {

    public OnItemClickListener mListenner;
    public UserAdapter(Context context, UserListModel userListModel) {
        super(context, userListModel);
        setBottomCount(1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.item_user, parent, false);
        UserViewHolder holder = new UserViewHolder(view, mContext);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(holder instanceof UserViewHolder){
            UserModel  user= mListModel.get(position);
            UserViewHolder vh= (UserViewHolder) holder;
            String url = user.avatar_large;
            if (!url.equals(vh.iv_avatar.getTag())) {
                vh.iv_avatar.setTag(url);
                ImageLoader.getInstance().displayImage(url, vh.iv_avatar, Constants.avatarOptions);
            }
            vh.tv_username.setText(user.getName());
            vh.tv_dest.setText(!TextUtils.isEmpty(user.verified_reason)?user.verified_reason:user.description);
        }

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_username)
        public TextView tv_username;
        @InjectView(R.id.tv_des)
        public TextView tv_dest;
        @InjectView(R.id.iv_avatar)
        public ImageView iv_avatar;


        public UserViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListenner != null) {

                        mListenner.onTtemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onTtemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onClickListenner){
        this.mListenner=onClickListenner;
    }
}
