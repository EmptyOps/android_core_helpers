package com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;
import android.widget.Toast;

import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.log.log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @category name space imlb_ => Library helper talks with mobile platform library project of "im_"
 * @author GAMSS
 */
public class imlb
{
	public static imlb imlb = null;

	public imlb()
	{
	}

	public static imlb singleton()
	{
		if( imlb == null )
		{
			imlb = new imlb();
		}
		return imlb;
	}

	public static String getAndroidVersion()
	{
		String release = Build.VERSION.RELEASE;
		int sdkVersion = Build.VERSION.SDK_INT;
		return "Android SDK: " + sdkVersion + " (" + release +")";
	}

	/**
	 *
	 * @return
	 */
	public static boolean isStrEmpty( String str )
	{
		if( str.length() == 0 )
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
	 * @return
	 */
	public static boolean isStrEmptyStrict(String str)
	{
		if( str == null || str.length() == 0 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static String removeLastChar(String str)
	{
		if (str != null && str.length() > 0 )
		{
			str = str.substring(0, str.length()-1);
		}
		return str;
	}

	/**
	 * imc_ : swap minus to plus and plus to minus
	 */
	public static int imc_swap( int val )
	{
		return val + ( val * -2 );
	}

	/**
	 *
	 * @return
	 */
	public static Map<String, String> stringToMap(String paramsStr)
	{
		Map<String, String> params = new HashMap<String, String>();
		String[] paramArr = paramsStr.split("&");
		int size = paramArr.length;
		for (int i=0; i<size; i++)
		{
			if( !imlb.isStrEmpty( paramArr[i].trim() ) )
			{
				String[] tmpArr = paramArr[i].split("=");

				if( tmpArr.length >= 2 )
				{
					params.put( tmpArr[0], tmpArr[1] );
				}
				else if( tmpArr.length >= 1 )
				{
					params.put( tmpArr[0], "" );
				}
			}
		}

		return params;
	}

	/**
	 *
	 * @return
	 */
	public static String arrayAdd( Activity activityObj, JSONArray paramsStr)
	{


		String[] salary_expectationtmpA = imlb.jsonToStrArr(activityObj, paramsStr );
		String salary_expectation = "";
		if( salary_expectationtmpA.length > 1 )
		{
			for( int i=1; i < salary_expectationtmpA.length; i++ )
			{
				salary_expectation += salary_expectationtmpA[i] + ",";
			}

			salary_expectation = imlb.removeLastChar( salary_expectation );
		}
		else if( salary_expectationtmpA.length == 1 )
		{
			salary_expectation = salary_expectationtmpA[0];
		}

		return  salary_expectation;
	}

	/**
	 *
	 * @return
	 */
	public static String mapToString(Map<String, String> params)
	{
		String query = "";
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			query += entry.getKey()+"="+entry.getValue()+"&";
		}
		return query;
	}

	/**
	 *
	 * @return
	 */
	public static String mapToStringPrint(Map<String, String> params)
	{
		String query = "";
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			log.singleton().printLn( entry.getKey()+"="+entry.getValue()+"\n" );
		}
		return query;
	}

	/**
	 *
	 * @return
	 */
	public static boolean isMapContainsKeyPart(Map<String, String> params, String keyPart)
	{
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			if( entry.getKey().equals( keyPart ) )
			{
				return true;
			}
		}

		return false;
	}


	/**
	 * converts json string to json object
	 */
	public static JSONObject jsonStrToObj( Context ctx, String jsonStr )
	{
		try
		{
			/**
			 * [temp]:
			 * Parse string and remove all undefined index error from PHP server side
			 */
			JSONObject resObj = new JSONObject( jsonStr.trim() );

			/**
			 * added on 18-06-2015, regression testing required.
			 * Below statement will check if activity is closing due to back click or any other circumstances
			 * it will say underlying functions that activity is "redirecting" by setting explicit "_redirect".
			 */
			if( ((Activity) ctx).isFinishing() )
			{
				resObj.put("_redirect", "UNKNOWN");
			}

			return resObj;
		}
		catch ( JSONException e )
		{
			//imui.singleton().errorMsg( ctx, 1, "Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
		}
		return null;
	}

