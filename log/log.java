package com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.log;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.android_sqlite_helper.sqLiteHelper;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.config.config;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.helpers.imlb;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.libraries.session;

/**
 * @category name space imlg_ => Interaction helper
 */
public class log 
{
        public static log log = null;
	
	public log()
	{
	}
	
	public static log singleton() 
	{
		if( log == null )
		{
			log = new log();
		}
		return log;
	}
	
	/**
	 * de reference all references to object and variable it will help prevent memory leak/massive memory usage 
	 */
	public void do_cleanup()
	{
		log = null;
	}


	public void debug( String message )
	{
		if(config.ENV < 3)
		{
			System.out.println("im_android_lib - Debug: " + message);
		}
	}

	public void printLn( String message )
	{
		System.out.println( message );
	}

	/**
	 *
	 * @param ctx
	 * @param priority
	 * @param message
	 */
	public void logError( Context ctx, int priority, String message)
	{
		logError(ctx,priority,message, new Exception());
	}

	/**
	 *
	 * @param ctx
	 * @param priority
	 * @param message
	 */

	public void logError( Context ctx, int priority, String message, Exception e )
	{
		if (priority <= 3 && config.ENV >= 3 && e != null)
		{
			Crashlytics.logException(e);
		}

		debug( "logError: Priority: "+priority + " Msg: " + message );
		sqLiteHelper.singleton(ctx).query(" INSERT INTO log(run_id, l_group_key, l_description, l_created_date) "
						+ " VALUES( ?, ?, ?, ? ) ",
				new String[]{session.singleton(ctx).getSessionId(ctx),
						"AL_" + ctx.getApplicationContext().getPackageName() + "_P_" + priority,
						message, imlb.singleton().timestamp()});

	}

}