package com.example.dhruv.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static android.provider.ContactsContract.CommonDataKinds.*;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String LOG_TAG = MainActivity.class.getSimpleName();

        String[] selectionArgs = {};
        String selectionClause = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC";

        ContentResolver contactsResolver = getContentResolver();
        String [] contactsProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY

        };

        Cursor contactsCursor;
        contactsCursor = contactsResolver.query (
                ContactsContract.Contacts.CONTENT_URI,
                contactsProjection,
                selectionClause,
                selectionArgs,
                sortOrder
        );


        if (contactsCursor == null) {
            Log.e(LOG_TAG, "contactsCursor is null");
        }

        else if (contactsCursor.getCount() == 0) {
            Log.e(LOG_TAG, " contactsCursor is empty");
        }

        else {

            ContactsCursorAdapter contactsAdapter = new ContactsCursorAdapter(
                    this,
                    contactsCursor,
                    0,
                    contactsResolver);

            ListView view = (ListView) findViewById(R.id.contactslistView);
            view.setAdapter(contactsAdapter);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