	/**
	 * converts json string to json object
	 */
	public static JSONArray jsonStrToArray( Context ctx, String jsonStr )
	{
		try
		{
			/**
			 * [temp]:
			 * Parse string and remove all undefined index error from PHP server side
			 */
			JSONArray resObj = new JSONArray( jsonStr.trim() );

			log.singleton().debug(" jsonStrToObj  " + resObj);

			return resObj;
		}
		catch ( JSONException e )
		{
			//imui.singleton().errorMsg( ctx, 1, "Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
		}
		return null;
	}

	/**
	 * converts json string to json object
	 */
	public static JSONObject jsonStrToObjInBackground( Context ctx, String jsonStr )
	{
		try
		{
			/**
			 * [temp]:
			 * Parse string and remove all undefined index error from PHP server side
			 */
			JSONObject resObj = new JSONObject( jsonStr.trim() );

			return resObj;
		}
		catch ( JSONException e )
		{
			//imui.singleton().errorMsg( ctx, 1, "Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
		}
		return null;
	}

	/**
	 * loads image from server
	 */
	public static String[] jsonToStrArr( Context ctx, JSONArray jsonArr )
	{
		int len = jsonArr.length();
		String stringArray[] = new String[ len ];
		for( int i = 0; i < len; i++ )
		{
			try
			{
				stringArray[i] = jsonArr.get(i).toString();
			}
			catch ( Exception e )
			{

			}
		}
		return stringArray;
	}

	/**
	 * loads image from server
	 */
	public static String[] jsonToStrArrFromKey( Context ctx, JSONArray jsonArr, String fromKey )
	{
		int len = jsonArr.length();
		String stringArray[] = new String[ len ];
		for( int i = 0; i < len; i++ )
		{
			try
			{
				stringArray[i] = ((JSONObject) jsonArr.get(i)).getString( fromKey );

				stringArray[i] = getFirstWord(stringArray[i]);
			}
			catch ( Exception e )
			{

			}
		}
		return stringArray;
	}

	/**
	 * loads image from server
	 */
	public static int[] jsonToIntArr( Context ctx, JSONArray jsonArr )
	{
		int len = jsonArr.length();
		int stringArray[] = new int[ len ];
		for( int i = 0; i < len; i++ )
		{
			try
			{
				stringArray[i] = strToInt(ctx, jsonArr.get(i).toString());
			}
			catch ( Exception e )
			{
				log.singleton().debug("jsonToIntArr "+e.getLocalizedMessage());
			}
		}
		return stringArray;
	}


	/**
	 *
	 * @param ctx
	 * @param jsonObj
	 * @param key
	 * @return
	 */
	public static int jsonGetInt( Context ctx, JSONObject jsonObj, String key )
	{
		String tempStr;
		try
		{
			tempStr = jsonObj.getString( key );
			return tempStr.length() > 0 ? Integer.parseInt( tempStr ) : 0;
		}
		catch ( JSONException e )
		{
			log.singleton().debug("jsonGetInt "+e.getLocalizedMessage());
		}

		return 0;
	}

	/**
	 * 	 secure version {@link} strToInt, it checks for if string length is 0 then return 0 by default
	 */
	public static float strToFloatSecure( Context ctx, String str )
	{
		try
		{
			return str.length() > 0 ? Float.parseFloat( str ) : 0;
		}
		catch ( Exception e )
		{
			log.singleton().debug("strToFloatSecure "+e.getLocalizedMessage());
		}

		return 0;
	}

	/**
	 * 	 secure version {@link} strToInt, it checks for if string length is 0 then return 0 by default
	 */
	public static int strToIntSecure( String str )
	{
		try
		{
			return str.length() > 0 ? Integer.parseInt( str ) : 0;
		}
		catch ( Exception e )
		{}

		return 0;
	}

