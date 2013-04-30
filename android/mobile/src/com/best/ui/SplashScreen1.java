////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import com.best.util.TransitionEffect;

public class SplashScreen1 extends Activity {   
   
	protected boolean _active = true;
	protected int _splashTime = 1200; // time to display the splash screen in ms
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	
        super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT)
			TransitionEffect.callOverridePendingTransition(this);

		//overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        // Splash screen view
        setContentView(com.best.ui.R.layout.splash);
	}
	@Override
    public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        _active = false;
	    }
	    return true;
   }  
} 