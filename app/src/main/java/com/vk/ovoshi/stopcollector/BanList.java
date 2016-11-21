package com.vk.ovoshi.stopcollector;

import android.content.Context;
import android.telephony.PhoneNumberUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Ovoshi on 11/21/2016.
 */
public class BanList extends Observable{
    private static BanList ourInstance = new BanList();
    private ArrayList<String> blacklist;
    public static BanList getInstance() {
        return ourInstance;
    }

    private BanList() {
        blacklist = new ArrayList<>();
        Context context = MyApp.getAppContext();
        try {
            FileInputStream fis = context.openFileInput("black_list");
            ObjectInputStream ois = new ObjectInputStream(fis);
            blacklist = (ArrayList) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(String number){
        if (!Find(number) && number!=null) {
            blacklist.add(number);
            setChanged();
            notifyObservers();
            Context context = MyApp.getAppContext();
            try {
                FileOutputStream fos = context.openFileOutput("black_list",Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(blacklist);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean Find(String what){
        if (what == null)
            return false;
        for (String s: blacklist) {
            if (PhoneNumberUtils.compare(s,what))
                return true;
        }
        return false;
    }

    public String[] getArray() {
        String[] result = new String[blacklist.size()];
        result = blacklist.toArray(result);
        if (blacklist == null)
            return new String[]{"Here be niggers!"};
        else
            return result;

    }

    public void delete(String number) {
        if (Find(number)){
            blacklist.remove(number);
            setChanged();
            notifyObservers();
            Context context = MyApp.getAppContext();
            try {
                FileOutputStream fos = context.openFileOutput("black_list",Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(blacklist);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
