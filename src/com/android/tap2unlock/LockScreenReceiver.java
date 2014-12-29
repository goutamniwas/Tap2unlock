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
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LockScreenReceiver extends BroadcastReceiver {
	static int swi,allow;
    @Override
    public void onReceive(Context context, Intent intent) {
    	String path = context.getFilesDir().getAbsolutePath();
    	try {
			FileInputStream fis = new FileInputStream(path+"/switch");
			swi = fis.read() - '0';
			if(swi == 0) 
				return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String action = intent.getAction();
        Intent i = new Intent(context, LockScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        //If the screen was just turned on or it just booted up, start your Lock Activity
        if(action.equals(Intent.ACTION_SCREEN_ON) || action.equals(Intent.ACTION_BOOT_COMPLETED))
        {
        	
			try {
				
				FileInputStream fis = new FileInputStream("sys/android_touch/t2u_allow");
				allow = fis.read() - '0';
				fis.close();
				if(swi == 1 && allow == 0){
					
			         context.startActivity(i);					
				}
				
				/*if(swi == 1 && allow == 1 && LockScreenActivity.running == true ){
				((Activity) context).finish();
				}
				*/
				
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
			
           
        }
    }
}
