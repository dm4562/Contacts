package com.example.dhruv.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.*;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dhruv on 3/19/15.
 */
public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView detailsName = (TextView) findViewById(R.id.detailsName);
        TextView detailsPhoneNo = (TextView) findViewById(R.id.detailsNumber);
        TextView detailsEmail = (TextView) findViewById(R.id.detailsEmail);
        ImageView detailsPic = (ImageView) findViewById(R.id.detailsImage);

        Intent detailsIntent = getIntent();

        long rawContactId = detailsIntent.getLongExtra("rawContactId", -1);

        String[] displayProjetion = {Data.DISPLAY_NAME_PRIMARY,
                CommonDataKinds.Phone.NUMBER,
                CommonDataKinds.Email.ADDRESS
        };

        String displaySelectionClause = Data.RAW_CONTACT_ID + " = ?";

        String[] displaySelectionArgs = { Long.toString(rawContactId) };

        Cursor detailsCursor = getContentResolver().query(
                Data.CONTENT_URI,
                displayProjetion,
                displaySelectionClause,
                displaySelectionArgs,
                null
        );

        detailsCursor.moveToFirst();

        String name = detailsCursor.getString(detailsCursor.getColumnIndex(Data.DISPLAY_NAME_PRIMARY));
        String phoneNo = detailsCursor.getString(detailsCursor.getColumnIndex(CommonDataKinds.Phone.NUMBER));
        String email = detailsCursor.getString(detailsCursor.getColumnIndex(CommonDataKinds.Email.ADDRESS));

        detailsName.setText(name);
        detailsEmail.setText(email);
        detailsPhoneNo.setText(phoneNo);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