	/**
	 * 	 secure version {@link} strToInt, it checks for if string length is 0 then return 0 by default
	 */
	public static int strToIntSecure( Context ctx, String str )
	{
		try
		{
			return str.length() > 0 ? Integer.parseInt(str) : 0;
		}
		catch ( Exception e )
		{
			log.singleton().debug("strToIntSecure "+e.getLocalizedMessage());
		}

		return 0;
	}

	/**
	 * un secure it does not check for empty string, however catches run time error and returns 0 in error case so same at the end
	 */
	public static Integer strToInt( Context ctx, String val )
	{
		try
		{
			return Integer.parseInt( val );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			log.singleton().debug("strToInt "+e.getLocalizedMessage());
		}

		return 0;
	}


	public static String fetchSubStr( String subject, String start, String end )
	{
		int startPos = subject.indexOf( start );

		if( startPos != -1 )
		{
			startPos += start.length();
			int endPos = subject.indexOf( end, startPos );
			if( endPos != -1 )
			{
				return subject.substring( startPos, endPos );
			}
			else
			{
				return subject.substring( startPos );
			}
		}

		return "";
	}

	/**
	 * remove everything starting from part of string
	 * @param subject
	 * @param needle
	 * @return
	 */
	public static String removeFromStr( String subject, String needle )
	{
		return subject.substring(0, subject.indexOf( needle ));
	}

