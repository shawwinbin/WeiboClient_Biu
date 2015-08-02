package com.dudutech.biu.adapter.timeline;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.DeviceUtil;
import com.dudutech.biu.Utils.StatusTimeUtils;
import com.dudutech.biu.Utils.Utility;
import com.dudutech.biu.dao.favo.FavoDao;
import com.dudutech.biu.dao.favo.LikeDao;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.global.MyApplication;
import com.dudutech.biu.model.MessageListModel;
import com.dudutech.biu.model.MessageModel;
import com.dudutech.biu.model.PicSize;
import com.dudutech.biu.ui.picture.PicsActivity;
import com.dudutech.biu.ui.post.PostNewCommentActivity;
import com.dudutech.biu.ui.post.PostNewRepostActivity;
import com.dudutech.biu.ui.timeline.UserHomeActivity;
import com.dudutech.biu.ui.common.StatusContextMenu;
import com.dudutech.biu.ui.common.StatusContextMenuManager;
import com.dudutech.biu.widget.FlowLayout;
import com.dudutech.biu.widget.TagImageVIew;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2014.12.29.
 */
public class TimelineAdapter extends BaseTimelinAdapter<MessageListModel> implements View.OnClickListener, StatusContextMenu.OnStatusContextMenuItemClickListener {

    private StatusTimeUtils mTimeUtils;
    private int photoMargin;
    private float imageMaxWidth;
    private float repostImageMaxWidth;
    float avatarSize;

    public OnClickListener mListenner;

    public TimelineAdapter(Context context, MessageListModel messageListModel) {
        super(context, messageListModel);
        mTimeUtils = StatusTimeUtils.instance(context);
        float padding = context.getResources().getDimension(R.dimen.NormalMargin);
        avatarSize = context.getResources().getDimension(R.dimen.avatar_lst_item);
        photoMargin = context.getResources().getDimensionPixelSize(R.dimen.moment_photo_margin);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        imageMaxWidth = metrics.widthPixels - 4 * padding - avatarSize;
        float smallPadding = context.getResources().getDimension(R.dimen.SmallPadding);
        repostImageMaxWidth = imageMaxWidth - 2 * smallPadding;

    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();
        int postion = (int) v.getTag();
        MessageModel msg = mListModel.get(postion);
        switch (viewId) {
            case R.id.ll_comment:
                PostNewCommentActivity.start(mContext, msg);
                break;
            case R.id.ll_repost:
                PostNewRepostActivity.start(mContext, msg);
                break;

            case R.id.btn_more:
                StatusContextMenuManager.getInstance().toggleContextMenuFromView(v, postion, this);
                break;

            case R.id.ll_like:
                LikeDao likeDao=new LikeDao(msg.id,mContext);
                if(!msg.liked) {
                    likeDao.like();
                    mListModel.get(postion).attitudes_count++;
                    mListModel.get(postion).liked=true;
                    ImageView iv_like = (ImageView) v.findViewById(R.id.iv_like);
                    TextView  tv_like_count= (TextView) v.findViewById(R.id.tv_like_count);
                    setLikeBtn(iv_like,tv_like_count,postion);
                    ScaleAnimation myAnimation_Scale = new ScaleAnimation(1.0f, 1.3f, 1.0f,
                            1.3f, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    myAnimation_Scale.setDuration(300);
                    iv_like.startAnimation(myAnimation_Scale);

                }
                else{
                    likeDao.unLike();
                    mListModel.get(postion).attitudes_count--;
                    mListModel.get(postion).liked=false;
                    ImageView iv_like = (ImageView) v.findViewById(R.id.iv_like);
                    TextView  tv_like_count= (TextView) v.findViewById(R.id.tv_like_count);
                    setLikeBtn(iv_like, tv_like_count, postion);
                }
                break;


        }

    }

    @Override
    public void onFavoClick(int feedItem) {
        StatusContextMenuManager.getInstance().hideContextMenu();
        FavoDao dao = new FavoDao(mListModel.get(feedItem).id,mContext);
        dao.favo();
    }

    @Override
    public void onCopyClick(int feedItem) {
        StatusContextMenuManager.getInstance().hideContextMenu();
        ClipboardManager c= (ClipboardManager)mContext.getSystemService(mContext.CLIPBOARD_SERVICE);
        c.setText(mListModel.get(feedItem).text);

    }



    @Override
    public void onCancelClick(int feedItem) {
        StatusContextMenuManager.getInstance().hideContextMenu();
    }


    public static enum ITEM_TYPE {
        ITEM_TYPE_HEADER,
        ITEM_TYPE_BOTTOM,
        ITEM_TYPE_CONTENT_BASE,
        ITEM_TYPE_CONTENT_REPOST,

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RepostWeiboViewHolder) {
            MessageModel msg = mListModel.getList().get(position);
            onBindRepostWeiboViewHolder((RepostWeiboViewHolder) holder, position);
            dealImageLayout(msg.retweeted_status, ((BaseWeiboViewHolder) holder), repostImageMaxWidth, position);

        } else if (holder instanceof BaseWeiboViewHolder) {
            MessageModel msg = mListModel.getList().get(position);
            onBindBaseWeiboViewHolder((BaseWeiboViewHolder) holder, position);
            dealImageLayout(msg, ((BaseWeiboViewHolder) holder), imageMaxWidth, position);
        }

    }

