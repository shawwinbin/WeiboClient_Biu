package com.dudutech.weibo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.StatusTimeUtils;
import com.dudutech.weibo.Utils.Utility;
import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.widget.FlowLayout;
import com.dudutech.weibo.widget.SelectableRoundedImageView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2014.12.29.
 */
public class TimelineAdapter  extends BaseMultipleItemAdapter {
    private  Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<MessageModel> mList;
    private StatusTimeUtils mTimeUtils;
    private int photoMargin;
    private float imageMaxWidth;
    public static enum ITEM_TYPE {
        ITEM_TYPE_HEADER,
        ITEM_TYPE_BOTTOM,
        ITEM_TYPE_CONTENT_BASE,
        ITEM_TYPE_CONTENT_REPOST,

    }
    public TimelineAdapter(Context context,List<MessageModel> list){
        this.mContext=context;
        this.mList=list;
        mLayoutInflater = LayoutInflater.from(context);
        mTimeUtils=StatusTimeUtils.instance(context);
        float padding = context.getResources().getDimension(R.dimen.MiddleMargin);
        float avatarSize = context.getResources().getDimension(R.dimen.avatar_lst_item);
        photoMargin = context.getResources().getDimensionPixelSize(R.dimen.moment_photo_margin);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();;
        imageMaxWidth = metrics.widthPixels - 4 * padding - avatarSize;
    }





//      @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mLayoutInflater.inflate(R.layout.card_weibo,parent,false);
//        ViewHolder holder = new ViewHolder(view,mContext);
//        return holder;
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  holder, int position) {


        MessageModel msg= mList.get(position);

        if(holder instanceof  BaseWeiboViewHolder){
            onBindBaseWeiboViewHolder((BaseWeiboViewHolder)holder,position);
            dealImageLayout(msg,((BaseWeiboViewHolder) holder), imageMaxWidth, position);
        }
        else if( holder instanceof  RepostWeiboViewHolder){
            onBindRepostWeiboViewHolder((RepostWeiboViewHolder)holder,position);
            dealImageLayout( msg.retweeted_status,((BaseWeiboViewHolder) holder), imageMaxWidth, position);
        }


    }

    public void onBindBaseWeiboViewHolder(BaseWeiboViewHolder holder, int position){
        resetViewHolder(holder) ;
        MessageModel msg = mList.get(position);
//        holder.tv_time.setText(msg.);
       holder.tv_content.setText(msg.span);
        holder.tv_username.setText(msg.user.name);
        String url = msg.user.avatar_large;
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(holder.iv_avatar);

        if (!msg.source.isEmpty()) {
            holder.tv_from.setText(Html.fromHtml(msg.source));
        }

       holder.tv_time.setText(mTimeUtils.buildTimeString(msg.created_at));
         holder.tv_comment_count.setText(Utility.getCountString(msg.comments_count));
        holder.tv_like_count.setText("  " + Utility.getCountString(msg.attitudes_count));
         holder.tv_repost_count.setText(Utility.getCountString(msg.reposts_count));


    }

    public void onBindRepostWeiboViewHolder(RepostWeiboViewHolder holder, int position){
        onBindBaseWeiboViewHolder(holder,position);
       MessageModel msg = mList.get(position).retweeted_status;
        holder.tv_orignal_content.setText(msg.user.name+":"+msg.span);
    }



    @Override
    public RecyclerView.ViewHolder onCreateHeaderView(ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE.ITEM_TYPE_CONTENT_BASE.ordinal()) {
            View view = mLayoutInflater.inflate(R.layout.card_weibo, parent, false);
            BaseWeiboViewHolder holder = new BaseWeiboViewHolder(view, mContext);
            return holder;
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_CONTENT_REPOST.ordinal()) {
            View view = mLayoutInflater.inflate(R.layout.card_weibo_repost, parent, false);
             RepostWeiboViewHolder holder = new RepostWeiboViewHolder(view, mContext);
            return holder;
        }
        else{
            return null;
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateBottomView(ViewGroup parent) {
        return null;
    }

    @Override
    public int getContentItemCount() {
     return mList.size();
    }

    @Override
    public int getContentItemViewType(int position) {
        MessageModel msg= mList.get(position);

        if(msg.retweeted_status==null){
            return  ITEM_TYPE.ITEM_TYPE_CONTENT_BASE.ordinal();
        }
        else {
            return  ITEM_TYPE.ITEM_TYPE_CONTENT_REPOST.ordinal();
        }
    }


    public static class BaseWeiboViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_time)
        public TextView tv_time;
        @InjectView(R.id.tv_username)
        public TextView tv_username;
        @InjectView(R.id.tv_from)
        public TextView tv_from;
        @InjectView(R.id.tv_content)
        public TextView tv_content;
