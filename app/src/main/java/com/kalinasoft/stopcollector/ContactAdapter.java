package com.kalinasoft.stopcollector;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ovoshi on 11/23/2016.
 */

public class ContactAdapter extends ArrayAdapter<ContactRow> {
    public ContactAdapter(Context context, int resource, List<ContactRow> objects) {
        super(context, resource, objects);
    }

    public ContactAdapter(Context context, int resource, ContactRow[] array) {
        super(context, resource, array);
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
        RadioButton radio;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ContactRow rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.contactPhone);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.contactID);
            holder.imageView = (ImageView) convertView.findViewById(R.id.contactPhoto);
            holder.radio = (RadioButton) convertView.findViewById(R.id.contactSelector);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        if (rowItem.getNumber()!=null)
            holder.txtDesc.setText(rowItem.getNumber());
        else
            holder.txtDesc.setText(android.R.string.unknownName);

        if (rowItem.getName()!=null)
            holder.txtTitle.setText(rowItem.getName());
        else
            holder.txtTitle.setText(android.R.string.unknownName);

        if (rowItem.getPhotoID()!=null) {
            Cursor c = MyApp.getAppContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[] {
                    ContactsContract.CommonDataKinds.Photo.PHOTO
            }, ContactsContract.Data._ID + "=?", new String[] {
                    rowItem.getPhotoID()
            }, null);
            byte[] imageBytes = null;
            if (c != null) {
                if (c.moveToFirst()) {
                    imageBytes = c.getBlob(0);
                }
                c.close();
            }
            if (imageBytes!=null)
                holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length));
            else
                holder.imageView.setImageResource(android.R.drawable.sym_def_app_icon);
        }
        else
            holder.imageView.setImageResource(android.R.drawable.sym_def_app_icon);

        ListView lv = (ListView)parent;

        holder.radio.setChecked(position == lv.getCheckedItemPosition()-1);


        return convertView;
    }
}

