package com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.libraries;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.android_sqlite_helper.sqLiteHelper;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.helpers.imlb;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.helpers.imui;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class session
{
	public static session session = null;
	Context ctx = null;

	//Expand - Collapse Text view session

	public session(Context activityObj)
	{
		ctx = activityObj;
	}

	public static session singleton(Context activityObj)
	{
		if(session == null)
		{
			session = new session(activityObj);
		}
		return session;
	}

	public String getSessionId(Context activityObj )
	{
		String session_id = sqLiteHelper.singleton(activityObj).getConfigKey( "session_id" );
		if( session_id.length() == 0 )
		{
			return "0";
		}
		else
		{
			return session_id;
		}
	}

	public void setSessionId( String session_id )
	{
		sqLiteHelper.singleton( ctx ).updInsConfigKey( "session_id", session_id );
	}

	/**
	 *
	 */
	public boolean isSession( String key )
	{
		String val = sqLiteHelper.singleton( ctx ).getConfigKey( key );
		if( val.length() == 0 )
		{
			return false;
		}
		else
		{
			return true;
		}
//
//		String val = sess.get( key );
//		if( val != null )
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
	}

	public String userdata(String key )
	{
		return sqLiteHelper.singleton( ctx ).getConfigKey(key);

//		String val = sess.get( key );
//		if( val != null )
//		{
//			return val;
//		}
//		else
//		{
//			return "";
//			//return sqLiteHelper.singleton().getConfigKey( key );
//		}
	}

	public int userdata_int( String key )
	{
		return imlb.strToIntSecure(ctx, sqLiteHelper.singleton(ctx).getConfigKey(key));
	}

	/**
	 * stores string value in session
	 * @param key
	 * @param val
	 */
	public void set_userdata(String key, String val )
	{
		//sess.put( key, val );
		sqLiteHelper.singleton( ctx ).updInsConfigKey( key, val );
	}

	/**
	 * stores map value in session
	 * @param key
	 * @param map
	 */
	public void set_userdata(String key, Map<String, String> map )
	{
		//sess.put( key, val );
		sqLiteHelper.singleton( ctx ).updInsConfigKey(key, imlb.singleton().mapToJsonString(map));
	}

	public void unset_userdata( String key )
	{
		//sess.remove( key );
		sqLiteHelper.singleton( ctx ).deleteConfigKey(key);
	}

	public void unset_userdataWildCard( String key )
	{
		//sess.remove( key );
		sqLiteHelper.singleton( ctx ).deleteConfigKeyWildCard( key );
	}

	public void unset_userdata_listAll( String key )
	{
		//sess.remove( key );
		sqLiteHelper.singleton( ctx ).deleteDataListAll( key );
	}

	////////////////////// DATA LIST SESSION FUNCTIONS END ///////////////////////////


	/**
	 * set gps country code
	 */
	public void Setgpslocation(String code)
	{
		set_userdata("selectedGpsCode", code);
	}

	/**
	 * retrive gps country code
	 */
	public String loadgpsLocationSelected()
	{
		return userdata("selectedGpsCode");
	}

	/**
	 * retrive gps city
	 */
	public String loadgpsCitySelected()
	{
		return userdata("selectedGps_city");
	}

	/**
	 * set gps city
	 */
	public void SetgpsCity(String code)
	{
		set_userdata("selectedGps_city", code);
	}

	/**
	 * retrive lcoation
	 */
	public String loadLocationSelected()
	{
		return userdata("selectedCountryCode");
	}

	/**
	 * set city location
	 */
	public String loadCityLocationSelected()
	{
		return userdata("selectedCountryCodeCity");
	}

	/**
	 * set city location
	 */
	public void setCityLocationSelected( String val )
	{
		set_userdata("selectedCountryCodeCity", val);
	}

	/**
	 * set location
	 */
	public void SetLocation(String code)
	{
		set_userdata("selectedCountryCode", code);
	}

	/**
	 * set location
	 */
	public void setGeoLocation(String last_latitude, String last_longitude, String last_location_text)
	{
		set_userdata("last_latitude", last_latitude);
		set_userdata("last_longitude", last_longitude);
		set_userdata("last_location_text", last_location_text);

	}


	/**
	 * load language
	 */
	public String loadLanguageSelected()
	{
		return userdata("selectedLanguageCode");
	}

	/**
	 * set language code
	 */
	public void SetLanguage(String code)
	{
		set_userdata("selectedLanguageCode", code);
	}

	/**
	 * retrive language name
	 */
	public String loadLangSelected()
	{
		return userdata("lang");
	}

	/**
	 * set language
	 */
	public void SetLang(String code)
	{
		set_userdata("lang", code);
	}


	/**
	 *
	 * @param responseObj
	 */
	public void setLoginSessions(Activity activityObj, JSONObject responseObj )
	{
		try
		{
			//set_userdata("session_id", responseObj.getString("session_id"));
			set_userdata("email", responseObj.getString("customer_emailid") );
			setSessionId(responseObj.getString("session_id"));
			set_userdata("customer_id", responseObj.getString("customer_id"));
			set_userdata("login_from", responseObj.getString("mode"));
			set_userdata("firstname",responseObj.getString( "customer_firstname" ) );
			set_userdata("su_type", responseObj.getString( "su_type" ) );
			set_userdata("sc_social_id", responseObj.getString( "sc_social_id" ) );
			set_userdata("seller_plan_id", responseObj.getString( "seller_plan_id" ) );
		}
		catch (JSONException e)
		{
			imui.singleton().errorMsg( activityObj, 1, "Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
		}
	}

    // user type admin
	public boolean isAdmin()
	{
		if (session.userdata("seller_plan_id").equals("1"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// user type owner
	public boolean isPropertyOwner()
	{
		if (session.userdata("seller_plan_id").equals("2"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// user type PropertyManager
	public boolean isPropertyManager()
	{
		if (session.userdata("seller_plan_id").equals("3"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	//user type staff
	public boolean isStaff()
	{
		if (session.userdata("seller_plan_id").equals("4"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public String getSearchLatitude()
	{
		String search_latitude = userdata("search_latitude");

		if( search_latitude.equals("") )
		{
			return userdata("last_latitude");
		}
		else
		{
			return search_latitude;
		}
	}

	public String getSearchLongitude()
	{
		String search_longitude = userdata("search_longitude");

		if( search_longitude.equals("") )
		{
			return userdata("last_longitude");
		}
		else
		{
			return search_longitude;
		}
	}

    /**
	 * unset Login
	 */
	public void unsetLoginSessions()
	{
		setSessionId("");
		unset_userdata("email");
		unset_userdata("customer_id");
		unset_userdata("firstname");
		unset_userdata("login_from");
		unset_userdata("su_type");
		unset_userdata("sf_social_group_id");

	}
	/************************************ LOGIN session functions END ***********************************/

	/**
	 * de reference all references to object and variable it will help prevent memory leak/massive memory usage
	 */
	public void do_cleanup()
	{
		session = null;
	}

	/**
	 *  check if user is loggedin
	 */
	public boolean isLoggedIn()
	{
		return ( !imlb.isStrEmpty( userdata("email") ) ) ? true : false;
	}


	/**
	 *
	 */
	public void setChatLoginSessions( Context activityObj, JSONObject resObj )
	{
		try
		{
			setChatLoginSessions( activityObj, resObj.getString("chat_user_id"), resObj.getString("chat_user_pass"), resObj.getString("email"), resObj.getString("full_name") );
		}
		catch (Exception e)
		{
			imui.singleton().errorMsg( activityObj, 1, "Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
		}
	}

	/**
	 *
	 */
	public void setChatLoginSessions( Context activityObj, String user_id, String pass, String chat_usr_email, String chat_usr_fname )
	{
		try
		{
			set_userdata("chat_user_id", user_id);
			set_userdata("chat_usr_email", chat_usr_email);
			set_userdata("chat_pass", pass);
			set_userdata("chat_usr_fname", chat_usr_fname);
		}
		catch (Exception e)
		{
			imui.singleton().errorMsg( activityObj, 1, "Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
		}
	}

	public void set_userdata_list( String key, String val )
	{
		sqLiteHelper.singleton( ctx ).insertDataList( key, val );
	}

	/**
	 * unset chatLogin
	 */
	public void unsetChatLoginSessions()
	{
		unsetChatConnectionLoginSessions();

		unset_userdata("chat_user_id");
		unset_userdata("chat_usr_email");
		unset_userdata("chat_pass");
		unset_userdata("chat_usr_fname");
		unset_userdata("chat_unread_cnt");

	}

	public String[] userdata_list( String key )
	{
		String[] res = new String[0];

		Cursor cursor = sqLiteHelper.singleton( ctx ).getDataList(key);
		if( cursor != null && cursor.getCount() > 0 )
		{
			int size = cursor.getCount();
			res = new String[ size ];

			cursor.moveToFirst();

			int cnt = 0;
			do
			{

				res[ cnt ] = cursor.getString( cursor.getColumnIndex("field_1") );

				cnt++;
			}while (cursor.moveToNext());

		}

		if( cursor != null )
		{
			cursor.close();
		}

		return res;
	}

	/**
	 *
	 */
	public void setChatConnectionSessions( Context activityObj )
	{
		set_userdata("is_chat_connection_on", "1");
		// ChatHelper.startBackgroundProcess(activityObj);
	}

	/**
	 * unset Login
	 */
	public void unsetChatConnectionLoginSessions()
	{
		unset_userdata("is_chat_connection_on");

	}


	/**
	 *  check if user is loggedin
	 */
	public boolean isChatCredentialAvailable()
	{
		return ( !imlb.isStrEmptyStrict( userdata("chat_user_id") ) ) ? true : false;
	}

	/**
	 *  check if user is loggedin
	 */
	public boolean isChatConnected()
	{
		return ( userdata("is_chat_connection_on").equals("1") ) ? true : false;
	}

}