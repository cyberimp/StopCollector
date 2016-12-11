package com.kalinasoft.stopcollector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;


public class ListLog extends AppCompatActivity implements Observer{


    int selected = -1;
    private RadioButton listRadioButton = null;

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
        ContactAdapter adapter  = new ContactAdapter(
                this, android.R.layout.simple_list_item_single_choice, l.getReverseArray());
        lv.setAdapter(adapter);
        l.addObserver(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blacklist, menu);
        ListView lv = (ListView) findViewById(R.id.log_log);
        menu.findItem(R.id.action_blacklist).setVisible(lv.getCheckedItemPosition()!=-1);
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_clear).setVisible(false);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ListView lv = (ListView) findViewById(R.id.log_log);
        menu.findItem(R.id.action_blacklist).setVisible(lv.getCheckedItemPosition()!=-1);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void update(Observable o, Object arg) {
        LogList l = LogList.getInstance();
        ListView lv = (ListView) findViewById(R.id.log_log);
        ContactAdapter adapter  = new ContactAdapter(
                this, android.R.layout.simple_list_item_single_choice, l.getReverseArray());
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_blacklist:
                ListView lv = (ListView) findViewById(R.id.log_log);
                if (selected>-1) {
                    ContactRow number = (ContactRow) lv.getItemAtPosition(selected);
                    if (number.getNumber() == null){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                R.string.warning_unknown_ban,
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else {
                        Log.d("ban", number.getNumber());
                        BanList.getInstance().addUnique(number);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                R.string.action_ban_complete,
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void onClick(View v){
        View vMain;
        RadioButton rb;
        try{
            rb = (RadioButton) v;
            vMain = ((View) v.getParent());
        }
        catch (ClassCastException e){
            vMain = v;
            rb = (RadioButton) v.findViewById(R.id.contactSelector);
        }
        ListView lv = (ListView) findViewById(R.id.log_log);

        int nowSelected = lv.getFirstVisiblePosition()+lv.indexOfChild(vMain);
        if (selected != nowSelected) {
            boolean redraw = false;
            if (selected == -1)
                redraw = true;
            lv.setItemChecked(selected,false);
            selected = nowSelected;
            lv.setItemChecked(selected,true);
            if (redraw)
                invalidateOptionsMenu();

        } else {
            lv.setItemChecked(selected,false);
      //      listRadioButton = null;
            selected = -1;
            invalidateOptionsMenu();
        }
    }


}
