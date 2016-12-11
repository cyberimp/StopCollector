package com.kalinasoft.stopcollector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ovoshi on 11/25/2016.
 */

public class ContactListActivity extends AppCompatActivity implements Observer {

    //TODO: write abstract contact list activity
    ContactList list;
    ListView contactListView;

    public void initBar(int titleID, int layoutID, int listID){
        setContentView(layoutID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(titleID);
        contactListView = (ListView) findViewById(listID);
        ContactAdapter adapter = new ContactAdapter(
                this,android.R.layout.simple_list_item_single_choice,list.getArray());
        contactListView.setAdapter(adapter);
        list.addObserver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
