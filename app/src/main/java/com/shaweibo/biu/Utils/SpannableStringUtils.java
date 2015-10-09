/* 
 * Copyright (C) 2014 Peter Cai
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

package com.shaweibo.biu.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;

import com.shaweibo.biu.dao.emoticons.EmoticonsDao;
import com.shaweibo.biu.model.CommentModel;
import com.shaweibo.biu.model.MessageModel;
import com.shaweibo.biu.widget.StickerImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/* This class is modified from qii/weiciyuan */
public class SpannableStringUtils
{
	private static final String TAG = SpannableStringUtils.class.getSimpleName();
	
	private static final Pattern PATTERN_WEB = Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
	private static final Pattern PATTERN_TOPIC = Pattern.compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
	private static final Pattern PATTERN_MENTION = Pattern.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
	private static final Pattern PATTERN_EMOTICON = Pattern.compile("\\[(\\S+?)\\]");

	public static final String HTTP_SCHEME = "http://";
	public static final String TOPIC_SCHEME = "topic://";
	public static final String MENTION_SCHEME = "user://";

	public static SpannableString span(Context context, String text ){
		return span(context,text,false);
	}

	public static SpannableString span(Context context, String text ,boolean isLight) {
		SpannableString ss = SpannableString.valueOf(text);
		Linkify.addLinks(ss, PATTERN_WEB, HTTP_SCHEME);
		Linkify.addLinks(ss, PATTERN_TOPIC, TOPIC_SCHEME);
		Linkify.addLinks(ss, PATTERN_MENTION, MENTION_SCHEME);
		
		// Convert to our own span
		URLSpan[] spans = ss.getSpans(0, ss.length(), URLSpan.class);
		for (URLSpan span : spans) {
			WeiboSpan s = new WeiboSpan(span.getURL(),isLight);
			int start = ss.getSpanStart(span);
			int end = ss.getSpanEnd(span);
			ss.removeSpan(span);
			ss.setSpan(s, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		// Match Emoticons
		Matcher matcher = PATTERN_EMOTICON.matcher(ss);
		while (matcher.find()) {
			// Don't be too long
			if (matcher.end() - matcher.start() < 8) {
				String iconName = matcher.group(0);
				Bitmap bitmap = EmoticonsDao.getInstance().bitmaps.get(iconName);
				
				if (bitmap != null) {
					StickerImageSpan span = new StickerImageSpan(context, bitmap);
					ss.setSpan(span, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		
		return ss;
	}

	public static SpannableString getSpan(Context context, MessageModel msg) {

		return getSpan(context,msg,false);
	}



	public static SpannableString getSpan(Context context, MessageModel msg,boolean isLight) {
		if (msg.span == null) {
			msg.span = span(context, msg.text,isLight);
		}

		return msg.span;
	}

	public static SpannableString getOrigSpan(Context context, MessageModel orig) {
		return getOrigSpan(context,orig,false);
	}

	public static SpannableString getOrigSpan(Context context, MessageModel orig,boolean isLight) {
		if (orig.origSpan == null) {

			String username = "";

			if (orig.user != null) {
				username = orig.user.getName();
				username = "@" + username + ":";
			}

			orig.origSpan = span(context, username + orig.text,isLight);
		}

		return orig.origSpan;
	}

	public static SpannableString getCommentSpan(Context context, CommentModel orig) {
		if (orig.span == null) {


			String username = "";

			if (orig.user != null) {
				username = orig.user.getName();
				username = "@" + username + ":";
			}

			orig.span = span(context, username + orig.text);
		}

		return orig.span;
	}


}
