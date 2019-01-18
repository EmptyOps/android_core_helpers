package com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.helpers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.hsquaretech.android_common_helpers.R;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.config.config;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.log.log;
import com.hsquaretech.android_common_helpers.android_common_helpers.views.spinnerAdapter;
import com.hsquaretech.android_common_helpers.android_common_helpers.views.spinnerObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @category name space imui_ => UI helper
 * @author GAMSS
 */
public class imui
{
	public static imui imui = null;

	public ProgressDialog dialog_loader = null;

	public float scale = 0;

	public imui()
	{
	}

	public static imui singleton()
	{
		if( imui == null )
		{
			imui = new imui();
		}
		return imui;
	}

	/**
	 *
	 */
	public static boolean isViewVisible( View vw )
	{
		if (vw.getVisibility() == View.VISIBLE)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public float getScale( Activity activityObj )
	{
		if( scale == 0 )
		{
			scale = activityObj.getResources().getDisplayMetrics().density;
		}

		return scale;
	}

	@SuppressWarnings ( "deprecation")
	public int[] getHeightWidth( Activity activityObj )
	{
		int[] res = new int[2];

		Display display = activityObj.getWindowManager().getDefaultDisplay();
		res[0] = display.getWidth();  // deprecated
		res[1] = display.getHeight();  // deprecated

		return res;
	}



	/**
	 *
	 * @param activityObj
	 * @param dps
	 * @return
	 */
	public int dpToPixel( Activity activityObj, int dps )
	{
		return (int) (dps * getScale( activityObj ) + 0.5f);
	}

	/**
	 * prevents touch events of list view and delegates the same to parent scroll view.
	 * used when list view is inside scroll view
	 * @since 28-05-2015
	 */
	public static void viewPreventTouchForScrollView( View lv )
	{
		if( lv != null )
		{
			lv.setOnTouchListener(new View.OnTouchListener()
			{
				/**
				 * Setting on Touch Listener for handling the touch inside ScrollView
				 */
				@Override
				public boolean onTouch( View v, MotionEvent event )
				{
					/**
					 * Disallow the touch request for parent scroll on touch of child view
					 */
					v.getParent().requestDisallowInterceptTouchEvent( true );
					return false;
				}
			});
		}
	}

	//unset crashlytics User session
    public static void unsetCrshlyticSession() {
		// TODO: Use the current user's information
		// You can call any combination of these three methods
		Crashlytics.setUserIdentifier("");
		Crashlytics.setUserEmail("");
		Crashlytics.setUserName("");
	}

	/**
	 *
	 * @param ctx
	 * @param type
	 * @param msg
	 * @param buttonTitle
	 */
    public void showPopUpNotification(Context ctx, String type, String msg, String buttonTitle )
	{
		//
		if( imlb.isStrEmpty(buttonTitle) )
		{
			buttonTitle = "Ok";
		}

		//only show message if activity is alive and not beind closed
		if(!((Activity) ctx).isFinishing())
		{
			//show dialog
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
			builder.setTitle( type + "!" );
			builder.setMessage( msg );
			builder.setPositiveButton( buttonTitle, null );
			android.app.AlertDialog dialog = builder.show();

			TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER);
			dialog.show();
		}

	}

	/**
	 *
	 * @param dt
	 * @return
	 */
    public String getDate( DatePicker dt )
	{
		return "" + dt.getYear() + "-" + ( dt.getMonth() + 1 ) + "-" + dt.getDayOfMonth();
	}

