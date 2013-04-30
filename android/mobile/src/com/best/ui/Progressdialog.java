////////////////////////////////////////////////
//
// License: GPLv3
//
//


package com.best.ui;

import android.content.Context;
import android.app.Dialog;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.Window;
import android.graphics.Typeface;

public class Progressdialog 
 {	
	ProgressBar myProgressBar;
	int myProgress = 0;
	private Dialog m_dialog;
    static Context context; 
    	
	//@Override
    public void show(Context _context) {
        context = _context;
		m_dialog = new Dialog( context );
		m_dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
		m_dialog.setContentView(R.layout.new_progress_bar);
		myProgressBar = (ProgressBar)m_dialog.findViewById(R.id.progress_bar);
		Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/mangal.ttf");
		TextView text = (TextView) m_dialog.findViewById(R.id.text);
		TextView title = (TextView) m_dialog.findViewById(R.id.title);
		title.setText(_context.getString(R.string.lnfrser));
		title.setTypeface( face ); 
		text.setText(_context.getString(R.string.lnfrserb));
		text.setTypeface(face); 
		m_dialog.show();
	}
	
	public void dismiss(){
		m_dialog.dismiss();
	}
}