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

public class Custom_dialog
{	
	Dialog m_dialog;
	
	public void show(Context _context,String _string)
	{   
	
		m_dialog = new Dialog( _context );
		m_dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
		m_dialog.setContentView( R.layout.customdialog );
		TextView text = (TextView)m_dialog.findViewById(R.id.text);
		TextView title = (TextView)m_dialog.findViewById(R.id.title);
		
		title.setText(_context.getString(R.string.chalobest));
		title.setTypeface( Best.m_marathi_typeface ); 
		text.setText(_string);
		text.setTypeface(Best.m_marathi_typeface); 
		Button dialogButton = (Button)m_dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setText(_context.getString(R.string.ok));
		dialogButton.setTypeface( Best.m_marathi_typeface );
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_dialog.dismiss();
			}
		});
		m_dialog.show();		  
	};
	public void show(Context _context,String _string,String _title)
	{   
	
		m_dialog = new Dialog( _context );
		m_dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
		m_dialog.setContentView( R.layout.customdialog );
		TextView text = (TextView)m_dialog.findViewById(R.id.text);
		TextView title = (TextView)m_dialog.findViewById(R.id.title);
		
		title.setText(_title);
		title.setTypeface( Best.m_marathi_typeface ); 
		text.setText(_string);
		text.setTypeface(Best.m_marathi_typeface); 
		Button dialogButton = (Button)m_dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setText(_context.getString(R.string.ok));
		dialogButton.setTypeface( Best.m_marathi_typeface );
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_dialog.dismiss();
			}
		});
		m_dialog.show();		  
	};
	
}
	
