package com.kalinasoft.stopcollector;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;


public class ListWhite extends AppCompatActivity implements Observer{

    int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.whitelist_name);
        UnknownList l = UnknownList.getInstance();
        ListView lv = (ListView) findViewById(R.id.log_unknown);
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_single_choice, l.getArray());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = i;
                invalidateOptionsMenu();
            }
        });
        l.addObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        UnknownList l = UnknownList.getInstance();
        ListView lv = (ListView) findViewById(R.id.log_unknown);
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_single_choice, l.getArray());
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ListView lv = (ListView) findViewById(R.id.log_unknown);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blacklist, menu);
        menu.findItem(R.id.action_blacklist).setVisible(selected!=-1);
        menu.findItem(R.id.action_delete).setVisible(selected!=-1);
        menu.findItem(R.id.action_add).setVisible(selected!=-1);
        menu.findItem(R.id.action_clear).setVisible(true);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ListView lv = (ListView) findViewById(R.id.log_unknown);
        menu.findItem(R.id.action_blacklist).setVisible(selected!=-1);
        menu.findItem(R.id.action_delete).setVisible(selected!=-1);
        menu.findItem(R.id.action_add).setVisible(selected!=-1);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ListView lv = (ListView) findViewById(R.id.log_unknown);
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (selected>-1) {
                    UnknownList.getInstance().delete(selected);
                }
                return true;

            case R.id.action_blacklist:
                if (selected>-1) {
                    String ban = UnknownList.getInstance().delete(selected);
                    BanList.getInstance().addUnique(new ContactRow(ban,null,null));
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.action_ban_complete,
                            Toast.LENGTH_SHORT);
                    toast.show();

                }
                return true;

            case R.id.action_add:
                if (selected>-1) {
                    Intent contactEdit = new Intent(Intent.ACTION_INSERT,
                            ContactsContract.Contacts.CONTENT_URI);
                    contactEdit.putExtra(ContactsContract.Intents.Insert.PHONE,
                            (String)lv.getItemAtPosition(selected));
                    startActivity(contactEdit);
                }
                return true;

            case R.id.action_clear:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(R.string.action_clear);
                alert.setMessage(R.string.action_clear_really);
                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        UnknownList.getInstance().clear();
                    }
                });
                alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
