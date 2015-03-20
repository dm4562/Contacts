package com.example.dhruv.contacts;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import static android.provider.ContactsContract.*;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static Cursor contactsCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final String LOG_TAG = MainActivity.class.getSimpleName();

        String[] selectionArgs = {"1"};
        String selectionClause = Contacts.HAS_PHONE_NUMBER + " = ?";
        String sortOrder = Contacts.DISPLAY_NAME_PRIMARY + " ASC";

        ContentResolver contactsResolver = getContentResolver();
        String [] contactsProjection = {
                Contacts._ID,
                Contacts.HAS_PHONE_NUMBER,
                Contacts.DISPLAY_NAME_PRIMARY,
                Contacts.NAME_RAW_CONTACT_ID

        };


        contactsCursor = contactsResolver.query (
                Contacts.CONTENT_URI,
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
                    0);

            ListView view = (ListView) findViewById(R.id.contactslistView);
            view.setAdapter(contactsAdapter);
            view.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        contactsCursor.moveToPosition(position);
        long detailsRawContactId= contactsCursor.getLong(contactsCursor.getColumnIndex(Contacts.NAME_RAW_CONTACT_ID));
        Intent detailsActivityIntent = new Intent(this, DetailsActivity.class)
                .putExtra("detailsRawContactId", detailsRawContactId);
        startActivity(detailsActivityIntent);

    }
}
