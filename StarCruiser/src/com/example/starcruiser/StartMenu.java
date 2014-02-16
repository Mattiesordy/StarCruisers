package com.example.starcruiser;


import com.example.starcruiser.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StartMenu extends Activity {
private CustomSurfaceView.customView fighter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
	//	getMenuInflater().inflate(R.menvu.activity_main, menu);
		
		return true;
	}
	
	public void Start(View view) {
		//fighter = new CustomSurfaceView.customView(this);
		//setContentView(fighter);
		Intent gameIntent = new Intent("com.example.starcruiser.game");
		startActivity(gameIntent);
		
	}
	public void Highscore(){
		//StartGameAcivity.this.finish();
		StartMenu.this.finish();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

}
