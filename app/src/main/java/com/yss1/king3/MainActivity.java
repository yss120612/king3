
package com.yss1.king3;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jme3.app.AndroidHarnessFragment;

import java.util.logging.Level;
import java.util.logging.LogManager;



public class MainActivity extends Activity {
    /*
     * Note that you can ignore the errors displayed in this file,
     * the android project will build regardless.
     * Install the 'Android' plugin under Tools->Plugins->Available Plugins
     * to get error checks and code completion for the Android project files.
     */
 
    public MainActivity(){
        // Set the default logging level (default=Level.INFO, Level.ALL=All Debug Info)
        LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
    }
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("Yss", "1");
        // Set window fullscreen and remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        //Log.d("Yss", "1a");
        // find the fragment
        FragmentManager fm = getFragmentManager();
        AndroidHarnessFragment jmeFragment = (AndroidHarnessFragment) fm.findFragmentById(R.id.jmeFragment);

        //Log.d("Yss", "2");
        // uncomment the next line to add the default android profiler to the project
        //jmeFragment.getJmeApplication().setAppProfiler(new DefaultAndroidProfiler());
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
 
 


}