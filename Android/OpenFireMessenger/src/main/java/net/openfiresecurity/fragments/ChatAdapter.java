/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.fragments;

import net.openfiresecurity.helper.EmoticonManager;
import net.openfiresecurity.helper.ResourceManager;
import net.openfiresecurity.messenger.MainService;
import net.openfiresecurity.messenger.R;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.text.SpannedString;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

class ChatAdapter extends CursorAdapter {

	private String drawablePrefsSender = "";
	private String drawablePrefsReceiver = "";

	@SuppressWarnings("deprecation")
	public ChatAdapter(Context context, Cursor c) {
		super(context, c);
		drawablePrefsSender = MainService.prefs.getString("chatDrawableSender");
		drawablePrefsReceiver = MainService.prefs
				.getString("chatDrawableReceiver");
	}

	/* ViewHolder for better performance */
	static class ViewHolder {
		TextView message;
		TextView chattime;
		String content;
		String source;
		String time;
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {

		View view = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.list_chat, parent, false);

		ViewHolder holder = new ViewHolder();

		holder.message = (TextView) view.findViewById(R.id.message_text);
		holder.chattime = (TextView) view.findViewById(R.id.tvchattime);

		holder.message.setTextColor(Color.rgb(255, 255, 255));
		holder.message.setShadowLayer(1.5f, 1.5f, 1.3f, Color.rgb(15, 15, 15));

		view.setTag(holder);

		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor c) {
		ViewHolder holder = (ViewHolder) view.getTag();

		holder.content = c.getString(c.getColumnIndex(c.getColumnName(1)));
		// String email = c.getString(c.getColumnIndex(c.getColumnName(2)));
		holder.source = c.getString(c.getColumnIndex(c.getColumnName(3)));
		holder.time = c.getString(c.getColumnIndex(c.getColumnName(4)));

		holder.chattime.setText(holder.time);

		SpannedString spanned = new SpannedString(
				EmoticonManager.getSmiledText(context, holder.content));

		holder.message.setText(spanned);
		holder.message.setTypeface(ResourceManager
				.getFontByName(MainService.prefs.getString("chat_font")));

		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		if (isMine(holder.source)) {
			holder.message.setBackgroundResource(setBackgroundFromPrefs(
					drawablePrefsSender, true));
			lp.gravity = Gravity.RIGHT;
			lp.leftMargin = 60;
			lp.rightMargin = 30;
		} else {
			holder.message.setBackgroundResource(setBackgroundFromPrefs(
					drawablePrefsReceiver, false));
			lp.gravity = Gravity.LEFT;
			lp.leftMargin = 30;
			lp.rightMargin = 60;
		}
		holder.message.setLayoutParams(lp);

		LayoutParams lptime = (LayoutParams) holder.chattime.getLayoutParams();
		if (isMine(holder.source)) {
			lptime.gravity = Gravity.RIGHT;
			lptime.leftMargin = 40;
		} else {
			lptime.gravity = Gravity.LEFT;
			lptime.leftMargin = 55;
		}
		holder.chattime.setLayoutParams(lptime);
	}

	private int setBackgroundFromPrefs(String resourcename, boolean sender) {
		if (resourcename.equals("green")) {
			if (sender) {
				return R.drawable.chat_bubble_sender_lightgreen;
			} else {
				return R.drawable.chat_bubble_receiver_darkgreen;
			}
		} else if (resourcename.equals("blue")) {
			if (sender) {
				return R.drawable.chat_bubble_sender_lightblue;
			} else {
				return R.drawable.chat_bubble_receiver_darkblue;
			}
		} else if (resourcename.equals("orange")) {
			if (sender) {
				return R.drawable.chat_bubble_sender_lightorange;
			} else {
				return R.drawable.chat_bubble_receiver_darkorange;
			}
		} else {
			if (sender) {
				return R.drawable.chat_bubble_sender_lightgreen;
			} else {
				return R.drawable.chat_bubble_receiver_darkgreen;
			}
		}
	}

	private boolean isMine(String source) {
		return source.equals("1");
	}
}