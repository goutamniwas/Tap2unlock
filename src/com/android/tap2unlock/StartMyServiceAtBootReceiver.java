package com.android.tap2unlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    	
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction("com.android.tap2unlock.MySystemService");
            context.startService(serviceIntent);
        }
    }
}
