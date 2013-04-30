////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class ExpandedListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int old_count = 0;

    public ExpandedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != old_count) {
            old_count = getCount();
            params = getLayoutParams();
			params.height = 0;
			if( old_count > 0 )
			{
				for(int i = 0; i < getCount(); i++)
				{
					System.out.println("===getCount()"+getCount()+"==="+i);
					try{
						params.height += getChildAt(i).getHeight();
					}catch(Exception e){
						System.out.println( "ERROR in getHeight:"+i );
						params.height += 75;
					}
				}
				params.height += 20;
				//params.height = ( getCount() * getChildAt(0).getHeight() ) + 20;
				System.out.println("====Child Height===="+getChildAt(0).getHeight());
				System.out.println("====List View Height===="+params.height);
			}
			else
				params.height = 0;
            setLayoutParams(params);
        }
		
        super.onDraw(canvas);
    }
}