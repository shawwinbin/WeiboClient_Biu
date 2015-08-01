
package com.dudutech.biu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dudutech.biu.db.tables.CommentMentionsTimeLineTable;
import com.dudutech.biu.db.tables.CommentTimeLineTable;
import com.dudutech.biu.db.tables.CommentsByMeTable;
import com.dudutech.biu.db.tables.CommentsToMeTable;
import com.dudutech.biu.db.tables.FavListTable;
import com.dudutech.biu.db.tables.GroupTable;
import com.dudutech.biu.db.tables.HomeTimeLineTable;
import com.dudutech.biu.db.tables.MentionsTimeLineTable;
import com.dudutech.biu.db.tables.RepostTimeLineTable;
import com.dudutech.biu.db.tables.StatusCommentTable;
import com.dudutech.biu.db.tables.UserTimeLineTable;
import com.dudutech.biu.db.tables.UsersTable;


public class DataBaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "weibo_data";
	private static int DB_VER = 2;
	
	private static DataBaseHelper instance;
	
	private DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VER);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(UsersTable.CREATE);
		db.execSQL(HomeTimeLineTable.CREATE);
		db.execSQL(UserTimeLineTable.CREATE);
		db.execSQL(MentionsTimeLineTable.CREATE);
		db.execSQL(CommentTimeLineTable.CREATE);
		db.execSQL(CommentMentionsTimeLineTable.CREATE);
		db.execSQL(StatusCommentTable.CREATE);
		db.execSQL(RepostTimeLineTable.CREATE);
		db.execSQL(FavListTable.CREATE);
		db.execSQL(GroupTable.CREATE);
		db.execSQL(CommentsToMeTable.CREATE);
		db.execSQL(CommentsByMeTable.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int from, int to) {
		if (from == 1) {
			db.execSQL(CommentsToMeTable.CREATE);
			db.execSQL(CommentsByMeTable.CREATE);
		}
	}
	
	public static synchronized DataBaseHelper instance(Context context) {
		if (instance == null) {
			instance = new DataBaseHelper(context);
		}
		
		return instance;
	}

}
