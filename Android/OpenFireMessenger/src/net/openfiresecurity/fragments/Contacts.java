/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.fragments;

import java.util.List;

import net.openfiresecurity.messenger.MainService;
import net.openfiresecurity.messenger.MainView;
import net.openfiresecurity.messenger.R;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.ListFragment;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.Toast;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;

public class Contacts extends ListFragment implements OnItemLongClickListener {

	public List<String> idList, emailList;
	private MainView main;
	private ContactsAdapter adapter;
	private Cursor c;
	private AlertDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		adapter = new ContactsAdapter(main, c);
		setListAdapter(adapter);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setLongClickable(true);
		getListView().setOnItemLongClickListener(this);
		getListView().setDivider(null);
		getListView().setDividerHeight(0);
		getListView().setBackgroundDrawable(
				MainService.res.getDrawable(R.drawable.listview_gradient_two));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id1) {
		super.onListItemClick(l, v, position, id1);
		main.changeContact(Integer.parseInt(adapter.contacts.get(position).id));
	}

	@Override
	public boolean onItemLongClick(final android.widget.AdapterView<?> arg0,
			View arg1, final int arg2, long arg3) {
		LayoutInflater li = LayoutInflater.from(main);
		View promptsView = li.inflate(R.layout.prompt_contacts_options, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(main);
		alertDialogBuilder.setView(promptsView);

		Button deleteContact = (Button) promptsView
				.findViewById(R.id.bPromptContactsDeleteContact);
		Button deleteChat = (Button) promptsView
				.findViewById(R.id.bPromptContactsDeleteChat);
		deleteContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				MainService.deleteContact(Integer.parseInt(adapter.contacts
						.get(arg2).id));
				Toast.makeText(main, "Contact deleted!", Toast.LENGTH_LONG)
						.show();
			}

		});
		deleteChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				MainService.deleteChat(adapter.contacts.get(arg2).email);
				Toast.makeText(main, "Chat deleted!", Toast.LENGTH_LONG).show();
			}

		});

		alertDialogBuilder.setNeutralButton("Return",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		dialog = alertDialogBuilder.create();
		dialog.show();
		return true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		main = ((MainView) getActivity());
		c = MainService.contactsDB.getCursor();
	}

	/**
	 * Refreshes the contacts list
	 */
	public void refreshList() {
		getContacts();
	}

	private void getContacts() {
		c = MainService.contactsDB.getCursor();
		adapter.swapCursor(c);
		adapter.notifyDataSetChanged();
	}

}