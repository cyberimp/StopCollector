package com.kalinasoft.stopcollector;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;


public class ListBlack extends AppCompatActivity implements Observer{
//TODO: refactor to implement list activity
    int selected = -1;
    private RadioButton listRadioButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_black);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.blacklist_name);

        BanList l = BanList.getInstance();
        ListView lv = (ListView) findViewById(R.id.log_black);
        ContactAdapter adapter  = new ContactAdapter(
                this, android.R.layout.simple_list_item_single_choice, l.getArray());
        lv.setAdapter(adapter);
        l.addObserver(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ListView lv = (ListView) findViewById(R.id.log_black);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blacklist, menu);
        menu.findItem(R.id.action_blacklist).setVisible(false);
        menu.findItem(R.id.action_delete).setVisible(lv.getCheckedItemPosition()!=-1);
        menu.findItem(R.id.action_add).setVisible(true);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ListView lv = (ListView) findViewById(R.id.log_black);
        menu.findItem(R.id.action_delete).setVisible(lv.getCheckedItemPosition()!=-1);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                ListView lv = (ListView) findViewById(R.id.log_black);
                if (selected>-1) {
                    ContactRow number = (ContactRow) lv.getItemAtPosition(selected-1);
                    BanList.getInstance().delete(number);
                }
                return true;

            case R.id.action_add:
//                AlertDialog.Builder alert = new AlertDialog.Builder(this);
//                alert.setTitle(R.string.dialog_number);
//                final EditText input = new EditText(this);
//                input.setInputType(InputType.TYPE_CLASS_PHONE);
//                input.setRawInputType(Configuration.KEYBOARD_12KEY);
//                alert.setView(input);
//                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        BanList.getInstance().add(input.getText().toString());
//                    }
//                });
//                alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                });
//                alert.show();

                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, 1);
                return true;

            case R.id.action_clear:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(R.string.action_clear);
                alert.setMessage(R.string.action_clear_really);
                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        BanList.getInstance().clear();
                    }
                });
                alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    Cursor cursor = null;
                    try {
                        String phoneNo = null ;
                        String name = null;
                        int photo = 0;
                        // getData() method will have the Content Uri of the selected contact
                        Uri uri = data.getData();
                        //Query the content uri
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        // column index of the phone number
                        int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        int  photoIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
                        phoneNo = cursor.getString(phoneIndex);
                        name = cursor.getString(nameIndex);
                        photo = cursor.getInt(photoIndex);
                        BanList.getInstance().add(new ContactRow(phoneNo,name,Integer.toString(photo)));
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        BanList l = BanList.getInstance();
        ListView lv = (ListView) findViewById(R.id.log_black);
        ContactAdapter adapter  = new ContactAdapter(
                this, android.R.layout.simple_list_item_single_choice, l.getArray());
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        View vMain;
        RadioButton rb;
        try {
            rb = (RadioButton) v;
            vMain = ((View) v.getParent());
        } catch (ClassCastException e) {
            vMain = v;
            rb = (RadioButton) v.findViewById(R.id.contactSelector);
        }
        ListView lv = (ListView) findViewById(R.id.log_black);

        int nowSelected = lv.getFirstVisiblePosition() + lv.indexOfChild(vMain)+1;
        if (selected != nowSelected) {
            boolean redraw = false;
            if (selected == -1)
                redraw = true;
            lv.setItemChecked(selected, false);
            selected = nowSelected;
            lv.setItemChecked(selected, true);
            if (redraw)
                invalidateOptionsMenu();

        } else {
            lv.setItemChecked(selected, false);
            //      listRadioButton = null;
            selected = -1;
            invalidateOptionsMenu();
        }
    }

}
