package com.kalinasoft.stopcollector;

import android.content.Context;
import android.net.Uri;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

/**
 * Created by Ovoshi on 11/21/2016.
 */
public class LogList extends ContactList{
    private static LogList ourInstance = new LogList();


    public static LogList getInstance() {
        return ourInstance;
    }

    private LogList() {
        super("log_list");
        setMaxSize(10);
    }
}
