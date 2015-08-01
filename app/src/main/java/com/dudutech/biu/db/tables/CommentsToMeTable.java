

package com.dudutech.biu.db.tables;

public class CommentsToMeTable
{
	public static  String NAME = "comments_to_me";
	public static final String ID = "id";

	public static final String JSON = "json";

	public static final String CREATE = "create table " + NAME
			+ "("
			+ ID + " integer primary key autoincrement,"
			+ JSON + " text"
			+ ");";
}
