package com.example.starcruiser;

import com.example.starcruiser.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class StartGameActivity extends Activity {

 //private Fighter fighter;

@Override
protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.splash);
    Thread logoTimer = new Thread(){
    	public void run(){
    		try{
    			sleep(5000);
    			Intent menuIntent = new Intent("com.example.starcruiser.startmenu");//name for intent is in the manifest
    			startActivity(menuIntent);
    		}
    		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    		}
    		finally{
    			StartGameActivity.this.finish();
    		}
    	}
    
    };
    logoTimer.start();
}


@Override
protected void onPause() {
     //TODO Auto-generated method stub
    super.onPause();

  //  setContentView(null);
   // fighter = null;

   // finish();
}


@Override
public void finish() {
	// TODO Auto-generated method stub
	super.finish();
}

}

