package com.vk.ovoshi.stopcollector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by Ovoshi on 11/14/2016.
 */

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean badNumber = false;
        int ringerMode;

        AudioManager aManager=(AudioManager)context.getSystemService(AUDIO_SERVICE);
        SharedPreferences ringerSettings = context.getSharedPreferences("stopcollector_settings", Context.MODE_PRIVATE);
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            //getting number
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);


            if (incomingNumber != null) {
                LogList.getInstance().add(incomingNumber);

                if (BanList.getInstance().Find(incomingNumber)) {
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
                            ContactsContract.PhoneLookup.STARRED}, null, null, null);
                    //if not found
                    if (cur == null || !cur.moveToFirst()) {
                        //trying to add this number to list of unknown numbers
                        if (UnknownList.getInstance().add(incomingNumber)) {

                            //if this number is not found in list, set badNumber to true
                            badNumber = true;
                        }
                    }
                    //cleaning up phone book search
                    if (cur != null)
                        cur.close();
                }
            }
            else {
                badNumber = true;
            }

            if (badNumber) {
                ringerMode = aManager.getRingerMode();
                SharedPreferences.Editor ed = ringerSettings.edit();
                ed.putInt("ringer", ringerMode);
                ed.apply();
                //and disabling all sounds
                aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            }
//            Toast.makeText(context, "Call from:" +incomingNumber, Toast.LENGTH_LONG).show();

        }
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
            // enabling ringer
            ringerMode = ringerSettings.getInt("ringer",AudioManager.RINGER_MODE_NORMAL);
            aManager.setRingerMode(ringerMode);
        }
    }

}
