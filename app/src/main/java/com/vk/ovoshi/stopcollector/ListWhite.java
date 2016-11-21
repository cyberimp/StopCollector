package com.vk.ovoshi.stopcollector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Observable;
import java.util.Observer;


public class ListWhite extends AppCompatActivity implements Observer{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.whitelist_name);
        UnknownList l = UnknownList.getInstance();
        ListView lv = (ListView) findViewById(R.id.log_unknown);
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, l.getArray());
        lv.setAdapter(adapter);
        l.addObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        UnknownList l = UnknownList.getInstance();
        ListView lv = (ListView) findViewById(R.id.log_unknown);
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, l.getArray());
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
