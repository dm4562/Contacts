package com.example.dhruv.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URI;

/**
 * Created by dhruv on 3/18/15.
 */
public class ContactsCursorAdapter extends CursorAdapter {

    ContentResolver contentResolver;


    public static class ViewHolder {
        public final ImageView contactImage;
        public final TextView contactName;

        ViewHolder(View view){
            contactImage = (ImageView) view.findViewById(R.id.contactPic);
            contactName = (TextView) view.findViewById(R.id.contactName);
        }
    }


    public ContactsCursorAdapter(Context context, Cursor c, int flags, ContentResolver contentResolver) {
        super(context, c, flags);
        this.contentResolver = contentResolver;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int layoutId = R.layout.layout_listview;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Long contactId = cursor.getLong(cursor.getColumnIndex(Contacts._ID));
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int contactImageID = cursor.getInt(
                cursor.getColumnIndex(Contacts.Photo._ID)
        );

//        int contactPicColIndex = cursor.getColumnIndex(Contacts.Photo.PHOTO);
//        Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI)));
        Uri uri = Uri.withAppendedPath(Contacts.CONTENT_URI, Long.toString(contactId));
        InputStream inputStream = Contacts.openContactPhotoInputStream(
                contentResolver,
                uri);
        Bitmap image = BitmapFactory.decodeStream(inputStream);

        viewHolder.contactImage.setImageBitmap(image);
        viewHolder.contactName.setText(
                cursor.getString(
                cursor.getColumnIndex(
                Contacts.DISPLAY_NAME_PRIMARY
        )));

    }
}
