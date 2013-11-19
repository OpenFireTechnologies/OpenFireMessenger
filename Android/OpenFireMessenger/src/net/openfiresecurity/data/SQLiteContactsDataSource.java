/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SQLiteContactsDataSource {

    // Database fields

    private SQLiteDatabase database;
    private final MySQLiteContactsHelper dbHelper;

    private final String[] allColumns = {MySQLiteContactsHelper.COLUMN_ID,
            MySQLiteContactsHelper.COLUMN_EMAIL,
            MySQLiteContactsHelper.COLUMN_NAME};

    public SQLiteContactsDataSource(Context context) {
        dbHelper = new MySQLiteContactsHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor getCursor() {
        return database.query(MySQLiteContactsHelper.TABLE_CONTACTS,
                allColumns, null, null, null, null, null, null);
    }

    public int getId(String email, String name) {
        Cursor c = database.rawQuery("SELECT _id FROM "
                + MySQLiteContactsHelper.TABLE_CONTACTS
                + " WHERE TRIM(email) = '" + email.trim()
                + "' AND TRIM(name) = '" + name.trim() + "' LIMIT 1;", null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("_id"));
        c.close();
        return id;
    }

    public int getId(String email) {
        Cursor c = database.rawQuery("SELECT _id FROM "
                + MySQLiteContactsHelper.TABLE_CONTACTS
                + " WHERE TRIM(email) = '" + email.trim() + "'; LIMIT 1", null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("_id"));
        c.close();
        return id;
    }

    public String getEmail(int id) {
        Cursor c = database.rawQuery("SELECT email FROM "
                + MySQLiteContactsHelper.TABLE_CONTACTS
                + " WHERE TRIM(_id) = '" + id + "'", null);
        c.moveToFirst();

        String email = c.getString(c.getColumnIndex("email"));
        c.close();
        return email;
    }

    public String getName(int id) {
        Cursor c = database.rawQuery("SELECT name FROM "
                + MySQLiteContactsHelper.TABLE_CONTACTS
                + " WHERE TRIM(_id) = '" + id + "'", null);
        c.moveToFirst();

        String name = c.getString(c.getColumnIndex("name"));
        c.close();
        return name;
    }

    public SQLiteContacts createContact(String email, String name) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteContactsHelper.COLUMN_EMAIL, email);
        values.put(MySQLiteContactsHelper.COLUMN_NAME, name);
        long insertId = database.insert(MySQLiteContactsHelper.TABLE_CONTACTS,
                null, values);
        Cursor cursor = database.query(MySQLiteContactsHelper.TABLE_CONTACTS,
                allColumns,
                MySQLiteContactsHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        SQLiteContacts newContact = cursorToContacts(cursor);
        cursor.close();
        return newContact;
    }

    public void deleteContact(SQLiteContacts contact) {
        long id = contact.getId();
        database.delete(MySQLiteContactsHelper.TABLE_CONTACTS,
                MySQLiteContactsHelper.COLUMN_ID + " = " + id, null);
    }

    public void deleteContact(int id) {
        database.delete(MySQLiteContactsHelper.TABLE_CONTACTS,
                MySQLiteContactsHelper.COLUMN_ID + " = " + id, null);
    }

    public List<SQLiteContacts> getAllContacts() {

        List<SQLiteContacts> contacts = new ArrayList<SQLiteContacts>();

        Cursor cursor = database.query(MySQLiteContactsHelper.TABLE_CONTACTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            SQLiteContacts contact = cursorToContacts(cursor);
            contacts.add(contact);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return contacts;
    }

    private SQLiteContacts cursorToContacts(Cursor cursor) {

        SQLiteContacts comment = new SQLiteContacts();
        comment.setId(cursor.getLong(0));
        comment.setEmail(cursor.getString(1));
        comment.setName(cursor.getString(2));
        return comment;
    }

}
