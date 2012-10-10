package appspot.smartboxsmu.network;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import appspot.smartboxsmu.parcelable.Contact;

public class ContactHelper {
	public static List<Contact> getAllContact(Context context) {
		List<Contact> contactList = new ArrayList<Contact>();

		// Cursor to get all contact's content
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		// parsing through all contacts
		while (cursor.moveToNext()) {
			// all contact contentProvider has contactId which can be used for
			// further query
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			String hasPhone = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (Boolean.parseBoolean(hasPhone) || Integer.parseInt(hasPhone) == 1) {
				// You know it has a number so now query it like this
				Cursor phones = context.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					
					String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					
					//add to contactlist
					contactList.add(new Contact(name, phoneNumber));
				}
				phones.close();
			}

			Cursor emails = context.getContentResolver().query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
							+ contactId, null, null);
			while (emails.moveToNext()) {
				// This would allow you get several email addresses
				String emailAddress = emails
						.getString(emails
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			}
			emails.close();
		}
		cursor.close();

		return contactList;
	}

}
