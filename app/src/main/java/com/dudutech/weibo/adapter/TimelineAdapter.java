package com.dudutech.weibo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.StatusTimeUtils;
import com.dudutech.weibo.Utils.Utility;
import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.widget.FlowLayout;
import com.dudutech.weibo.widget.SelectableRoundedImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2014.12.29.
 */
public class TimelineAdapter  extends Adapter<TimelineAdapter.ViewHolder> {
    private  Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<MessageModel> mList;
    private StatusTimeUtils mTimeUtils;
    private int photoMargin;
    public TimelineAdapter(Context context,List<MessageModel> list){
        this.mContext=context;
        this.mList=list;
        mLayoutInflater = LayoutInflater.from(context);
        mTimeUtils=StatusTimeUtils.instance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.card_weibo,parent,false);
        ViewHolder holder = new ViewHolder(view,mContext);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MessageModel msg=mList.get(position);
//        holder.tv_time.setText(msg.);
        holder.tv_content.setText(msg.span);
        holder.tv_username.setText(msg.user.name);
        String url = msg.user.avatar_large;
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(holder.iv_avatar);

        if(!msg.source.isEmpty()){
            holder.tv_from.setText(Html.fromHtml(msg.source));
        }

        holder.tv_time.setText(mTimeUtils.buildTimeString(msg.created_at));
        holder.tv_comment_count.setText(Utility.getCountString(msg.comments_count));
        holder.tv_like_count.setText("  "+Utility.getCountString(msg.attitudes_count));
        holder.tv_repost_count.setText(Utility.getCountString(msg.reposts_count));


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View itemView,Context context) {
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

    /**
     * 图片处理
     */
    private void dealImageLayout(final MessageModel msg ,ViewHolder holder ,float maxWidth ,int position){



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

                SelectableRoundedImageView imageView = holder.listImageView.get(i);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setMinimumHeight(smallSize);
                imageView.setMinimumWidth(smallSize);
                FlowLayout.LayoutParams param=new FlowLayout.LayoutParams(smallSize, smallSize); ;
                DisplayImageOptions options;

                switch (count) {
                    case 1:


                        //处理单张图片
                        if (media.height > 0 && media.width > 0 && SnsBuss.isLoadLargeImage(media.strUrlBig)) {
                            float density = DeviceUtil.getDensity();
                            int heigth = (int) (media.height * density);
                            int width = (int) (media.width * density);
                            if (width >= heigth && width >= maxWidth) {
                                double scale = maxWidth / width;
                                heigth = (int) (scale * heigth);
                                width = (int) maxWidth;
                            }
                            else if (heigth >= width && heigth >= maxWidth) {
                                double scale = maxWidth / heigth;
                                width = (int) (scale * width);
                                heigth = (int) maxWidth;

                            }
                            param = new FlowLayout.LayoutParams(width, heigth);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        else
                        {
                            param = new FlowLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            imageView.setMaxHeight((int) maxWidth);
                            imageView.setMaxWidth((int) maxWidth);
                            imageView.setAdjustViewBounds(true);
                        }

                        if (SnsBuss.isMomentSending(moment)) {
                            // sending moment:
                            imgUrl = ImageLoaderConst.URI_FILE + media.strFilePath;
                        } else if (FileUtil.isFileExists(media.strFilePath)) {
                            // send success moment:刚发送成功的动态
                            imgUrl = ImageLoaderConst.URI_FILE + media.strFilePath;
                        } else {
                            // server moment:
                            if (media.iType == GlobalConst.SNS_DATA_PHOTO) {
                                if (SnsBuss.isLoadLargeImage(media.strUrlBig)) {
                                    imgUrl = media.getRealBigImageUrl();
                                } else {
                                    imgUrl = media.strUrlSmall;
                                }
                            }
                        }

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



                if (media.iType == GlobalConst.SNS_DATA_GIF) {
                    imageView.setDrawTag(true);
                }
                imageView.setVisibility(View.VISIBLE);
                imageView.setLayoutParams(param);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                final int index = i;
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeLineOper.openPhotoBrowse(moment.strMomentID, moment.strMomentID, moment.medias, index,
                                SnsBuss.isMomentSending(moment));
                    }
                });



                if (imgUrl != null && !imgUrl.equals(imageView.getTag())) {


                    if (SnsBuss.isMomentSending(moment)) {
                        options = ImageOptions.getInstance().getTimeLineImageOptionByNoCache(medias.size());
                    } else {
                        options = ImageOptions.getInstance().getTimeLineImageOptionByCache(medias.size());
                        if (imgUrl.startsWith(ImageLoaderConst.URI_FILE) && !new File(imgUrl.replace(ImageLoaderConst.URI_FILE, "")).exists()) {
                            imgUrl = media.getRealImageUrl();
                        }
                    }
                    imageView.setTag(imgUrl);

                    ImageLoader.getInstance().displayImage(imgUrl, imageView, options,
                            new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    // 如果动态发布成功，删除临时的图片资源
                                    if (media.width > 0 || loadedImage == null || loadedImage.isRecycled()) {
                                        return;
                                    }
                                    changeMomentMediaWH(moment, loadedImage.getWidth(), loadedImage.getHeight());
                                }
                            });

//					ImageLoader.getInstance().displayImage(imgUrl, imageView, options
//							);

                    // 停止计时

                }

            }
            holder.fl_images.setVisibility(View.VISIBLE);

        }



    }
}
