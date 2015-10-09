
package com.shaweibo.biu.db.tables;

public class CommentsByMeTable
{
	public static final  String NAME = "comments_by_me";
	public static final String ID = "id";

	public static final String JSON = "json";

	public static final String CREATE = "create table " + NAME
			+ "("
			+ ID + " integer primary key autoincrement,"
			+ JSON + " text"
			+ ");";
}
