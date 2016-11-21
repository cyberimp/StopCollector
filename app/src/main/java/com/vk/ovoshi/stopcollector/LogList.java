package com.vk.ovoshi.stopcollector;

import android.content.Context;

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
public class LogList extends Observable{
    private static LogList ourInstance = new LogList();

    private ArrayList<String> log;

    public static LogList getInstance() {
        return ourInstance;
    }

    private LogList() {
        log = new ArrayList<>();
        Context context = MyApp.getAppContext();
        try {
            FileInputStream fis = context.openFileInput("log_list");
            ObjectInputStream ois = new ObjectInputStream(fis);
            log = (ArrayList) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String[] getArray(){
        String[] result = new String[log.size()];
        ArrayList<String> reverseLog = new ArrayList<>(log);
        Collections.reverse(reverseLog);
        result = reverseLog.toArray(result);
        if (log == null)
            return new String[]{"Here be niggers!"};
        else
            return result;
    }

    public void add(String number){
        if (number!=null) {
            log.add(number);
            if (log.size()>10)
                log.remove(0);
            setChanged();
            notifyObservers();
            Context context = MyApp.getAppContext();
            try {
                FileOutputStream fos = context.openFileOutput("log_list",Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(log);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
