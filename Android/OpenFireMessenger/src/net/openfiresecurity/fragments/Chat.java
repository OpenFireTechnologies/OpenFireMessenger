/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import net.openfiresecurity.helper.MessageSender;
import net.openfiresecurity.helper.ResourceManager;
import net.openfiresecurity.messenger.MainService;
import net.openfiresecurity.messenger.MainView;
import net.openfiresecurity.messenger.R;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import android.accounts.AccountManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Chat extends Fragment implements OnClickListener,
		OnLongClickListener {

	/* Delaying sending for 2 seconds */
	private final Handler mHandler = new Handler();
	private boolean canSend = true;

	private Cursor c;

	private static TextView tvChatUser;
	private String email = "", name = "";

	private EditText etChatMessage;
	private ImageView ivChatSend;
	private ListView lvChatMessages;
	private Activity context;
	private ChatAdapter adapter;
	ArrayList<String> msg = new ArrayList<String>();

	public void changeActionBarTitle() {
		MainView.ac.setTitle(name);
	}

	public void changeContact(int id) {
		email = MainService.contactsDB.getEmail(id);
		name = MainService.contactsDB.getName(id);
		MainView.ac.setTitle(name);
		tvChatUser.setText(name);
		MainView.mViewPager.setCurrentItem(1);
		refreshList();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_chat, container, false);

		etChatMessage = (EditText) view.findViewById(R.id.etChatMessage);
		etChatMessage.setTypeface(ResourceManager.L_LATO);
		etChatMessage.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				String content = s.toString();
				if (canSend) {
					if (!content.trim().isEmpty()) {
						ivChatSend.setVisibility(View.VISIBLE);
					} else {
						ivChatSend.setVisibility(View.INVISIBLE);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		});

		tvChatUser = (TextView) view.findViewById(R.id.tvChatUser);
		ivChatSend = (ImageView) view.findViewById(R.id.ivChatSendEnabled);
		ivChatSend.setVisibility(View.INVISIBLE);
		ivChatSend.setOnClickListener(this);
		ivChatSend.setOnLongClickListener(this);
		lvChatMessages = (ListView) view.findViewById(R.id.lvChatMessages);

		lvChatMessages.setDivider(null);
		lvChatMessages.setDividerHeight(0);

		adapter = new ChatAdapter(getActivity(), c);
		lvChatMessages.setAdapter(adapter);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
		c = null;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ivChatSendEnabled:
			if (!(email.trim().isEmpty())) {
				ivChatSend.setVisibility(View.INVISIBLE);
				canSend = false;
				mHandler.removeCallbacks(mUpdateTimeTask);
				mHandler.postDelayed(mUpdateTimeTask, 2000);

				String message = MainService.filter(etChatMessage.getText()
						.toString());
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",
						Locale.getDefault());
				String currentTime = sdf.format(new Date());
				MainService.createMessage(message, email, "1", currentTime);
				new MessageSender().execute(
						AccountManager.get(context).getUserData(
								MainView.account, "email"),
						email,
						message,
						AccountManager.get(context).getUserData(
								MainView.account, "hash"));
				etChatMessage.setText("");
			} else {
				MainView.mViewPager.setCurrentItem(0);
			}
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	private final Runnable mUpdateTimeTask = new Runnable() {
		@Override
		public void run() {
			canSend = true;
			if (!etChatMessage.getText().toString().trim().isEmpty()) {
				ivChatSend.setVisibility(View.VISIBLE);
			}
		}
	};

	/**
	 * Refreshes the Chat Window
	 */
	public void refreshList() {
		c = MainService.messagesDB.getCursorFromContact(email);
		adapter.swapCursor(c);
	}
}