    public void onBindBaseWeiboViewHolder(BaseWeiboViewHolder holder, int position) {
        resetViewHolder(holder);
        final MessageModel msg = mListModel.get(position);
        holder.tv_content.setText(msg.span);
        holder.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tv_username.setText(msg.user.getName());
        String url = avartarHd?msg.user.avatar_large:msg.user.profile_image_url;

        if (!url.equals(holder.iv_avatar.getTag())) {
            holder.iv_avatar.setTag(url);
            ImageLoader.getInstance().displayImage(url, holder.iv_avatar, Constants.getAvatarOptions(msg.user.name.substring(0, 1)));
        }

        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHomeActivity.startUserHomeActivity(mContext, msg.user);
            }
        });

        holder.ll_comment.setOnClickListener(this);
        holder.ll_comment.setTag(position);
        holder.ll_repost.setOnClickListener(this);
        holder.ll_repost.setTag(position);
        holder.ll_like.setOnClickListener(this);
        holder.ll_like.setTag(position);
        holder.btn_more.setVisibility(View.VISIBLE);
        holder.btn_more.setOnClickListener(this);
        holder.btn_more.setTag(position);
        setLikeBtn(holder.iv_like,holder.tv_like_count,position);
        String source = TextUtils.isEmpty(msg.source) ? "" : Utility.dealSourceString(msg.source);
        holder.tv_time_source.setText(mTimeUtils.buildTimeString(msg.created_at) + " | " + source);
        holder.tv_comment_count.setText(Utility.getCountString(msg.comments_count));

        holder.tv_repost_count.setText(Utility.getCountString(msg.reposts_count));
    }

    private void setLikeBtn(ImageView iv_like,TextView tv_like_count,int position){

        if(mListModel.get(position).liked){
            iv_like.setImageResource(R.mipmap.ic_like_red);
        }
        else{
          iv_like.setImageResource(R.mipmap.ic_like_full);
        }
        tv_like_count.setText("  " + Utility.getCountString(mListModel.get(position).attitudes_count));
    }

    public void onBindRepostWeiboViewHolder(RepostWeiboViewHolder holder, int position) {
        onBindBaseWeiboViewHolder(holder, position);
        MessageModel msg = mListModel.getList().get(position).retweeted_status;
        holder.tv_orignal_content.setText(msg.origSpan);
        holder.tv_orignal_content.setMovementMethod(LinkMovementMethod.getInstance());
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
        } else {
            return null;
        }
    }

    public void setOnClickListenner(OnClickListener onClickListenner) {
        this.mListenner = onClickListenner;
    }

    @Override
    public int getContentItemCount() {
        return mListModel.getList().size();
    }

    @Override
    public int getContentItemViewType(int position) {
        MessageModel msg = mListModel.getList().get(position);

        if (msg.retweeted_status == null) {
            return ITEM_TYPE.ITEM_TYPE_CONTENT_BASE.ordinal();
        } else {
            return ITEM_TYPE.ITEM_TYPE_CONTENT_REPOST.ordinal();
        }
    }


    /**
     * 图片处理
     */
    private void dealImageLayout(final MessageModel msg, BaseWeiboViewHolder holder, float maxWidth, int position) {


        List<MessageModel.PictureUrl> medias = msg.pic_urls;
        if (medias != null && medias.size() > 0) {
//			int size = 0;
            int mediumSize = (int) ((maxWidth - photoMargin) / 2);
            int smallSize = (int) ((maxWidth - 2 * photoMargin) / 3);

            int count = medias.size();

            for (int i = 0; i < count; i++) {
                // 图片超过九张
                if (i > holder.fl_images.getChildCount() - 1) {
                    break;
                }
                final MessageModel.PictureUrl pictureUrl = medias.get(i);

                String imgUrl = pictureUrl.getThumbnail();
                if (netWorkType == DeviceUtil.NetWorkType.wifi && !pictureUrl.isGif()&&picQuantity==2) {
                    imgUrl = pictureUrl.getMedium();
                }

                TagImageVIew imageView = holder.listImageView.get(i);
                imageView.setMinimumHeight(smallSize);
                imageView.setMinimumWidth(smallSize);
                FlowLayout.LayoutParams param = new FlowLayout.LayoutParams(smallSize, smallSize);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                boolean isSizeSaved = false;
                PicSize picSize = null;
                switch (count) {
                    case 1:
                        if(picQuantity!=0) {
                            imgUrl = pictureUrl.getMedium();
                        }
                        else{
                            imgUrl = pictureUrl.getThumbnail();
                        }

                        picSize = MyApplication.picSizeCache.get(imgUrl);
                        if (picSize != null) {
                            param = new FlowLayout.LayoutParams(picSize.getWidth(), picSize.getHeight());

                        } else {
                            param = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                        }

                        imageView.setMaxHeight((int) maxWidth);
                        imageView.setMaxWidth((int) maxWidth);

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
                        if (1 < i && i < 5) {
                            param = new FlowLayout.LayoutParams(smallSize, smallSize);
                        } else {
                            param = new FlowLayout.LayoutParams(mediumSize, mediumSize);
                        }
                        break;

                    case 8:
                        if (2 < i && i < 5) {
                            param = new FlowLayout.LayoutParams(mediumSize, mediumSize);
                        } else {
                            param = new FlowLayout.LayoutParams(smallSize, smallSize);
                        }
                        break;

                    default:
                        break;
                }


                imageView.setVisibility(View.VISIBLE);
                imageView.setLayoutParams(param);

                if (pictureUrl.isGif()) {
                    imageView.setDrawTag(true);
                }

                final int index = i;




                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PicsActivity.start((Activity) mContext, msg, index);
                    }
                });


                if (!TextUtils.isEmpty(imgUrl) && !imgUrl.equals(imageView.getTag())) {

                    ImageLoadingListener imageLoadingListener = null;

                    if (count == 1 && picSize == null) {
                        imageLoadingListener = new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                int width = loadedImage.getWidth();
                                int height = loadedImage.getHeight();
                                ImageView imageView = (ImageView) view;
                                int singleImgMaxHeight = (int) (imageMaxWidth * 2 / 3);
                                if (height > singleImgMaxHeight) {
                                    height = (int) singleImgMaxHeight;
                                }
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                FlowLayout.LayoutParams param = new FlowLayout.LayoutParams(width, height);
                                imageView.setLayoutParams(param);
                                imageView.setImageBitmap(loadedImage);
                                PicSize picSize = new PicSize();
                                picSize.setKey(imageUri);
                                picSize.setWidth(width);
                                picSize.setHeight(height);
                                // 放入内存
                                MyApplication.picSizeCache.put(picSize.getKey(), picSize);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        };
                    }

                    ImageLoader.getInstance().displayImage(imgUrl, imageView, Constants.timelineListOptions, imageLoadingListener);
                    imageView.setTag(imgUrl);

                }

            }
            holder.fl_images.setVisibility(View.VISIBLE);

        }


    }

    public void resetViewHolder(BaseWeiboViewHolder holder) {

        holder.fl_images.setVisibility(View.GONE);


        for (TagImageVIew imageView : holder.listImageView) {
            imageView.setVisibility(View.GONE);
            imageView.setDrawTag(false);
        }

    }

    public class BaseWeiboViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_time_source)
        public TextView tv_time_source;
        @InjectView(R.id.tv_username)
        public TextView tv_username;
        @InjectView(R.id.tv_content)
        public TextView tv_content;

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
        public TextView tv_comment_count;
        @InjectView(R.id.tv_repost_count)
        public TextView tv_repost_count;
        @InjectView(R.id.tv_like_count)
        public TextView tv_like_count;
        @InjectView(R.id.iv_like)
        public ImageView iv_like;

        @InjectView(R.id.btn_more)
        public ImageButton btn_more;

        public List<TagImageVIew> listImageView = new ArrayList<TagImageVIew>();

        public BaseWeiboViewHolder(View itemView, Context context) {
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

            for (int i = 0; i < 9; i++) {
                TagImageVIew imageView = new TagImageVIew(context);
                imageView.setBackgroundColor(context.getResources().getColor(R.color.list_line));
                imageView.setVisibility(View.GONE);
                imageView.setAdjustViewBounds(true);
                listImageView.add(imageView);
                fl_images.addView(imageView);

            }
        }


    }

    public class RepostWeiboViewHolder extends BaseWeiboViewHolder {

        @InjectView(R.id.tv_orignal_content)
        public TextView tv_orignal_content;

        public RepostWeiboViewHolder(View itemView, Context context) {
            super(itemView, context);
        }
    }

    public interface OnClickListener {
        public void onTtemClick(View view, int position);
    }



}
