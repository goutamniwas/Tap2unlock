package com.android.tap2unlock;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			sett2uswitch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void onPatternchange(View viewpattern) {
		
		Intent intent = new Intent(this, Pattern.class);
		startActivity(intent);
		
	}
	public void sett2uswitch() throws IOException
	{
		FileInputStream fi = new FileInputStream("/sys/android_touch/tap2unlock");
		
		
			
		
		int s;
		s =  fi.read() - '0';
		if(s==1)
		{
			ToggleButton tb = (ToggleButton) findViewById(R.id.t2uswitch);
			tb.setChecked(true);
			
		}
		else if (s == 0 ){
			
		}

		
		
		try {
			FileInputStream fis = new FileInputStream("/sys/android_touch/tap2unlock_version");
		
			
			int ch;
		
		        ch = fis.read() - '0';
		        fis.close();
		      
		        if(ch<2)
					Toast.makeText(getApplicationContext(), "Incompatible T2u version, Continue if you dont like using your phone",
							   Toast.LENGTH_LONG).show();  
			    
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
	}
	public void onToggleClicked(View view) throws InterruptedException, IOException {
		
		// Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    FileOutputStream fos;
		fos = openFileOutput("switch", Context.MODE_PRIVATE);
	    if (on){
	    	
		CommandCapture command1 = new CommandCapture(0, "chmod 777 /sys/android_touch/tap2unlock", "echo 1 > /sys/android_touch/tap2unlock");
		try {
			RootTools.getShell(true).add(command1);
			fos.write("1".getBytes());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RootDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	    else
	    {
	    	CommandCapture command = new CommandCapture(0, "chmod 777 /sys/android_touch/tap2unlock", "echo 0 > /sys/android_touch/tap2unlock");
			try {
				RootTools.getShell(true).add(command);
				fos.write("0".getBytes());
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RootDeniedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    fos.close();
	    }
		
}
