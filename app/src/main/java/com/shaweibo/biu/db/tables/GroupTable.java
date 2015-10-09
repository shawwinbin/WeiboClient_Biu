

package com.shaweibo.biu.db.tables;

public class GroupTable
{
	public static final String NAME = "groups";

	public static final String ID = "id";

	public static final String JSON = "json";

	public static final String CREATE = "create table " + NAME
						+ "("
						+ ID + " integer primary key autoincrement,"
						+ JSON + " text"
						+ ");";
}
