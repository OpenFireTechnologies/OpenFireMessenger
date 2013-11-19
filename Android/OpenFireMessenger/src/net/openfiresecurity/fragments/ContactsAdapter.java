/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.openfiresecurity.messenger.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends CursorAdapter {

    final List<Contact> contacts = new ArrayList<Contact>();

    public class Contact {
        String id;
        String name;
        String email;

        public Contact(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Contact get(int position) {
            Cursor cursor = getCursor();
            Contact contact = null;
            if (cursor.moveToPosition(position)) {
                contact = new Contact(cursor.getString(cursor
                        .getColumnIndex(cursor.getColumnName(0))),
                        cursor.getString(cursor.getColumnIndex(cursor
                                .getColumnName(1))), cursor.getString(cursor
                        .getColumnIndex(cursor.getColumnName(2))));
            }

            return contact;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    @SuppressWarnings("deprecation")
    public ContactsAdapter(Context context, Cursor c) {
        super(context, c);
    }

    /* ViewHolder for better performance */
    static class ViewHolder {
        TextView contactName;
        TextView contactLastMessage;
        ImageView contactPicture;
        String id;
        String name;
        String email;
    }

    @Override
    public void bindView(View view, Context arg1, Cursor c) {
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.id = c.getString(c.getColumnIndex(c.getColumnName(0)));
        holder.name = c.getString(c.getColumnIndex(c.getColumnName(2)));
        holder.email = c.getString(c.getColumnIndex(c.getColumnName(1)));

        contacts.add(new Contact(holder.id, holder.name, holder.email));

        holder.contactName.setText(holder.name);
        holder.contactLastMessage.setText(holder.email);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        contacts.clear();
        return super.swapCursor(newCursor);
    }

    @Override
    public View newView(Context arg0, Cursor c, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_contact, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.contactName = (TextView) view.findViewById(R.id.tvContactName);
        holder.contactLastMessage = (TextView) view
                .findViewById(R.id.tvLastMessage);
        @SuppressWarnings("unused")
        ImageView contactPicture = (ImageView) view
                .findViewById(R.id.ivContactPicture);

        view.setTag(holder);

        return view;
    }
}