//        public View v;
       @InjectView(R.id.ll_comment)
        public LinearLayout ll_comment;
        @InjectView(R.id.ll_like)
        public LinearLayout ll_like;
        @InjectView(R.id.ll_repost)
        public LinearLayout ll_repost;
        @InjectView(R.id.fl_images)
        public FlowLayout fl_images;
        @InjectView(R.id.iv_avatar)
        public ImageView iv_avatar;

        @InjectView(R.id.tv_comment_count)
        public  TextView tv_comment_count;
        @InjectView(R.id.tv_repost_count)
        public  TextView tv_repost_count;
        @InjectView(R.id.tv_like_count)
        public  TextView tv_like_count;

        public List<SelectableRoundedImageView > listImageView = new ArrayList<SelectableRoundedImageView>();

        public BaseWeiboViewHolder(View itemView,Context context) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            for (int i = 0; i < 9; i++) {
                SelectableRoundedImageView imageView = new SelectableRoundedImageView(context);
                imageView.setCornerRadiiDP(5, 5, 5, 5);
                imageView.setBackgroundColor(context.getResources().getColor(R.color.bg_list_press));
                imageView.setVisibility(View.GONE);
                imageView.setAdjustViewBounds(true);
                listImageView.add(imageView);
                fl_images.addView(imageView);
            }

        }
    }

    public static class RepostWeiboViewHolder extends BaseWeiboViewHolder {

        @InjectView(R.id.tv_orignal_content)
        public  TextView tv_orignal_content;
        public RepostWeiboViewHolder(View itemView, Context context) {
            super(itemView, context);
        }
    }


    /**
     * 图片处理
     */
    private void dealImageLayout(MessageModel msg,BaseWeiboViewHolder holder ,float maxWidth ,int position){


        List<MessageModel.PictureUrl> medias = msg.pic_urls;
        if (medias != null && medias.size() > 0) {
//			int size = 0;
            int mediumSize= (int) ((maxWidth - photoMargin) / 2);
            int smallSize=  (int) ((maxWidth - 2 * photoMargin) / 3);

            int count=medias.size();

        for (int i = 0; i < medias.size(); i++)
            {
                int size = 0;
                // 图片超过九张
                if (i > holder.fl_images.getChildCount() - 1) {
                    break;
                }
                MessageModel.PictureUrl pictureUrl=medias.get(i);
                SelectableRoundedImageView imageView = holder.listImageView.get(i);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setMinimumHeight(smallSize);
                imageView.setMinimumWidth(smallSize);
                FlowLayout.LayoutParams param=new FlowLayout.LayoutParams(smallSize, smallSize); ;
                switch (count) {
                    case 1:
                            param = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                            imageView.setMaxHeight((int) maxWidth);
                            imageView.setMaxWidth((int) maxWidth);
                            imageView.setAdjustViewBounds(true);
                        break;
                    case 3:
                    case 6:
                    case 9:
                        param = new FlowLayout.LayoutParams(smallSize, smallSize);
                        break;
                    case 2:
                    case 4:
                        param = new FlowLayout.LayoutParams(mediumSize, mediumSize);
                        break;
				case 5:
				case 7:
					if(1<i&&i<5){
						param = new FlowLayout.LayoutParams(smallSize, smallSize);
					}
					else{
						param = new FlowLayout.LayoutParams(mediumSize, mediumSize);
					}
					break;

				case 8:
					if(2<i&&i<5){
						param = new FlowLayout.LayoutParams(mediumSize, mediumSize);
					}
					else{
						param = new FlowLayout.LayoutParams(smallSize, smallSize);
					}
					break;

                    default:
                        break;
                }




                imageView.setVisibility(View.VISIBLE);
                imageView.setLayoutParams(param);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                final int index = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });



                if (pictureUrl.getThumbnail()!= null ) {
                    Glide.with(mContext)
                            .load(pictureUrl.getThumbnail())
                            .centerCrop()
                            .crossFade()
                            .into(imageView);
                }

            }
            holder.fl_images.setVisibility(View.VISIBLE);

        }


    }

    public void resetViewHolder(BaseWeiboViewHolder holder) {

        holder.fl_images.setVisibility(View.GONE);

//		holder.tv_like.setText("");
//		holder.tv_comment_count.setText("");
        for (SelectableRoundedImageView imageView : holder.listImageView) {
            imageView.setVisibility(View.GONE);
            imageView.setDrawTag(false);
            imageView.setNeedChange(false);
        }
//        holder.tv_location.setVisibility(View.GONE);
//		holder.tv_device.setText("");
//		holder.tv_content.setText("");
    }

}
