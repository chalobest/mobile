////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.util;

import android.app.Activity;

public class TransitionEffect{

	public static void callOverridePendingTransition(Activity m_context)
		{
			m_context.overridePendingTransition(com.best.ui.R.anim.fade_in_center, com.best.ui.R.anim.fade_out_center);
		}
}