package com.shaweibo.biu.ui.post;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.shaweibo.biu.R;
import com.shaweibo.biu.adapter.common.EmoticonAdapter;
import com.shaweibo.biu.dao.emoticons.EmoticonsDao;
import com.shaweibo.biu.widget.StickerImageSpan;

public class EmoticonFragment extends Fragment {

    private GridView gv_emoji;
    private EmoticonAdapter mAdapter;

    OnEmoticonOnClinckListener  mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View mView = null;
        mView = inflater.inflate(R.layout.fragment_emoji_panel, null);
        gv_emoji = (GridView) mView.findViewById(R.id.gv_emoji);


        return mView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new EmoticonAdapter(getActivity());
        gv_emoji.setAdapter(mAdapter);
        gv_emoji.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stubf
                onClickItem(arg2, mAdapter.getItem(arg2));
            }
        });
    }


    private void onClickItem(int position, String emoctionName) {
        // delete button pressed


        Bitmap bitmap = EmoticonsDao.getInstance().bitmaps.get(emoctionName);

        StickerImageSpan imageSpan = new StickerImageSpan(getActivity(), bitmap);

        SpannableString spannableString = new SpannableString(
                emoctionName
                        .substring(0, emoctionName.length()));
        spannableString.setSpan(imageSpan, 0,
                emoctionName.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        onEmojiClick(spannableString);
        if(mListener!=null) {
            mListener.onEmoticonClick(spannableString);
        }

    }

    public void  setOnEmoticonListener(OnEmoticonOnClinckListener listener){
        mListener=listener;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof  OnEmoticonOnClinckListener){
            mListener= (OnEmoticonOnClinckListener) activity;
        }
    }

    public void onEmojiClick(SpannableString spannableString) {


//			int maxLength;
//
//
//			if (et_content.getText().toString().length() + spannableString.length() <= maxLength) {
//				Spannable contentSpann = et_content.getText();
//				int select = et_content.getSelectionStart();
//				SpannableStringBuilder strBuilder = new SpannableStringBuilder();
//				strBuilder.append(contentSpann.subSequence(0, select));
//				strBuilder.append(spannableString);
//				strBuilder.append(contentSpann.subSequence(select, contentSpann.length()));
//
//				et_content.setText(strBuilder);
//				int length = select + spannableString.length();
//				if (length > et_content.length()) {
//					length = et_content.length();
//				}
//				et_content.setSelection(length);
//			} else {
//				String msg = String.format(getString(R.string.moment_comment_length_error),
//						maxLength);
//
//				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//			}

    }

//	public void onEmojiDelete() {
//		int start = et_content.getSelectionStart();
//		int end = et_content.getSelectionEnd();
//		String s = et_content.getText().toString();
//		if (start == end && start != 0) {
//			start--;
//		}
//
//		if (start == end && start == 0) {
//			return;
//		}
//
//		String deStr = getDeleteSub(s, start);
//		// make sure that the string is like the name
//		if (!TextUtils.isEmpty(deStr) && deStr.length() > 1) {
//			int id = EmojiUtil.getResIdByName(deStr);
//			if (id != 0) {
//				start = start - deStr.length();
//				et_content.setText(EmojiUtil.getExpressionString(getActivity(),
//						s.substring(0, start + 1) + s.substring(end, s.length())));
//
//			} else {
//				et_content.setText(EmojiUtil.getExpressionString(getActivity(),
//						s.substring(0, start) + s.substring(end, s.length())));
//			}
//
//		} else {
//			et_content.setText(EmojiUtil.getExpressionString(getActivity(),
//					s.substring(0, start) + s.substring(end, s.length())));
//		}
//
//		if (s.length() > 0) {
//			et_content.setSelection(et_content.getText().toString().length());
//		}
//
//	}
//
//	private String getDeleteSub(String s, int start) {
//		String deStr = s.substring(start, start + 1);
//		// get the emoji's temp str
//		if (!TextUtils.isEmpty(deStr) && deStr.equals(")")) {
//			for (int i = start - 1; i >= 0; i--) {
//				String ch = s.substring(i, i + 1);
//				if (TextUtils.isEmpty(ch)) {
//					deStr = null;
//					break;
//				}
//
//				if (ch.equals(")")) {
//					deStr = null;
//					break;
//				}
//
//				if (ch.equals("(")) {
//					deStr = ch + deStr;
//					break;
//				}
//
//				if (!Utils.isEnglishChar(ch.charAt(0)) && !ch.equals("/")) {
//					deStr = null;
//					break;
//				}
//
//				deStr = ch + deStr;
//			}
//		}
//
//		return deStr;
//	}


    public interface  OnEmoticonOnClinckListener{
       void onEmoticonClick(SpannableString emojiSpane);
    }

}
