package com.spbstu.appmathdep;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

//****************************************************************
//class AboutActivity
//****************************************************************
public class AboutActivity extends Activity implements  OnCompletionListener, View.OnTouchListener
{
	
	// *************************************************
	// DATA
	// *************************************************
	AppIntro				m_app;
	AppView				    m_appView;
	

	// *************************************************
	// METHODS
	// *************************************************
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        //overridePendingTransition(0, 0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // No Status bar
        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // Detect language
        String strLang = Locale.getDefault().getDisplayLanguage();
        int language;
        if (strLang.equalsIgnoreCase("english"))
        {
        	Log.d("AMDEPTH", "LOCALE: English");
        	language = AppIntro.LANGUAGE_ENG;
        }
        else if (strLang.equalsIgnoreCase("русский"))
        {
        	Log.d("AMDEPTH", "LOCALE: Russian");
        	language = AppIntro.LANGUAGE_RUS;
        }
        else
        {
        	Log.d("AMDEPTH", "LOCALE unknown: " + strLang);
        	language = AppIntro.LANGUAGE_UNKNOWN;
        	//AlertDialog alertDialog;
        	//alertDialog = new AlertDialog.Builder(this).create();
        	//alertDialog.setTitle("Language settings");
        	//alertDialog.setMessage("This application available only in English or Russian language.");
        	//alertDialog.show();        	
        }
        // Create application
        m_app = new AppIntro(this, language);
        // Create view
        m_appView = new AppView(this);
        setContentView(m_appView);
		
	}

	protected void onPostCreate(Bundle savedInstanceState) 
	{
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		
		// delayedHide(100);
	}
    public void onCompletion(MediaPlayer mp) 
    {
    	Log.d("AMDEPTH", "onCompletion: Video play is completed");
    	//switchToGame();
    }
	
	
    public boolean onTouch(View v, MotionEvent evt)
    {
    	int x = (int)evt.getX();
    	int y = (int)evt.getY();
    	int touchType = AppIntro.TOUCH_DOWN;
		if (evt.getAction() == MotionEvent.ACTION_MOVE)
			touchType = AppIntro.TOUCH_MOVE;
		if (evt.getAction() == MotionEvent.ACTION_UP)
			touchType = AppIntro.TOUCH_UP;
    	return m_appView.onTouch( x, y, touchType);
    }
    public boolean onKeyDown(int keyCode, KeyEvent evt)
    {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			//Log.d("SPARTA", "Back key pressed");
			//boolean wantKill = m_app.onKey(Application.KEY_BACK);
			//if (wantKill)
		    //		finish();
			//return true;
		}
    	boolean ret = super.onKeyDown(keyCode, evt);
    	return ret;
    }
    public AppIntro getApp()
    {
    	return m_app;
    }
    
	protected void onPause()
	{
	    // stop anims
   	    m_appView.stop();
	    // complete system
		super.onPause();
	}
	protected void onResume()
	{
		super.onResume();
    	m_appView.start();
	}
	public void onConfigurationChanged(Configuration confNew)
	{
		super.onConfigurationChanged(confNew);
		m_appView.onConfigurationChanged(confNew);
	}
    
    
}
