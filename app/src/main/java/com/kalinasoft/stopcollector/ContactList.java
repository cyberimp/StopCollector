package com.kalinasoft.stopcollector;

import android.content.Context;
import android.telephony.PhoneNumberUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

/**
 * Created by Ovoshi on 11/24/2016.
 */

public class ContactList extends Observable {
    private String fileName;
    private ArrayList<ContactRow> list;
    private int maxSize = 0;

    public ContactList(String fileName) {
        list = new ArrayList<>();
        this.fileName = fileName;
        loadList();
    }
    
    private void loadList() {
        Context context = MyApp.getAppContext();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<ContactRow>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void saveList() {
        setChanged();
        notifyObservers();
        Context context = MyApp.getAppContext();
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void add(ContactRow number) {
        list.add(number);
        rotate();
        saveList();
    }

    public void add(String number, String name, String photo) {
        ContactRow row = new ContactRow(number, name , photo);
        this.add(row);
    }

    public void rotate(){
        if (maxSize > 0 && list.size() > maxSize)
            list.remove(0);
    }

    public void clear(){
        list.clear();
        saveList();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean addUnique(ContactRow number) {
        if (!find(number) && number.getNumber() != null) {
            this.add(number);
            return true;
        }
        else
            return false;
    }

    public boolean find(ContactRow number) {
        return find(number.getNumber());
    }

    public boolean find(String number) {
        if (number == null)
            return false;
        for (ContactRow s: list) {
            if (s.getNumber()!= null &&
                    PhoneNumberUtils.compare(
                            MyApp.getAppContext(),
                            s.getNumber(),number))
                return true;
        }
        return false;

    }

    public ContactRow[] getArray() {
        ContactRow[] result = new ContactRow[list.size()];
        result = list.toArray(result);
        if (list == null)
            return new ContactRow[0];
        else
            return result;
    }

    public ContactRow[] getReverseArray() {
        ContactRow[] result = new ContactRow[list.size()];
        ArrayList<ContactRow> reverseLog = new ArrayList<>(list);
        Collections.reverse(reverseLog);
        result = reverseLog.toArray(result);
        if (list == null)
            return new ContactRow[0];
        else
            return result;

    }

    public void delete(ContactRow number) {
        if (find(number)) {
            list.remove(number);
            saveList();
        }
    }

}
