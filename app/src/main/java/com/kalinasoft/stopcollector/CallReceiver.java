package com.kalinasoft.stopcollector;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by Ovoshi on 11/14/2016.
 */

public class CallReceiver extends BroadcastReceiver {

    final int BLOCK_TYPE_HANGUP = 1;
    final int BLOCK_TYPE_MUTE =2;

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean badNumber = false;
        int ringerMode;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int blockType = Integer.parseInt(prefs.getString("block_type","2"));
        boolean autoWhite = prefs.getBoolean("unblock_auto",true);
        boolean blockPrivate = prefs.getBoolean("block_private", true);
        boolean blockUnknown = prefs.getBoolean("block_unknown", true);

        Log.d("receiver", "onReceive: "+blockType);

        AudioManager aManager=(AudioManager)context.getSystemService(AUDIO_SERVICE);
        SharedPreferences ringerSettings = context.getSharedPreferences("stopcollector_settings", Context.MODE_PRIVATE);
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            //getting number
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);


            if (incomingNumber != null) {
//                if (binder!=null)
//                Log.d("service", "onReceive: "+binder.getList("black").getArray().length);

//                if (BanList.getInstance().find(incomingNumber)) {
                if (BanList.getInstance().find(incomingNumber)) {
                    badNumber = true;
                }
                else {
                    //encode number to URI
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                            Uri.encode(incomingNumber));
                    //searching for number in phone book
                    Cursor cur = context.getContentResolver().query(uri, new
                            String[]{
                            ContactsContract.PhoneLookup._ID,
                            ContactsContract.PhoneLookup.DISPLAY_NAME,
                            ContactsContract.PhoneLookup.PHOTO_ID}, null, null, null);
                    //if not found
                    if (cur == null || !cur.moveToFirst()) {
                        LogList.getInstance().add(incomingNumber,null,null);
                        //trying to add this number to list of unknown numbers
                        if (autoWhite){
                            if ( UnknownList.getInstance().add(incomingNumber))
                                //if this number is not found in list, set badNumber to true
                                if (blockUnknown)
                                    badNumber = true;
                        }
                        else
                            if (blockUnknown)
                                badNumber = true;


                    }
                    else{
                       LogList.getInstance().add(incomingNumber,cur.getString(1),Integer.toString(cur.getInt(2)));
                    }
                    //cleaning up phone book search
                    if (cur != null)
                        cur.close();
                }
            }
            else {
                LogList.getInstance().add(null,null,null);
                if (blockPrivate)
                    badNumber = true;
            }

            if (badNumber) {
                if (blockType == BLOCK_TYPE_MUTE) {
                    ringerMode = aManager.getRingerMode();
                    SharedPreferences.Editor ed = ringerSettings.edit();
                    ed.putInt("ringer", ringerMode);
                    ed.apply();
                    //and disabling all sounds
                    aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                } else
                if (blockType == BLOCK_TYPE_HANGUP){
                    TelephonyManager telephony = (TelephonyManager)
                            context.getSystemService(Context.TELEPHONY_SERVICE);
                    try {
                        Class c = Class.forName(telephony.getClass().getName());
                        Method m = c.getDeclaredMethod("getITelephony");
                        m.setAccessible(true);
                        ITelephony telephonyService = (ITelephony) m.invoke(telephony);
                            telephonyService.endCall();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
//            Toast.makeText(context, "Call from:" +incomingNumber, Toast.LENGTH_LONG).show();

        }
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){

            if (blockType == BLOCK_TYPE_MUTE) {

                // enabling ringer
                ringerMode = ringerSettings.getInt("ringer",AudioManager.RINGER_MODE_NORMAL);
                aManager.setRingerMode(ringerMode);
            }
        }
    }



}
