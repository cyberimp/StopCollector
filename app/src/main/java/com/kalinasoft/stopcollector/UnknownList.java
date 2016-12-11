package com.kalinasoft.stopcollector;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Ovoshi on 11/15/2016.
 */
public class UnknownList extends Observable {
    private static UnknownList ourInstance = new UnknownList();

    public static UnknownList getInstance() {
        return ourInstance;
    }

    private ArrayList<String> NumberList;

    private UnknownList() {
        NumberList = new ArrayList<>();
        Context context = MyApp.getAppContext();
        try {
            FileInputStream fis = context.openFileInput("unknown_list");
            ObjectInputStream ois = new ObjectInputStream(fis);
            NumberList = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveList(){
        setChanged();
        notifyObservers();
        Context context = MyApp.getAppContext();
        try {
            FileOutputStream fos = context.openFileOutput("unknown_list",Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(NumberList);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean add(String number){
        if(hasNumber(number))
            return false;
        else {
            NumberList.add(number);
            saveList();
            return true;
        }
    }

    public String delete(int number) {
        String deleted =  NumberList.remove(number);
        saveList();
        return deleted;
    }

        private boolean hasNumber(String number){
        return NumberList.contains(number);
    }

    public String[] getArray(){
//        return new String[]{"Here be niggers!"};
        String[] result = new String[NumberList.size()];
        result = NumberList.toArray(result);
        if (NumberList == null)
            return new String[]{"Here be niggers!"};
        else
            return result;
    }

    public void clear(){
        NumberList.clear();
        saveList();
    }
}
