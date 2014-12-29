package com.android.tap2unlock;

import java.io.FileInputStream;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//Automatically called when State Change is Detected because this Receiver is Registered for PHONE_STATE intent filter in AndroidManifest.xml
public class PhoneStateReceiver extends BroadcastReceiver {
	
	TelephonyManager manager;       
	PhoneStateMonitor phoneStateListener;
	static boolean isAlreadyListening=false;

	//This Method automatically Executed when Phone State Change is Detected
	public void onReceive(Context context, Intent intent) {
		String path = context.getFilesDir().getAbsolutePath();
		
		try {
			FileInputStream fis = new FileInputStream(path+"/switch");
			int swi = fis.read() - '0';
			if(swi == 0) 
				return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		phoneStateListener =new PhoneStateMonitor(context);//Creating the Object of Listener
		manager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);//Getting the Telephony Service Object
		if(!isAlreadyListening)//Checking Listener is Not Registered with Telephony Services
		{
              manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);//Registering the Listener with Telephony to listen the State Change
	      isAlreadyListening=true;  //setting true to indicate that Listener is listening the Phone State
		}

	}

}

