package com.kalinasoft.stopcollector;

import android.content.Context;
import android.telephony.PhoneNumberUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;

import static com.kalinasoft.stopcollector.R.menu.blacklist;

/**
 * Created by Ovoshi on 11/21/2016.
 */
public class BanList extends ContactList{
    private static BanList ourInstance = new BanList();
    public static BanList getInstance() {
        return ourInstance;
    }

    private BanList() {
        super("black_list");
    }
}
