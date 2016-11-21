package com.vk.ovoshi.stopcollector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;


public class ListLog extends AppCompatActivity implements Observer{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_log);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.history_name);

        LogList l = LogList.getInstance();
        ListView lv = (ListView) findViewById(R.id.log_log);

        TextView tv = new TextView(this);
        tv.setText(R.string.header_log);

        lv.addHeaderView(tv);
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_single_choice, l.getArray());
        lv.setAdapter(adapter);
        l.addObserver(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blacklist, menu);
        menu.findItem(R.id.action_blacklist).setVisible(true);
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_add).setVisible(false);

        return true;
    }


    @Override
    public void update(Observable o, Object arg) {
        LogList l = LogList.getInstance();
        ListView lv = (ListView) findViewById(R.id.log_log);
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_single_choice, l.getArray());
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_blacklist:
                ListView lv = (ListView) findViewById(R.id.log_log);
                String number = lv.getItemAtPosition(lv.getCheckedItemPosition()).toString();
                BanList.getInstance().add(number);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void onClick(View v){
    }


}
