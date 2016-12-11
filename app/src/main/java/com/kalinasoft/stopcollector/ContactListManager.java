package com.kalinasoft.stopcollector;

import android.support.v4.util.ArrayMap;

/**
 * Created by Ovoshi on 11/27/2016.
 */
public class ContactListManager {
    private static ContactListManager ourInstance = new ContactListManager();

    public static ContactListManager getInstance() {
        return ourInstance;
    }

    public static ContactList getList(String name){
        return ourInstance.listGet(name);
    }

    private ArrayMap<String,ContactList> lists;

    private void listAdd(String name){
        ContactList newList = new ContactList(name);
        lists.put(name, newList);
    }

    private ContactList listGet(String name){
        return  lists.get(name);
    }

    private ContactListManager() {
        lists = new ArrayMap<>();
        listAdd("black_list");
        listAdd("log_list");
        listGet("log_list").setMaxSize(10);
    }
}
