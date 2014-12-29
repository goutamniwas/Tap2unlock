package com.android.tap2unlock;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

public class PhoneStateMonitor extends PhoneStateListener  {
	Context context;
	public static StringBuilder builder;
	public static int cha;
	public static boolean in_call = false;
	public void prevent_suspend() {
		try {
			
			builder = new StringBuilder();
			
			FileInputStream fi;
		
		    String path = context.getFilesDir().getAbsolutePath();
			fi = new FileInputStream(path+"/pat");
			int ch;		
			while((ch = fi.read()) != -1){
			    builder.append((char)ch);
			}
			fi.close();
			FileInputStream fis;
			fis = new FileInputStream("/sys/android_touch/t2u_allow");		
			cha = fis.read()-'0';
			fis.close();
			 CommandCapture command = new CommandCapture(0, "chmod 777 /sys/android_touch/t2u_allow","echo "+builder+"1"+" > /sys/android_touch/t2u_allow");
			RootTools.getShell(true).add(command);
			//Toast.makeText(context, "Phone State is RINGING "+builder.toString()+"1"
			//		+"the val ", Toast.LENGTH_LONG).show();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (TimeoutException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (RootDeniedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	public PhoneStateMonitor(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	//This Method Automatically called when changes is detected in Phone State
	public void onCallStateChanged(int state, String incomingNumber) {
		// TODO Auto-generated method stub
		super.onCallStateChanged(state, incomingNumber);
		
		//Toast.makeText(context, "Phone State - "+state+" Incoming Number - "+incomingNumber, Toast.LENGTH_LONG).show();//Giving the Message that Phone State Changed
		//Checking The phone state  
		switch(state)
		{
		case TelephonyManager.CALL_STATE_IDLE:    //Phone is in Idle State
			if(cha == 0 && in_call == true) {
				CommandCapture command1 = new CommandCapture(0, "chmod 777 /sys/android_touch/t2u_allow","echo "+builder+"0"+" > /sys/android_touch/t2u_allow");
				try {
					RootTools.getShell(true).add(command1);
					Intent i = new Intent(context, LockScreenActivity.class);
			        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			        String path = context.getFilesDir().getAbsolutePath();
		        	FileInputStream fis;
					try {
						fis = new FileInputStream(path+"/switch");
						int swi = fis.read() - '0';
						fis.close();
						
						if(swi == 1){
							
					         context.startActivity(i);					
						}
						
						
						
					} catch (IOException e) {
						
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException | TimeoutException | RootDeniedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			in_call = false;
			break;
		case TelephonyManager.CALL_STATE_RINGING:  //Phone is Ringing			
				
				   in_call = true ;
					prevent_suspend();
					
					//Toast.makeText(context, "ringin  in_call = "+in_call, Toast.LENGTH_SHORT).show();
					break;
				
					
		
		case TelephonyManager.CALL_STATE_OFFHOOK:  //Call is Received
			
			//Toast.makeText(context, "offhook in_call = "+in_call, Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
