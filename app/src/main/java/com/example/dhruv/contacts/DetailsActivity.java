package com.example.dhruv.contacts;

import android.content.ContentUris;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.*;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dhruv and nikhil on 3/19/15.
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

        long detailsRawContactId = detailsIntent.getLongExtra("detailsRawContactId", -1);

        String[] displayProjection = {
                Data._ID,
                Data.MIMETYPE,
                Data.DATA1,
                Data.DATA2,
                Data.DATA3,
                Data.DATA4,
                Data.DATA5,
                Data.DATA6,
                Data.DATA7,
                Data.DATA8,
                Data.DATA9,
                Data.DATA10,
                Data.DATA11,
                Data.DATA12,
                Data.DATA13,
                Data.DATA14,
                Data.DATA15

        };

        String displaySelectionClause = Data.RAW_CONTACT_ID + " = ?"  + " AND (" +
                Data.MIMETYPE + " = '" + CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'" +
                " OR " + Data.MIMETYPE + " = '" + CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'" +
                " OR " + Data.MIMETYPE + " = '" + CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'" +
                " OR " + Data.MIMETYPE + " = '" + CommonDataKinds.Email.CONTENT_ITEM_TYPE + "')";

        String[] displaySelectionArgs = { Long.toString(detailsRawContactId) };

        Cursor detailsCursor = getContentResolver().query(
                Data.CONTENT_URI,
                displayProjection,
                displaySelectionClause,
                displaySelectionArgs,
                null
        );

        detailsCursor.moveToFirst();

        int imageId = detailsCursor.getInt(detailsCursor.getColumnIndex(Data.DATA14));
        Uri imageURI = ContentUris.withAppendedId(DisplayPhoto.CONTENT_URI, imageId);
        try {
            AssetFileDescriptor imageFileDescriptor = getContentResolver().openAssetFileDescriptor(imageURI, "r");
            InputStream imageInputStream = imageFileDescriptor.createInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(imageInputStream);
            detailsPic.setImageBitmap(imageBitmap);
        }
        catch(IOException e){

        }

        boolean phoneFlag = false;
        boolean emailFlag = false;

        do {
            String data = detailsCursor.getString(detailsCursor.getColumnIndex(Data.DATA1));
            String mimetype = detailsCursor.getString(detailsCursor.getColumnIndex(Data.MIMETYPE));

            switch (mimetype) {
                case CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
                    detailsName.setText(data);
                    break;
                case CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                    if (!phoneFlag) {
                        detailsPhoneNo.setText(detailsPhoneNo.getText() + data);
                        phoneFlag = true;
                    }
                    break;
                case CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                    if (!emailFlag) {
                        detailsEmail.setText(detailsEmail.getText() + data);
                        emailFlag = true;
                    }
                    break;
            }

        } while(detailsCursor.moveToNext());

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
