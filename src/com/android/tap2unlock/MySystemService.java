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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

public class MySystemService extends Service{
	Context context = null;
	
public int swi =0;


   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
	   
	  
	   StringBuilder builder = new StringBuilder();
		
		FileInputStream fi,fis;
		try {
			
			
			fis = openFileInput("switch");
			swi = fis.read() - '0';
			
			
			CommandCapture command1 = new CommandCapture(0, "chmod 777 /sys/android_touch/tap2unlock", "echo "+swi+" > /sys/android_touch/tap2unlock");
			
			
			try {
				RootTools.getShell(true).add(command1);
			} catch (TimeoutException | RootDeniedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		
		}  catch (IOException e1) {
			// TODO Auto-generated catch block
			CommandCapture command1 = new CommandCapture(0, "chmod 777 /sys/android_touch/tap2unlock", "echo 0 > /sys/android_touch/tap2unlock");
			
			
			try {
				RootTools.getShell(true).add(command1);
			} catch (TimeoutException | RootDeniedException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e1.printStackTrace();
		}
	
		
	
		try {
			
				fi = openFileInput("pat");
				int ch;
				while((ch = fi.read()) != -1){
				    builder.append((char)ch);
				}
				CommandCapture command = new CommandCapture(0, "chmod 777 /sys/android_touch/tap2unlock_pattern", "echo 1234"+builder+" > /sys/android_touch/tap2unlock_pattern");

				try {
					
					RootTools.getShell(true).add(command);
					
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RootDeniedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			CommandCapture command = new CommandCapture(0, "chmod 777 /sys/android_touch/tap2unlock_pattern", "echo 12341234 > /sys/android_touch/tap2unlock_pattern");

			try {
				
				RootTools.getShell(true).add(command);
				
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
			
			e1.printStackTrace();
		}
		
       
       return START_STICKY;
   }

   @Override
   public void onCreate() {
       super.onCreate();
      
   }

   @Override
   public void onDestroy() {
       super.onDestroy();
      
   }

@Override
public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
}

}
