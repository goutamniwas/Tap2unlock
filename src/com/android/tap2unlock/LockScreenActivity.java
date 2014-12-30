/* 
 *	Copyright (C)  2014 Goutamniwas <goutamniwas@gmail.com>
 *
 *
 *	 Tap2unlock is a free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *	
*/

package com.android.tap2unlock;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.CommandCapture;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class LockScreenActivity extends Activity implements OnClickListener {
	private Object mStatusBarManager;
    private static final String STATUS_BAR_SERVICE = "statusbar";
    private static final int DISABLE_NONE = 0x00000000;
    private static final int STATUS_BAR_DISABLE_EXPAND = 0x00010000;
	public static int count = 0;
	public static boolean user_leave = false,authenticated = false;
	FileInputStream fi;
	StringBuilder builder = new StringBuilder();
	StringBuilder builder1 = new StringBuilder();
	TextView tv ;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        mStatusBarManager = getSystemService(STATUS_BAR_SERVICE);
        disableStatusbarExpand(STATUS_BAR_DISABLE_EXPAND);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); 
        setContentView(R.layout.activity_lockscreen);
        //Set up our Lockscreen
        makeFullScreen();
        tv = (TextView) findViewById(R.id.patternView);
        startService(new Intent(this,LockScreenService.class));
        Button mClickButton1 = (Button)findViewById(R.id.button1);
        mClickButton1.setOnClickListener(this);
        Button mClickButton2 = (Button)findViewById(R.id.button2);
        mClickButton2.setOnClickListener(this);
        Button mClickButton3 = (Button)findViewById(R.id.button3);
        mClickButton3.setOnClickListener(this);
        Button mClickButton4 = (Button)findViewById(R.id.button4);
        mClickButton4.setOnClickListener(this);
       
        tv.setBackgroundColor(Color.WHITE);
        
        
        
        	try {
				fi = openFileInput("pat");
				int ch;
				while((ch = fi.read()) != -1){
				    builder1.append((char)ch);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        	
        
    }
    
  
    /**
     * A simple method that sets the screen to fullscreen.  It removes the Notifications bar,
     *   the Actionbar and the virtual keys (if they are on the phone)
     */
    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        return; //Do nothing!
    }
    
    @Override
    public void onPause() {
    	authenticated = false;
    	super.onPause();
    	if(user_leave)
    	same();
    }
    
    public void same() {
    	Intent intent = new Intent(LockScreenActivity.this,
				LockScreenActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        user_leave = false;
    }
   
    @Override
    protected void onUserLeaveHint() {
    	user_leave = true;
    	super.onUserLeaveHint();
    	try {
    	CommandCapture command = new CommandCapture(0, "chmod 777 /sys/android_touch/t2u_allow","echo "+builder1+"0"+" > /sys/android_touch/t2u_allow");
		RootTools.getShell(true).add(command);
		same();
		
		//Toast.makeText(context, "Phone State is RINGING "+builder.toString()+"1"
		//		+"the val ", Toast.LENGTH_LONG).show();
    }
	 catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	} catch (TimeoutException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	} catch (RootDeniedException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
    	
        return; // Don't forget this line
        
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        
         if(!hasFocus) {
             Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
             closeDialog.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            sendBroadcast(closeDialog);
        }
    }
   
    
    @Override
    public void onResume() {
    	count = 0;
    	try {
    		builder1.setLength(0);
    		try {
				fi = openFileInput("pat");
				int ch;
				while((ch = fi.read()) != -1){
				    builder1.append((char)ch);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	CommandCapture command = new CommandCapture (0,"chmod 777 /sys/android_touch/t2u_allow","echo "+builder1+"1"+" > /sys/android_touch/t2u_allow");
    	
		RootTools.getShell(true).add(command);
		
		
		//Toast.makeText(getApplicationContext(), "trying to enable touch sensor "+builder1.toString()+"1", Toast.LENGTH_LONG).show();
    }
	 catch (IOException | TimeoutException | RootDeniedException e2) {
		// TODO Auto-generated catch block
		// Toast.makeText(getApplicationContext(), "exception "+e2.toString(),Toast.LENGTH_LONG);
	} 

    	disableStatusbarExpand(STATUS_BAR_DISABLE_EXPAND);
    	super.onResume();
    	FileInputStream fis;
		try {
			fis = new FileInputStream("sys/android_touch/t2u_allow");
			int allow = fis.read() - '0';
			fis.close();
			if(allow == 1 || PhoneStateMonitor.in_call == true ) {
				this.finish();
				
				disableStatusbarExpand(DISABLE_NONE);
				
			}
			
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    public void exit() {
    	count = 0;
    	authenticated = true;
    	disableStatusbarExpand(DISABLE_NONE);
    	this.finish();
    }
    public void onClick(View v) {
    	count ++;
    	
        switch (v.getId()) {
            case  R.id.button1: {
                // do something for button 1 click
            	builder.append("1");
            	tv.setBackgroundColor(Color.WHITE);
                break;
            }

            case R.id.button2: {
                // do something for button 2 click
            	builder.append("2");
                break;
            }
            case  R.id.button3: {
                // do something for button 1 click
            	builder.append("3");
                break;
            }

            case R.id.button4: {
                // do something for button 2 click
            	builder.append("4");
                break;
            }

            //.... etc
        }
        if(count == 4) {
        	count = 0;
        	
        
        	if(builder.toString().equals(builder1.toString())) {
        		builder.setLength(0); 
        		
        		exit();
        	}
        	else {
        		tv.setText("wrong pattern");
        		tv.setBackgroundColor(Color.RED);
        		Toast.makeText(getApplicationContext(), "Wrong pattern !!! ",
  					   Toast.LENGTH_SHORT).show(); 
        	builder.setLength(0); 
        	}
       
        }
        
        tv.setText(builder.toString());
        	
        
    }

    public void unlockScreen(View view) {
    	disableStatusbarExpand(DISABLE_NONE);
    	count =0;
        //Instead of using finish(), this totally destroys the process
        this.finish();
    }
    
    private void disableStatusbarExpand(int what) {
        try {
        //	Toast.makeText(getApplicationContext(), "on disable status bar function ",
		//			   Toast.LENGTH_SHORT).show(); 
            Method disable = mStatusBarManager.getClass().getMethod("disable", int.class);
            try {
                disable.invoke(mStatusBarManager, what);
            } catch (IllegalArgumentException e) {
            	Toast.makeText(getApplicationContext(), "illegal argument",
 					   Toast.LENGTH_SHORT).show(); 
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
            	Toast.makeText(getApplicationContext(), "illegal access",
 					   Toast.LENGTH_SHORT).show(); 
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
            	//Toast.makeText(getApplicationContext(), "invocation target"+e.getCause(),
 				//	   Toast.LENGTH_SHORT).show(); 
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
        	Toast.makeText(getApplicationContext(), "no such method  ",
					   Toast.LENGTH_SHORT).show(); 
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
