package com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.libraries;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.hsquaretech.android_common_helpers.R;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.android_sqlite_helper.sqLiteHelper;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.helpers.imui;


/**
 * @category name space system_permissions_ => Interaction helper
 */
public class system_permissions
{
    public static system_permissions system_permissions = null;

	public final static int SP_IDENTIFIER_INTERNET = 1;
	public final static int SP_IDENTIFIER_ACCESS_FINE_LOCATION = 2;
	public final static int SP_IDENTIFIER_WRITE_EXTERNAL_STORAGE = 3;
	public final static int SP_IDENTIFIER_MULTIPLE = 4;

	public system_permissions()
	{
		// TODO Auto-generated constructor stub
	}
	
	public static system_permissions singleton()
	{
		if( system_permissions == null )
		{
			system_permissions = new system_permissions();
		}
		return system_permissions;
	}

	public static boolean isPermissionCheckRequired()
	{
		if( imui.singleton().versionSDK_INT() >= 23 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean isPermissionAllowed(Activity activityObj, String permission )
	{
		if( ContextCompat.checkSelfPermission(activityObj, permission) ==  PackageManager.PERMISSION_GRANTED )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 *
	 * @param activityObj
	 * @param title
	 * @param msg
	 * @param permission
	 */
	private static void requestPermissionDialog(final Activity activityObj, String title, String msg, final String permission)
	{
		new AlertDialog.Builder(activityObj)
				//set message, title, and icon
				.setTitle( title )
				.setMessage( msg )
				.setIcon(R.mipmap.ic_launcher)
				.setNeutralButton( "CANCEL", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						dialog.dismiss();
						imui.singleton().showToast(activityObj, "Function depending on requested permission will not work!", Toast.LENGTH_LONG);

						if( permission.equals( Manifest.permission.ACCESS_FINE_LOCATION ) )
						{
							sqLiteHelper.singleton(activityObj).updInsConfigKey("gpsSelected", "1");
						}
					}
				})
				.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton)
					{
						requestPermission( activityObj, permission, false );
						dialog.dismiss();
					}
				})
				.create()
				.show();
	}


	/**
	 * de reference all references to object and variable it will help prevent memory leak/massive memory usage 
	 */
	public void do_cleanup()
	{
		system_permissions = null;
	}

	/**
	 * do request permission stuff, soon these function needs to able to ask multiple permissions at once
	 * @param activityObj
	 * @param permission
	 * @param isShowRequestDialog
	 */
	public static void requestPermission( Activity activityObj, String permission, boolean isShowRequestDialog )
	{
		// Should we show an explanation?
		if ( isShowRequestDialog && ActivityCompat.shouldShowRequestPermissionRationale(activityObj,permission) )
		{

			// Show an expanation to the user *asynchronously* -- don't block
			// this thread waiting for the user's response! After the user
			// sees the explanation, try again to request the permission.

			String title = "Allow Permission";
			if( permission.equals( Manifest.permission.INTERNET ) )
			{
				requestPermissionDialog(activityObj, title, "For application to function it is required that you give app permission to access the internet.", permission);
			}
			else if( permission.equals( Manifest.permission.ACCESS_FINE_LOCATION ) )
			{
				requestPermissionDialog(activityObj, title, "For application to function well it is required that you give app permission to access GPS.", permission);
			}
			else if( permission.equals( Manifest.permission.WRITE_EXTERNAL_STORAGE ) )
			{
				requestPermissionDialog(activityObj, title, "For application to function well it is required that you give app permission to access SD storage.", permission);
			}
			else
			{
				requestPermissionDialog(activityObj, title, "For application to function it is required that you give app permission to access " + permission, permission);
			}

		}
		else
		{

			// No explanation needed, we can request the permission.
			if( permission.equals( Manifest.permission.INTERNET ) )
			{
				ActivityCompat.requestPermissions( activityObj, new String[]{ permission }, SP_IDENTIFIER_INTERNET);
			}
			else if( permission.equals( Manifest.permission.ACCESS_FINE_LOCATION ) )
			{
				ActivityCompat.requestPermissions( activityObj, new String[]{ permission }, SP_IDENTIFIER_ACCESS_FINE_LOCATION );
			}
			else if( permission.equals( Manifest.permission.WRITE_EXTERNAL_STORAGE ) )
			{
				ActivityCompat.requestPermissions( activityObj, new String[]{ permission }, SP_IDENTIFIER_WRITE_EXTERNAL_STORAGE );
			}

		}
	}

	/**
	 *
	 * @param activityObj
	 * @param permission
	 *
	 * @return false if operation depending on permissions required to be halted, true if no need to halt any operations
	 */
	public static boolean doPermissionStuff( Activity activityObj, String permission, boolean isShowRequestDialog )
	{
		if( !isPermissionCheckRequired() )
		{
			//return since permission check is not required
			return true;
		}

		if( isPermissionAllowed(activityObj, permission) )
		{
			//return since permission is already available
			return true;
		}

		requestPermission(activityObj, permission, isShowRequestDialog);

		return false;
	}

	public static boolean initiateInternetPermissionCheck( Activity activityObj )
	{
		return doPermissionStuff(activityObj, Manifest.permission.INTERNET, false);
	}

	
}