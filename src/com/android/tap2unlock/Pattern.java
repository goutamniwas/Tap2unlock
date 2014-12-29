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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

public class Pattern extends Activity {
	public boolean isCorrect = false ;
	//EditText old; //= (EditText) findViewById(R.id.old);
	//EditText newpin;// = (EditText) findViewById(R.id.newpin);
	public String Oldpin,Newpin;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pattern);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pattern, menu);
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
	
	public void changePattern(View view) throws IOException, TimeoutException, RootDeniedException
	{
		isCorrect =false;
		forceChange();
		//forceChange();
		if(isCorrect) {
			FileOutputStream fos;
			try {
				fos = openFileOutput("pat", Context.MODE_PRIVATE);
				fos.write(Newpin.getBytes());
				fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

			
				
		
		
		
	}
	
	public void forceChange() throws IOException, TimeoutException, RootDeniedException {
		EditText old = (EditText) findViewById(R.id.old);
		EditText newpin = (EditText) findViewById(R.id.newpin);
		
		Newpin =newpin.getText().toString();
		Oldpin =old.getText().toString();
		if (Newpin.length() == 4 && Oldpin.length() == 4)
		{
			
			
			if(Newpin.charAt(0) - '0' > 4 || Newpin.charAt(1) - '0' > 4 || Newpin.charAt(2) - '0' > 4 || Newpin.charAt(3) - '0' > 4 
						|| Oldpin.charAt(0) - '0' > 4 || Oldpin.charAt(1) - '0' > 4 || Oldpin.charAt(2) - '0' > 4 || Oldpin.charAt(3) - '0' > 4){
			Toast.makeText(getApplicationContext(), "T2U : Invalid pin new or old pin (range 1-4)",
					   Toast.LENGTH_SHORT).show();
			newpin.setText("");
			old.setText("");
			}
			
			
			else {
				
		Toast.makeText(getApplicationContext(), "T2U : Processing ...",
				   Toast.LENGTH_SHORT).show();
		

		CommandCapture command = new CommandCapture(0, "chmod 777 /sys/android_touch/tap2unlock_pattern");
		

		try {
			RootTools.getShell(true).add(command);
			
			try {
				File f = new File("/sys/android_touch/tap2unlock_pattern");
				FileOutputStream foss = new FileOutputStream(f);
				String patput = Oldpin+Newpin;
				foss.write(patput.getBytes());
				foss.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RootDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		FileInputStream fi;
		try {
			fi = new FileInputStream("/sys/android_touch/tap2unlock_pattern");
			int s;
			s =  fi.read() - '0';
			fi.close();
			
			
			if(s==0)			
				Toast.makeText(getApplicationContext(), "T2U : The old pin you entered is wrong!",
						   Toast.LENGTH_SHORT).show();	
			else {
				Toast.makeText(getApplicationContext(), "T2U : Pattern change successful",
						   Toast.LENGTH_SHORT).show();
				isCorrect = true;
			}
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		old.setText("");
		newpin.setText("");
		
	}
	
	}
		else {
			Toast.makeText(getApplicationContext(), "T2U : The new or old pin has to be 4 numbers in size ",
					   Toast.LENGTH_SHORT).show();
		}
	}
	
}