	/**
	 *
	 * @param inputString
	 * @param items
	 * @return
	 */
	public static boolean strContainsItemFromStrArr( String inputString, String[] items )
	{
		int size = items.length;
		for(int i =0; i < size; i++)
		{
			if(inputString.contains(items[i]))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * return if map value is there empty string otherwise.
	 * @param map
	 * @param key
	 * @return
	 */
	public static String mapValue( Map<String, String> map, String key )
	{
		String val = map.get( key );
		if( val != null )
		{
			return val;
		}
		else
		{
			return "";
		}
	}


	/**
	 * saves bitmap as a file on android app directory
	 */
	public static boolean is_im_f_su_ImageExists(Activity activityObj, String imgPath)
	{
		File imageFile = new File( imgPath );
		if( imageFile != null && imageFile.exists() )
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	/**
	 * sets image to network image view from file
	 */
	public static Bitmap bitmapFromFile(Activity activityObj, final String imgPath)
	{
		File imgFile = new File(imgPath);
		if( imgFile != null && imgFile.exists() )
		{
			return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}

		return null;
	}


	/**
	 * sets image to network image view from file
	 */
	public static void setImageFromFile(Context activityObj, ImageView networkImageView, File imgFile)
	{
		if( imgFile != null && imgFile.exists() )
		{
			log.singleton().debug(imgFile.toString());
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			//log.singleton().debug("setImageFromFile: myBitmap " + ( myBitmap == null ? "is null" : "is not null" ) );
			//log.singleton().debug( "PIU_"+session.singleton(activityObj).userdata("root_id") );
			networkImageView.setImageBitmap(myBitmap);
		}
	}

	public static void openBrowser(Context activityObj ,String url)
	{
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

		try
		{
			activityObj.startActivity(browserIntent);
		}
		catch (ActivityNotFoundException ex)
		{
			Toast.makeText(activityObj, "No application can handle this request."
					+ " Please install a webbrowser",  Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * returns date representable string in  locale compatible format to show end users
	 * @return
	 */
	public static String dateLocaleStr( String mySqlDate )
	{
		String[] dateParts = mySqlDate.split("-");
		return dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
	}

	/**
	 * returns date representable string in  locale compatible format to show end users
	 * @return
	 */
	public static String dateLocaleStr( int day, int month, int year )
	{
		month = (month+1);
		return ( day < 10 ? "0"+day : day )  + "-" + ( month < 10 ? "0"+month : month ) + "-" + year;
	}

	/**
	 * returns year representable string in  locale compatible format to show end users
	 * @return
	 */
	public static String dateYear( String mySqlDate )
	{
		String[] dateParts = mySqlDate.split("-");
		return dateParts[0];
	}


	/**
	 * returns after converting date representable string in locale compatible format to MySql REST format
	 * @return
	 */
	public static String restDateFromLocaleDate( String locale_date_str )
	{
		String[] dateParts = locale_date_str.split("-");
		return dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
	}

	/**
	 * returns date representable string in  locale compatible format to show end users
	 * @return
	 */
	public static String[] datePartsFromLocaleStr( String mySqlDate )
	{
		String[] dateParts = mySqlDate.split("-");
		return dateParts;
	}

	/**
	 *
	 */
	public static String[] removeElementsByKey(String[] input, int key)
	{
		int size = input.length;
		String[] result = new String[size-1];
		int cnt = 0;
		for( int i=0; i<size; i++ )
		{
			if( i != key )
			{
				result[cnt++] = input[i];
			}
		}

		return result;
	}

	/**
	 *
	 */
	public static String[] addElementInArr(String[] existingArr, String ele)
	{
		int size = existingArr.length;
		String[] result = new String[size+1];
		for( int i=0; i<size; i++ )
		{
			result[i] = existingArr[i];
		}

		/**
		 * add the intended element at last element in the array
		 */
		result[size] = ele;

		return result;
	}

	/**
	 *
	 */
	public static int[] addElementInIntArr(int[] existingArr, int ele)
	{
		int size = existingArr.length;
		int[] result = new int[size+1];
		for( int i=0; i<size; i++ )
		{
			result[i] = existingArr[i];
		}

		/**
		 * add the intended element at last element in the array
		 */
		result[size] = ele;

		return result;
	}

	/**
	 *
	 */
	public static int[] removeElementsByKey(int[] input, int key)
	{
		int size = input.length;
		int[] result = new int[size-1];
		int cnt = 0;
		for( int i=0; i<size; i++ )
		{
			if( i != key )
			{
				result[cnt++] = input[i];
			}
		}

		return result;
	}

	public static String timestamp()
	{
		return String.valueOf( (System.currentTimeMillis()/1000) );
	}

	/**
	 * returns map after merging them
	 */
	public static Map<String, String> mergeMap( Map<String, String> map1, Map<String, String> map2 )
	{
		for (Map.Entry<String, String> e : map2.entrySet())
		{
			map1.put( e.getKey(), e.getValue() );
		}

		return map1;
	}

	/**
	 * returns jsonString after converted from java map
	 */
	public static String mapToJsonString( Map<String, String> map )
	{
		return new JSONObject(map).toString();
	}


	/**
	 * un recursive version so only work for non-dimensional jsonObject
	 */
	public static Map<String,String> jsonObjToMap(JSONObject json) throws JSONException
	{
		Map<String,String> out = new HashMap<String, String>();
		Iterator<String> keys = json.keys();
		while(keys.hasNext())
		{
			String key = keys.next();
			out.put(key,json.getString(key));
		}
		return out;
	}

	/**
	 * recursive version so only work for nested jsonObjects also
	 */
	public static Map<String,String> jsonObjToMapRecursive(JSONObject json , Map<String,String> out) throws JSONException
	{
		Iterator<String> keys = json.keys();
		while(keys.hasNext())
		{
			String key = keys.next();
			String val = null;
			try
			{
				JSONObject value = json.getJSONObject(key);
				jsonObjToMapRecursive(value,out);
			}
			catch(Exception e)
			{
				val = json.getString(key);
			}

			if(val != null)
			{
				out.put(key,val);
			}
		}
		return out;
	}

	/**
	 * finds sqrt of BigInteger
	 */
	public static BigInteger sqrtBigInteger(BigInteger n )
	{
		BigInteger a = BigInteger.ONE;
		BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
		while ( b.compareTo(a) >= 0 )
		{
			BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
			if ( mid.multiply(mid).compareTo(n) > 0 )
				b = mid.subtract(BigInteger.ONE);
			else
				a = mid.add(BigInteger.ONE);
		}

		return a.subtract(BigInteger.ONE);
	}

	/**
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource( File imgFile, int reqWidth, int reqHeight )
	{
		/**
		 *  First decode with inJustDecodeBounds=true to check dimensions
		 */
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile( imgFile.getAbsolutePath(), options );

		/**
		 *  Calculate inSampleSize
		 */
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		/**
		 *  Decode bitmap with inSampleSize set
		 */
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile( imgFile.getAbsolutePath(), options );
	}

	/**
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight )
	{
//		/**
//		 *  Raw height and width of image
//		 */
//		final int height = options.outHeight;
//		final int width = options.outWidth;
//		int inSampleSize = 1;
//		/**
//		 * check if image landscaped then turn required height width to landscape orientation
//		 */
//		if( width > height )
//		{
//			int temp = reqWidth;
//			reqWidth = reqHeight;
//			reqHeight = temp;
//		}
//
//		if ( height > reqHeight || width > reqWidth )
//		{
//
//			final int halfHeight = height / 2;
//			final int halfWidth = width / 2;
//
//			// Calculate the largest inSampleSize value that is a power of 2 and
//			// keeps both
//			// height and width larger than the requested height and width.
//			while ( (halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth )
//			{
//				inSampleSize *= 2;
//			}
//		}
//
//		return inSampleSize;

		// Facebook image size
		final int IMAGE_MAX_SIZE = 630;

		int inSampleSize = 1;
		options.inJustDecodeBounds = true;
		if (options.outHeight > IMAGE_MAX_SIZE || options.outWidth > IMAGE_MAX_SIZE)
		{
			inSampleSize = (int)Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
		}


		log.singleton().debug("inSampleSize: " + inSampleSize);
		return inSampleSize;
	}


	/**
	 * Returns a psuedo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimim value
	 * @param max Maximim value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max)
	{

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	/**
	 *
	 * @param fm
	 */
	public static void displayBackStack( FragmentManager fm )
	{
		Thread.dumpStack();
//		int count = fm.getBackStackEntryCount();
//		log.singleton().debug("Backstack log: There are " + count + " entries");
//		for ( int i = 0; i < count; i++ )
//		{
//			// Display Backstack-entry data like
//			String name = fm.getBackStackEntryAt(i).getName();
//			log.singleton().debug("Entry " + i + ": " + name);
//		}
	}

	/**
	 *
	 * @return
	 */
	public static boolean isNetworkAvailable( Context ctx )
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * do delayed execution of some task on a separate thread after specified time
	 */
	public static void doDelayedExecution( long delay, TimeUnit tu )
	{
		final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

		exec.schedule(new Runnable(){
			@Override
			public void run(){
				//MyMethod();
			}
		}, delay, tu);
	}

	/**
	 * 1000 milliseconds is one second.
	 * @param ms milliseconds
	 */
	public static void im_sleep( int ms )
	{
		try
		{
			Thread.sleep(ms);
		}
		catch ( InterruptedException ex )
		{
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * removes suffix from REST error response to be compatible with REST client forms
	 */
	public static String[] removeSuffix( Activity activityObj, String[] keyArr )
	{
		int size = keyArr.length;
		for( int i=0; i<size; i++ )
		{
			if( keyArr[i].substring( Math.max( keyArr[i].length() - 2, 0 ) ).equals( "_n" ) )
			{
				keyArr[i] = keyArr[i].substring( 0 , keyArr[i].length() - 2 );
			}
		}

		return keyArr;
	}

	/**
	 * deletes file
	 */
	public static boolean removeFile( Activity activityObj, File file)
	{
		if (file.exists())
		{
			if(file.delete())
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Creates a unique cache key based on a url value
	 * @param url
	 * 		url to be used in key creation
	 * @return
	 * 		cache key value
	 */
	public static String urlKey(String url)
	{
		return String.valueOf(url.hashCode());
	}

	public static boolean isStringExistInStrArr(String sub, String[] strArr)
	{
		int size = strArr.length;
		//log.singleton().debug(" isStringExistInStrArr  " + size);
		for(int i=0; i < size; i++)
		{
			if( sub.equals(strArr[i]) )
			{
				return true;
			}
		}

		return false;
	}

	public static boolean isStringContainsInStrArr(String sub, String[] strArr)
	{
		int size = strArr.length;
		log.singleton().debug(" isStringExistInStrArr  " + size);
		for(int i=0; i < size; i++)
		{
			log.singleton().debug(" isStringExistInStrArr 1  " + sub);
			log.singleton().debug(" isStringExistInStrArr 2  " + strArr[i]);
			if( sub.contains(strArr[i]) )
			{
				log.singleton().debug(" isStringExistInStrArr 3  " + strArr[i]);
				return true;
			}
		}

		return false;
	}

	public static boolean containsIgnoreCase(String src, String what)
	{
		final int length = what.length();
		if (length == 0)
			return true; // Empty string is contained

		final char firstLo = Character.toLowerCase(what.charAt(0));
		final char firstUp = Character.toUpperCase(what.charAt(0));

		for (int i = src.length() - length; i >= 0; i--) {
			// Quick check before calling the more expensive regionMatches() method:
			final char ch = src.charAt(i);
			if (ch != firstLo && ch != firstUp)
				continue;

			if (src.regionMatches(true, i, what, 0, length))
				return true;
		}

		return false;
	}

	/**
	 * return boolean stating either device is considered a low memory or low configuration device
	 * considers low memory if RAM is below or equal to 312 MB or API is < 9
	 */
	public boolean isLowMemoryOrLowConfigurationDevice(Context activityObj)
	{
		log.singleton().debug("isLowMemoryOrLowConfigurationDevice API " + imui.singleton().versionSDK_INT());
		if( imui.singleton().versionSDK_INT() < 9 || getRAMMemorySize(activityObj) <= (312 * 1024 * 1024) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * return total device RAM size in bytes
	 */
	@SuppressLint("NewApi")
	public long getRAMMemorySize(Context activityObj)
	{
		if( imui.singleton().versionSDK_INT() >= 16 )
		{
			ActivityManager actManager = (ActivityManager) activityObj.getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
			actManager.getMemoryInfo(memInfo);
			log.singleton().debug("memInfo.totalMem: " + memInfo.totalMem);

			return memInfo.totalMem;
		}
		else
		{
			try
			{
				String str1 = "/proc/meminfo";
				String str2;
				String[] arrayOfString;

				FileReader localFileReader = new FileReader(str1);
				BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
				str2 = localBufferedReader.readLine();// meminfo
				arrayOfString = str2.split("\\s+");
				for ( String num : arrayOfString )
				{
					log.singleton().debug( str2 + ": " + num + "\t");
				}

				localBufferedReader.close();
				// total Memory
				return Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			}
			catch ( IOException e )
			{
				//imui.singleton().errorMsg( activityObj, 1, "Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
			}
		}

		return -1;
	}

	/**
	 * de reference all references to object and variable it will help prevent memory leak/massive memory usage
	 */
	public void do_cleanup()
	{
		imlb = null;
	}

	public static String timestamptolocaldate( BigInteger milliSeconds, boolean is_today )
	{
		SimpleDateFormat simpleDateFormat;
		if( !is_today )
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a ");
		else
			simpleDateFormat = new SimpleDateFormat("hh:mm a");

		log.singleton().debug("timestamptolocaldate:: time="+milliSeconds.longValue()+ " is_today="+ is_today + " result=" + simpleDateFormat.format(milliSeconds.longValue()) );

		return simpleDateFormat.format(milliSeconds.longValue());
	}

	public static String timestamptolocaldate( long milliSeconds, boolean is_today )
	{
		SimpleDateFormat simpleDateFormat;
		if( !is_today )
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		else
			simpleDateFormat = new SimpleDateFormat("HH:mm");

		log.singleton().debug("timestamptolocaldate:: time="+milliSeconds+ " is_today="+ is_today + " result=" + simpleDateFormat.format(milliSeconds) );

		return simpleDateFormat.format(milliSeconds);
	}

	public static boolean isDateToday(BigInteger milliSeconds)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds.longValue());

		Date getDate = calendar.getTime();

		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date startDate = calendar.getTime();

		return getDate.compareTo(startDate) > 0;

	}

	public static boolean isDateToday(long milliSeconds)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);

		Date getDate = calendar.getTime();

		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date startDate = calendar.getTime();

		return getDate.compareTo(startDate) > 0;

	}

	public static long dateToMilliSeconds( Activity activityObj,  String dateStr, String format )
	{
		if( isStrEmpty( format  ) )
		{
			format = "yyyy-MM-dd HH:mm:ss";
		}

		Date dateTime = null;
		try
		{
			dateTime = new SimpleDateFormat( format, Locale.ENGLISH).parse( dateStr );
		}
		catch (ParseException e)
		{
			log.singleton().debug("dateToMilliSeconds"+e.getLocalizedMessage());
		}

		return dateTime.getTime();
	}

	/**
	 * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
	 * @param context Context reference to get the TelephonyManager instance from
	 * @return country code or null
	 */
	public static String getCurrentCountryCode2Char(Context context)
	{
		try {
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			final String simCountry = tm.getSimCountryIso();
			if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
				return simCountry.toLowerCase(Locale.US);
			}
			else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
				String networkCountry = tm.getNetworkCountryIso();
				if (networkCountry != null && networkCountry.length() == 2)
				{ // network country code is available
					return networkCountry.toLowerCase(Locale.US);
				}
			}
		}
		catch (Exception e) { }
		return null;
	}

	/**
	 * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
	 * @param context Context reference to get the TelephonyManager instance from
	 * @return country code or null
	 */
	public static String getCurrentCityCode2Char(Context context)
	{
		try {
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			final String simCountry = tm.getSimCountryIso();
			if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
				return simCountry.toLowerCase(Locale.US);
			}
			else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
				String networkCountry = tm.getNetworkCountryIso();
				if (networkCountry != null && networkCountry.length() == 2)
				{ // network country code is available
					return networkCountry.toLowerCase(Locale.US);
				}
			}
		}
		catch (Exception e) { }
		return null;
	}

	public static String getFirstWord(String text)
	{

		String subString = "";
		if (text.length() > 1)
		{ // Check if there is more than one word.
			// full file name
			int iend = text.indexOf(","); //this finds the first occurrence of "."

			if (iend != -1)
				subString = text.substring(0 , iend); //this will give abc
		}

		return  subString;
	}

	public static String getDeviceLanguage()
	{
		return Locale.getDefault().getLanguage().toLowerCase();
	}

	public static String getAppVersion(Activity activity)
	{
		PackageInfo pInfo = null;
		try
		{
			pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			return pInfo.versionName;
		} catch (PackageManager.NameNotFoundException e)
		{
			//imui.singleton().errorMsg();
		}

		return "";
	}


	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

	public static float getScreenWidth(Context context)
	{
//		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//
//		return metrics.widthPixels;

		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		return  width;
	}

	public static  float tabletscreenwidth(Context context)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();

		float screenWidth  = dm.widthPixels;

		return screenWidth;
	}

	public static int getScreenHeight(Context context) {
//		return Resources.getSystem().getDisplayMetrics().heightPixels;
		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y;
		return  height;
	}

	public static int doubleToInt(double d1) {

		Double d = new Double(d1);

		return d.intValue();
	}

	public static String formatUrl(String url)
	{
		if(url.contains(" "))
		{
			return url.replace(" ","%20");
		}

		return url;
	}

	public static Boolean isTablet(Context context) {

		if ((context.getResources().getConfiguration().screenLayout &
				Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {

			return true;
		}
		return false;
	}



	public static boolean isExistPackage(Activity context,Intent intent, String packageName)
	{

		PackageManager packManager = context.getPackageManager();

		List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(intent,  PackageManager.MATCH_DEFAULT_ONLY);


		for(ResolveInfo resolveInfo: resolvedInfoList){
			if(resolveInfo.activityInfo.packageName.startsWith(packageName))
			{
				intent.setClassName(
						resolveInfo.activityInfo.packageName,
						resolveInfo.activityInfo.name );
				return true;
			}
		}
		return false;
	}

	public static Map<String, String> resolveGoogleAddress(Address address)
	{
		//
		if( address == null )
		{
			return null;
		}

		Map<String, String> res = new HashMap<String, String>();

		//
		if (address.getPostalCode() != null)
		{
			res.put("postcode", address.getPostalCode());
		}
		else
		{
			res.put("postcode", "");
		}

		//
		if (address.getAddressLine(0) != null)
		{
			res.put("address", address.getAddressLine(0));
		}
		else
		{
			res.put("address", "");
		}

		//
		if (address.getSubLocality() != null)
		{
			res.put("landmark", address.getAddressLine(0));
		}
		else
		{
			res.put("landmark", "");
		}

		//
		if (address.getLocality() != null)
		{
			res.put("city", address.getLocality());
		}
		else
		{
			res.put("city", "");
		}

		//
		if (address.getAdminArea() != null)
		{
			res.put("state", address.getAdminArea());
		}
		else
		{
			res.put("state", "");
		}

		//
		if (address.getCountryName() != null)
		{
			res.put("country", address.getCountryName());
		}
		else
		{
			res.put("country", "");
		}

		return res;
	}

	public static String filterURL(String url)
	{
		if(url.contains(" "))
		{
			return url.replace(" ","%20");
		}

		return url;
	}

	/**
	 * get uri to any resource type
	 * @param context - context
	 * @param resId - resource id
	 * @throws Resources.NotFoundException if the given ID does not exist.
	 * @return - Uri to resource by given id
	 */
	public static final Uri getUriToResource(@NonNull Context context,
											 @AnyRes int resId)
			throws Resources.NotFoundException
	{
		/** Return a Resources instance for your application's package. */
		Resources res = context.getResources();
		/**
		 * Creates a Uri which parses the given encoded URI string.
		 * @param uriString an RFC 2396-compliant, encoded URI
		 * @throws NullPointerException if uriString is null
		 * @return Uri for this given uri string
		 */
		Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
				"://" + res.getResourcePackageName(resId)
				+ '/' + res.getResourceTypeName(resId)
				+ '/' + res.getResourceEntryName(resId));
		/** return uri */
		return resUri;
	}

	public static Uri getUrifromDrawable(Activity activity,int resID)
	{
		Bitmap bm = BitmapFactory.decodeResource( activity.getResources(), resID);

		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		File file = new File(extStorageDirectory, "MyIMG.png");
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Uri.fromFile(file);
	}

	//
	public static void downloadFile(final InputStream inputStream, final Context ctx){
		Thread thread = new Thread() {
			@Override
			public void run() {
				try
				{
					String filePath = ctx.getFilesDir().getPath().toString() + "/bigFile.pkg";
					File file = new File(filePath);
					OutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
					int bufferSize = 1024;
					byte[] buffer = new byte[bufferSize];
					int len;
					while ((len = inputStream.read(buffer)) != -1) {
						stream.write(buffer, 0, len);
					}
					if(stream != null) {
						stream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	public static String converttoAMformat(String tmpStr)
	{
		if(tmpStr.contains("a.m."))
		{
			tmpStr = tmpStr.replace("a.m.","AM");
		}
		else
		{
			tmpStr = tmpStr.replace("p.m.","PM");
		}

		return tmpStr;
	}

	public static String converttoamformat(String tmpStr)
	{
		if(tmpStr.contains("AM"))
		{
			tmpStr = tmpStr.replace("AM","a.m.");
		}
		else
		{
			tmpStr = tmpStr.replace("PM","p.m.");
		}

		return tmpStr;
	}

	public static String gmttoLocalDate(String currentDate,String dateFormate)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormate);

		Date date=null;
		try {
			date = dateFormat.parse(currentDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String timeZone = Calendar.getInstance().getTimeZone().getID();

		Date localDate = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));

		SimpleDateFormat outputFmt = new SimpleDateFormat(dateFormate);

		return outputFmt.format(localDate);
	}

	public static void reloadActivity( Activity activityObj )
	{
		if( imui.singleton().versionSDK_INT() >= 11 )
		{
			activityObj.recreate();
		}
	}

}