	/**
	 * dialog message
	 * @param activityObj
	 * @param title
	 * @param message
	 */
    public static void alertMessgeDialog(final Activity activityObj , String title , String message)
	{
		AlertDialog myQuittingDialogBox =new AlertDialog.Builder(activityObj)
				//set message, title, and icon
				.setTitle(title)
				.setMessage(message)
				.setIcon(R.mipmap.ic_launcher)

				.setPositiveButton(activityObj.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						dialog.dismiss();
					}
				})

				.create();
		myQuittingDialogBox.show();
	}

	/**
	 * get device id
	 */
     public 	String getDeviceId(Context activityObj)
	{
		return Settings.Secure.getString(activityObj.getContentResolver(),
				Settings.Secure.ANDROID_ID);
	}

	/**
	 * Method for Setting the Height of the ListView dynamically.
	 * Hack to fix the issue of not showing all the items of the ListView
	 * when placed inside a ScrollView
	 **/
	public static void setListViewHeight( ListView listView, boolean is_first_time , int extraMargin )
	{
		ListAdapter listAdapter = listView.getAdapter();
		if ( listAdapter == null )
			return;

		/**
		 * extra bottom margin
		 */

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = extraMargin;
		listView.setLayoutParams(params);
		listView.requestLayout();

	}

	/**
	 *
	 * @return
	 */
	public static String getSpinnerSelValue( Spinner spinner )
	{
		Object selItem = spinner.getSelectedItem();
		if( selItem != null && !selItem.toString().equals( "Select" ) )
		{
			spinnerAdapter spnAdp = (spinnerAdapter) spinner.getAdapter();
			return String.valueOf( spnAdp.getDatabaseIdByPosition ( spinner.getSelectedItemPosition() ) );
		}
		else
		{
			return "";
		}
	}

	/**
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.DONUT)
	public boolean isFroyo()
	{
		try
		{
			return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? true : false;
		}
		catch( RuntimeException e )
		{
			errorMsg( null, 7, "Veriosn Check Runtime Exception. Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
			return false;
		}
	}

	/**
	 *
	 * @param ctx
	 * @param priority
	 * @param msg
	 */
	public void errorMsg( Context ctx, int priority, String msg )
	{
		//msg += Log.getStackTraceString(new Exception());
		if( msg.contains("im_") || msg.contains("java.net.UnknownHostException") )
		{
			if( msg.contains("im_e_nrc") )
			{
//				dialogMsg( ctx, "Internet Error", " Server is not responding, please try again later. " , "OK" );
				if(config.ENV <= 2)
				{
					dialogMsg( ctx, "Internet Error", " Server is not responding, please try again later. " , "OK" );
				}
				else
				{
//					dialogMsg(ctx, "Internet Error", ctx.getResources().getString(R.string.SOMETHING_WENT_WRONG), "OK");
				}

			}
			else
			{
				if(config.ENV <= 2)
				{
					dialogMsg( ctx, "Internet Error", " Server is not responding, please try again later. " , "OK" );
				}
				else
				{
//					dialogMsg(ctx, "Internet Error", ctx.getResources().getString(R.string.SOMETHING_WENT_WRONG), "OK");
				}
			}
		}
		else
		{
			if(config.ENV <= 2)
			{
				dialogMsg( ctx, "Internet Error", " Server is not responding, please try again later. " , "OK" );
			}
			else
			{
//				dialogMsg(ctx, "Internet Error", ctx.getResources().getString(R.string.SOMETHING_WENT_WRONG), "OK");
			}
		}

		log.singleton().logError(ctx, priority, msg);
	}


	/**
	 *
	 */

	/**
	 *  open soft keyboard
	 */
	public void openKeyboard( Context ctx )
	{
		((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 *  close soft keyboard
	 */
	public void closeKeyboard( Context ctx )
	{
		//((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow( ctx.getWindowToken(), 0);
	}

	/**
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.DONUT)
	public int versionSDK_INT()
	{
		return Build.VERSION.SDK_INT;
	}

	/**
	 *
	 */
	public boolean isVisible( Context activityObj, View vw )
	{
		if( vw != null )
		{
			if ( vw.getVisibility() == View.VISIBLE )
			{
				return true;
			}
		}

		return false;
	}

	/**
	 *
	 * @param activityObj
	 */
	public void hideElements( Activity activityObj, ArrayList<View> viewArr )
	{
		for (View vw : viewArr)
		{
			hideEleByObj(activityObj, vw);
		}
	}

	/**
	 *
	 * @param activityObj
	 */
	public void showElements( Activity activityObj, ArrayList<View> viewArr )
	{
		for (View vw : viewArr)
		{
			showEleByObj(activityObj, vw);
		}
	}

	/**
	 * @param activityObj
	 */
	public void showEleByInt( Activity activityObj, ArrayList<Integer> idArr )
	{
		for (Integer id : idArr)
		{
			View vw = (View) activityObj.findViewById( id );
			showEleByObj( activityObj, vw );
		}
	}

	/**
	 *
	 * @param activityObj
	 * @param idS
	 */
	public void hideElements( Activity activityObj, int[] idS )
	{
		int size = idS.length;

		for( int i=0; i < size; i++ )
		{
			View vw = (View) activityObj.findViewById( idS[i] );
			hideEleByObj(activityObj, vw);
		}
	}

	/**
	 * @param activityObj
	 * @param idS
	 */
	public void showElements( Activity activityObj, int[] idS )
	{
		int size = idS.length;

		for( int i=0; i < size; i++ )
		{
			View vw = (View) activityObj.findViewById( idS[i] );
			showEleByObj(activityObj, vw);
		}
	}

	/**
	 *
	 * @param view
	 * @param idS
	 */
	public void hideElementsInView( View view, int[] idS )
	{
		int size = idS.length;

		for( int i=0; i < size; i++ )
		{
			View vw = (View) view.findViewById( idS[i] );
			if( vw != null )
			{
				vw.setVisibility( View.GONE );
			}
		}
	}

	/**
	 * @param view
	 * @param idS
	 */
	public void showElementsInView( View view, int[] idS )
	{
		int size = idS.length;

		for( int i=0; i < size; i++ )
		{
			View vw = (View) view.findViewById( idS[i] );
			if( vw != null )
			{
				vw.setVisibility( View.VISIBLE );
			}
		}
	}

	/**
	 * @param activityObj
	 * @param vw
	 */
	public static void hideEleByObj(Activity activityObj, View vw)
	{
		if( vw != null )
		{
			vw.setVisibility( View.GONE );
		}
	}

	/**
	 * @param activityObj
	 * @param vw
	 */
	public void hideEleByObjInvisible( Activity activityObj, View vw )
	{
		if( vw != null )
		{
			vw.setVisibility( View.INVISIBLE );
		}
	}

	/**
	 * @param activityObj
	 * @param vw
	 */
	public void showEleByObj( Activity activityObj, View vw )
	{
		if( vw != null )
		{
			vw.setVisibility( View.VISIBLE );
		}
	}

	/**
	 *
	 * @param activityObj
	 * @param idS
	 */
	public void removeElements( Activity activityObj, int[] idS )
	{
		int size = idS.length;

		for( int i=0; i < size; i++ )
		{
			View vw = (View) activityObj.findViewById( idS[i] );
			removeEleByObj( vw );
		}
	}

	/**
	 *
	 * @param activityObj
	 */
	public void removeEleByInt( Activity activityObj, ArrayList<Integer> idArr )
	{
		for (Integer id : idArr)
		{
			View vw = (View) activityObj.findViewById( id );
			removeEleByObj( vw );
		}
	}

	/**
	 *
	 * @param activityObj
	 */
	public void removeElements( Activity activityObj, ArrayList<View> viewArr )
	{
		for (View vw : viewArr)
		{
			removeEleByObj( vw );
		}
	}

	/**
	 * @param vw
	 */
	public void removeEleByObj( View vw )
	{
		if( vw != null )
		{
			((ViewManager)vw.getParent()).removeView(vw);
		}
	}

	/**
	 *
	 * @param ctx
	 * @param msg
	 */
	public void showToast( Context ctx, String msg )
	{
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 *
	 * @param ctx
	 * @param msg
	 */
	public void showToast( Context ctx, String msg, int length )
	{
		Toast.makeText(ctx, msg, length).show();
	}

	public static void showReservationDialog(Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog.setContentView(R.layout.custom_dialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		dialog.show();
	}


	/**
	 *
	 * @param ctx
	 * @param title
	 * @param msg
	 * @param buttonTitle
	 */
	public void dialogMsg( Context ctx, String title, String msg, String buttonTitle )
	{
		//only show message if activity is alive and not beind closed
		if(!((Activity) ctx).isFinishing())
		{
			//show dialog
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
			builder.setTitle( title );
			builder.setMessage( msg );
			builder.setPositiveButton( buttonTitle, null );
			android.app.AlertDialog dialog = builder.show();

			TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER);
			dialog.show();
		}
	}

	/**
	 *
	 * @param icon
	 * @return
	 */
	public int iconStrToInt( String icon )
	{
		if( icon.equals("hm") )
		{
			//return R.drawable.home;
		}
		else if( icon.equals("h") )
		{
			//return R.drawable.ic_action_home_favorite_black;
		}

		return 0;
	}

	/**
	 *
	 * @param iconArr
	 * @return
	 */
	public int[] iconArrtoImgArr( String[] iconArr )
	{
		int len = iconArr.length;

		int[] imageArr = new int[ len ];
		for( int i = 0; i < len; i++ )
		{
			imageArr[ i ] = iconStrToInt( iconArr[ i ] );
		}

		return imageArr;
	}

	/**
	 * 	String array version
	 */
	public void loadSpinner( final Context ctx, Spinner spinner, String[] keys, String[] vals )
	{
		if( keys.length > 0 )
		{
			List<spinnerObject> spinnerOpts = getSpinnerOptions( ctx, keys, vals );
			spinnerAdapter spinerAdp = new spinnerAdapter( ctx, android.R.layout.simple_spinner_item, spinnerOpts);
			spinerAdp.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

			spinner.setAdapter( spinerAdp );

			//set selection to empty so that default first item will not be set automatically
			spinner.setSelection(AdapterView.INVALID_POSITION);
		}
	}

	/**
	 * 	String array version
	 */
	public void clearSpinner( final Context ctx, Spinner spinner )
	{
		if( spinner.getCount() > 0 )
		{
			spinnerAdapter spinerAdp = (spinnerAdapter) spinner.getAdapter();
			if(spinerAdp != null)
			{
				spinerAdp.clearAdp();
				spinner.setAdapter( spinerAdp );
			}
		}
	}

	/**
	 *
	 * @return
	 */
	public static List <spinnerObject> getSpinnerOptions(final Context ctx, String[] keyArr, String[] valArr)
	{
		List < spinnerObject > options = new ArrayList < spinnerObject > ();
		int size = keyArr.length;

		for( int i=0; i < size; i++ )
		{
			//options.add ( new spinnerObject ( imlb.strToFloatSecure( ctx, keyArr[i]), valArr[i] ) );
			options.add ( new spinnerObject ( keyArr[i], valArr[i] ) );
		}

		return options;
	}


	/**
	 * 	JSON array version
	 */
	public void loadSpinner(final Context ctx, Spinner spinner, JSONArray keys, JSONArray vals )
	{
		if( keys.length() > 0 )
		{
			List <spinnerObject> spinnerOpts = getSpinnerOptions( ctx, keys, vals );
			spinnerAdapter spinerAdp = new spinnerAdapter( ctx, android.R.layout.simple_spinner_item, spinnerOpts);
			spinerAdp.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
			spinner.setAdapter( spinerAdp );

			//set selection to empty so that default first item will not be set automatically
			spinner.setSelection(AdapterView.INVALID_POSITION);
		}
	}


	/**
	 *
	 * @return
	 */
	public List <spinnerObject> getSpinnerOptions( final Context ctx, JSONArray keys, JSONArray vals )
	{
		String[] keyArr =  imlb.singleton().jsonToStrArr( ctx, keys );
		String[] valArr =  imlb.singleton().jsonToStrArr( ctx, vals );

		List < spinnerObject > options = new ArrayList < spinnerObject > ();
		int size = keyArr.length;
		for( int i=0; i < size; i++ )
		{
			//options.add ( new spinnerObject ( imlb.strToFloatSecure( ctx, keyArr[i]), valArr[i] ) );
			options.add ( new spinnerObject ( keyArr[i], valArr[i] ) );
		}

		return options;
	}

	public static void spinnerSetValue( Spinner spinner, String id )
	{
		spinnerAdapter spnAdp = (spinnerAdapter) spinner.getAdapter();
		spinner.setSelection( spnAdp.getPosition( id ) );
	}



	/**
	 *  returns all view under Root that matches given tag: a jQuery like class/tag selector
	 * @return
	 */
	public ArrayList<View> getViewsByTag( ViewGroup root, String tag )
	{
		ArrayList<View> views = new ArrayList<View>();
		final int childCount = root.getChildCount();
		for ( int i = 0; i < childCount; i++ )
		{
			final View child = root.getChildAt(i);
			if ( child instanceof ViewGroup )
			{
				views.addAll(getViewsByTag((ViewGroup) child, tag));
			}

			final Object tagObj = child.getTag();
			if ( tagObj != null && tagObj.equals(tag) )
			{
				views.add(child);
			}
		}
		return views;
	}

	/**
	 *  returns single view under Root that contains given string in tag: a jQuery like class/tag selector
	 *  QC_OPT: optimization possible and required here
	 * @return
	 */
	public static View getSingleViewIfTagContains(ViewGroup root, CharSequence str)
	{
		if( root != null )
		{
			final int childCount = root.getChildCount();
			for ( int i = 0; i < childCount; i++ )
			{
				View child = root.getChildAt(i);

				final Object tagObj = child.getTag();
				if ( tagObj != null && tagObj.toString().contains( str ) )
				{
					return child;
				}

				if( child instanceof ViewGroup )
				{
					child = getSingleViewIfTagContains((ViewGroup) child, str);
					if( child != null )
					{
						//return child view found that contains tag inside group
						return child;
					}
				}
			}
		}
		return null;
	}

	/**
	 *  returns all view under Root that contains given string in tag: a jQuery like class/tag selector
	 * @return
	 */
	public static ArrayList<View> getViewsIfTagContains( ViewGroup root, CharSequence str )
	{
		ArrayList<View> views = new ArrayList<View>();
		if( root != null )
		{
			final int childCount = root.getChildCount();
			for ( int i = 0; i < childCount; i++ )
			{
				final View child = root.getChildAt(i);
				if ( child instanceof ViewGroup )
				{
					views.addAll(getViewsIfTagContains((ViewGroup) child, str));
				}

				final Object tagObj = child.getTag();
				if ( tagObj != null && tagObj.toString().contains( str ) )
				{
					views.add(child);
				}
			}
		}
		return views;
	}

	/**
	 *  returns all view under Root that contains given string in tag: a jQuery like class/tag selector
	 * @return
	 */
	public ArrayList<View> getViewsIfTagContainsFromArrAlso( ViewGroup root, CharSequence str, String[] itemArr )
	{
		ArrayList<View> views = new ArrayList<View>();
		if( root != null )
		{
			final int childCount = root.getChildCount();
			for ( int i = 0; i < childCount; i++ )
			{
				final View child = root.getChildAt(i);
				if ( child instanceof ViewGroup )
				{
					views.addAll(getViewsIfTagContains((ViewGroup) child, str));
				}

				final Object tagObj = child.getTag();
				if ( tagObj != null && tagObj.toString().contains( str ) && imlb.singleton().strContainsItemFromStrArr( tagObj.toString(), itemArr) )
				{
					views.add(child);
				}
			}
		}
		return views;
	}

	/**
	 *  hides all child of view group
	 *  @since 27-05-2015
	 */
	public void hideAllChilds( Activity activityObj, ViewGroup root )
	{
		if( root != null )
		{
			final int childCount = root.getChildCount();
			for ( int i = 0; i < childCount; i++ )
			{
				final View child = root.getChildAt(i);
				hideEleByObj(activityObj, child);
			}
		}
	}

	/**
	 *  show all child of view group
	 *  @since 27-05-2015
	 */
	public void showAllChilds( Activity activityObj, ViewGroup root )
	{
		if( root != null )
		{
			final int childCount = root.getChildCount();
			for ( int i = 0; i < childCount; i++ )
			{
				final View child = root.getChildAt(i);
				showEleByObj(activityObj, child);
			}
		}
	}

	/**
	 * function will unset checkout related sessions that were created during checkout process
	 */
	public static void unsetCheckOutSession( Activity activityObj, String customer_id, String session_prefix)
	{

	}

	/**
	 * taken from http://stackoverflow.com/a/18068122/1480088
	 * share and invite intent
	 *
	 */
	@SuppressLint("NewApi")
	public static void shareIntent( Activity activityObj,
									String chooserTxt, String txtSubj, String txtBody , String to )
	{

		/**
		 * it uses intent type message/rfc822 to prevent all other apps, then will add only necessary one. <br>
		 * though it will still add garbage like email clients
		 */
		Intent initialIntent = new Intent();
		initialIntent.setAction(Intent.ACTION_SEND);
		initialIntent.setType("text/plain");

		// Native email client doesn't currently support HTML, but it doesn't
		// hurt to try in case they fix it
		initialIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
		initialIntent.putExtra(Intent.EXTRA_SUBJECT, txtSubj);
		initialIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(txtBody));
		//initialIntent.putExtra(Intent.EXTRA_STREAM, imgUri);

		Intent openInChooser = Intent.createChooser(initialIntent, chooserTxt);


		activityObj.startActivity(openInChooser);
	}

	/**
	 * de reference all references to object and variable it will help prevent memory leak/massive memory usage
	 */
	public void do_cleanup()
	{
		if( dialog_loader != null )
		{
			dialog_loader.dismiss();
			dialog_loader = null;
		}
		imui = null;
	}

	/**
	 * added on 08-02-2015 now it is required to sync do_cleanup function with this finalize call
	 */
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}

	/**
	 * get country id
	 */
	public String countyNameFromCode(Context activityObj , String code)
	{
		Locale loc_location = new Locale("", code);
		String LocaleLocation = loc_location.getDisplayCountry();

		return LocaleLocation;
	}


	public static float currencyValueToFloatSecure(String str)
	{
		if (imlb.isStrEmptyStrict(str))
		{
			return 0;
		}

		str = str.replace("$","");
		str = str.replace(",","");

		if (str.length() > 0)
		{
			return Float.parseFloat(str);
		}
		else
		{
			return 0;
		}
	}

	/*********************************   Login Sign-Up Function   **************************************/

	public static void performClick(View view) {
		// apply with email job
		if (imui.singleton().versionSDK_INT() >= 15) {
			view.callOnClick();
		} else {
			view.performClick();
		}
	}

	/**
	 * Creates a unique cache key based on a url value
	 * @param url
	 * 		url to be used in key creation
	 * @return
	 * 		cache key value
	 */
	public static String createKeyFromUrl(String url)
	{
		if( url == null )
		{
			return null;
		}

		log.singleton().debug("createKey: " + String.valueOf(url.hashCode()));
		return String.valueOf(url.hashCode());
	}

}