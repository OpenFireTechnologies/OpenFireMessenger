/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteMessagesDataSource {

	// Database fields

	private SQLiteDatabase database;
	private final MySQLiteMessagesHelper dbHelper;

	private final String[] allColumns = { MySQLiteMessagesHelper.COLUMN_ID,
			MySQLiteMessagesHelper.COLUMN_CONTENT,
			MySQLiteMessagesHelper.COLUMN_EMAIL,
			MySQLiteMessagesHelper.COLUMN_SOURCE,
			MySQLiteMessagesHelper.COLUMN_TIME };

	public SQLiteMessagesDataSource(Context context) {
		dbHelper = new MySQLiteMessagesHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Cursor getCursor() {
		return database.query(MySQLiteMessagesHelper.TABLE_MESSAGES,
				allColumns, null, null, null, null, null, null);
	}

	public Cursor getCursorFromContact(String email) {
		return database.query(MySQLiteMessagesHelper.TABLE_MESSAGES,
				allColumns, MySQLiteMessagesHelper.COLUMN_EMAIL + " = '"
						+ email + "'", null, null, null, null, null);
	}

	public int getId(String content, String email) {
		Cursor c = database.rawQuery("SELECT _id FROM "
				+ MySQLiteMessagesHelper.TABLE_MESSAGES
				+ " WHERE TRIM(content) = '" + content.trim()
				+ "' AND TRIM(email) = '" + email.trim() + "'; LIMIT 1", null);
		c.moveToFirst();
		int id = c.getInt(c.getColumnIndex("_id"));
		c.close();
		return id;
	}

	public String getEmail(int id) {
		Cursor c = database.rawQuery("SELECT email FROM "
				+ MySQLiteMessagesHelper.TABLE_MESSAGES
				+ " WHERE TRIM(_id) = '" + id + "'", null);
		c.moveToFirst();

		String email = c.getString(c.getColumnIndex("email"));
		c.close();
		return email;
	}

	public String getContent(int id) {
		Cursor c = database.rawQuery("SELECT content FROM "
				+ MySQLiteMessagesHelper.TABLE_MESSAGES
				+ " WHERE TRIM(_id) = '" + id + "'", null);
		c.moveToFirst();

		String name = c.getString(c.getColumnIndex("content"));
		c.close();
		return name;
	}

	public String getSource(int id) {
		Cursor c = database.rawQuery("SELECT source FROM "
				+ MySQLiteMessagesHelper.TABLE_MESSAGES
				+ " WHERE TRIM(_id) = '" + id + "'", null);
		c.moveToFirst();

		String name = c.getString(c.getColumnIndex("content"));
		c.close();
		return name;
	}

	public SQLiteMessages createMessage(String content, String email,
			String source, String time) {

		ContentValues values = new ContentValues();
		values.put(MySQLiteMessagesHelper.COLUMN_CONTENT, content);
		values.put(MySQLiteMessagesHelper.COLUMN_EMAIL, email);
		values.put(MySQLiteMessagesHelper.COLUMN_SOURCE, source);
		values.put(MySQLiteMessagesHelper.COLUMN_TIME, time);
		long insertId = database.insert(MySQLiteMessagesHelper.TABLE_MESSAGES,
				null, values);
		Cursor cursor = database.query(MySQLiteMessagesHelper.TABLE_MESSAGES,
				allColumns,
				MySQLiteMessagesHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();

		SQLiteMessages newMessage = cursorToMessages(cursor);
		cursor.close();
		return newMessage;
	}

	public void deleteMessage(SQLiteMessages message) {
		long id = message.getId();
		database.delete(MySQLiteMessagesHelper.TABLE_MESSAGES,
				MySQLiteMessagesHelper.COLUMN_ID + " = " + id, null);
	}

	public void deleteMessage(int id) {
		database.delete(MySQLiteMessagesHelper.TABLE_MESSAGES,
				MySQLiteMessagesHelper.COLUMN_ID + " = " + id, null);
	}

	public void deleteChat(int id) {
		database.delete(MySQLiteMessagesHelper.TABLE_MESSAGES,
				MySQLiteMessagesHelper.COLUMN_ID + " = '" + id + "'", null);
	}

	public void deleteChat(String email) {
		database.delete(MySQLiteMessagesHelper.TABLE_MESSAGES,
				MySQLiteMessagesHelper.COLUMN_EMAIL + " = '" + email + "'",
				null);
	}

	public List<SQLiteMessages> getAllMessages(String email) {

		List<SQLiteMessages> messages = new ArrayList<SQLiteMessages>();

		Cursor cursor = database.query(MySQLiteMessagesHelper.TABLE_MESSAGES,
				allColumns, "email like '" + email + "'", null, null, null,
				null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			SQLiteMessages message = cursorToMessages(cursor);
			messages.add(message);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return messages;
	}

	private SQLiteMessages cursorToMessages(Cursor cursor) {

		SQLiteMessages message = new SQLiteMessages();
		message.setId(cursor.getLong(0));
		message.setContent(cursor.getString(1));
		message.setEmail(cursor.getString(2));
		return message;
	}

}
