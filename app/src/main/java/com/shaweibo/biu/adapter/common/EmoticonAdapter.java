
package com.shaweibo.biu.adapter.common;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.shaweibo.biu.R;
import com.shaweibo.biu.dao.emoticons.EmoticonsDao;


import java.util.ArrayList;
import java.util.Map.Entry;


public class EmoticonAdapter extends BaseAdapter
{
	private static ArrayList<String> mNames = new ArrayList<String>();
	private static ArrayList<Bitmap> mBitmaps = new ArrayList<Bitmap>();
	
	private LayoutInflater mInflater;
	
	public EmoticonAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}
	
	@Override
	public int getCount() {
		return mNames.size();
	}

	@Override
	public String getItem(int position) {
		return mNames.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView==null){
			convertView=mInflater.inflate(R.layout.emoticon_view, null);
		}

		ImageView iv = (ImageView)convertView.findViewById(R.id.emoticon_image);
		iv.setImageBitmap(mBitmaps.get(position));
		return convertView;

	}
	
	public static void init() {
		for (Entry<String, Bitmap> entry : EmoticonsDao.getInstance().bitmaps.entrySet()) {
			if (!mNames.contains(entry.getKey()) && !mBitmaps.contains(entry.getValue())) {
				mNames.add(entry.getKey());
				mBitmaps.add(entry.getValue());
			}
		}
	}

}
