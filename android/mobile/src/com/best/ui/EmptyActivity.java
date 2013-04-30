////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.best.util.TransitionEffect;

public class EmptyActivity extends Activity {

	public static Activity me;
	public static Context m_context;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT)
			TransitionEffect.callOverridePendingTransition(this);
	    //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		me = this;
		m_context = this;
		me.setContentView( com.best.ui.R.layout.in_progress );
    }

    @Override
    protected void onResume() {
        super.onResume();
	}
	
	 @Override
    protected void onPause() {
        super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		Best.exit( me );
		//return;
	}
}
		
  