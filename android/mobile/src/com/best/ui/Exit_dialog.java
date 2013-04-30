////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.view.Window;

public class Exit_dialog
{	
	Dialog m_dialog;

	public void show(Context _context)
	{   
	
		m_dialog = new Dialog( _context );
		m_dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
		m_dialog.setContentView( R.layout.exit_dialog );
		
		TextView title = (TextView)m_dialog.findViewById(R.id.title);
		
		title.setText(_context.getString(R.string.are_you_exit));
		title.setTypeface( Best.m_marathi_typeface ); 
		Button dialogButton = (Button)m_dialog.findViewById(R.id.dialogButtonYES);
		dialogButton.setText(_context.getString(R.string.yes));
		dialogButton.setTypeface( Best.m_marathi_typeface );
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainTab maintab=new MainTab();
				maintab.finish1();
			}
		});
		Button dialogButton2 = (Button)m_dialog.findViewById(R.id.dialogButtonNO);
		dialogButton2.setText(_context.getString(R.string.no));
		dialogButton2.setTypeface( Best.m_marathi_typeface );
		dialogButton2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_dialog.dismiss();
			}
		});
		m_dialog.show();		  
	};
	}