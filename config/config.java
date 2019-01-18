package com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.config;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.helpers.imui;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.libraries.session;

import static com.hsquaretech.android_common_helpers.android_common_helpers.app_constants.ENV_1_REST_URL;
import static com.hsquaretech.android_common_helpers.android_common_helpers.app_constants.ENV_2_REST_URL;
import static com.hsquaretech.android_common_helpers.android_common_helpers.app_constants.ENV_3_REST_URL;

public class config
{
    public static config config = null;

    //1: development , 2: staging, 3: production
    public static final int ENV = 2;

    /**
     * REST URL
     */
    public String REST_URL = "";

    /**
     * REST sessid_index.
     * On 13-06-2015 changed to APP_KEY from PHPSESSID, to use in shared servers.
     */
    public static String sessid_index = "APP_KEY";

    //REST app_key_index
    public static final String app_key_index = "rest_v";

    //REST out_format_index
    public static final String out_format_index = "format";

    //REST rest_status_field_name
    public static final String rest_status_field_name = "type";

    //REST rest_message_field_name
    public static final String rest_message_field_name = "msg";

    //REST rest_response_field_name
    public static final String rest_response_field_name = "response";

    //REST rest_version
    public static final float rest_ver = 1;


    /**
     * directories
     */
    public static final String PICT_DIR = "Pictures";

    /**
     * IM_: network background task tag
     */
    public static final String IM_NBT = "IM_NBT";

    /**
     *
     */
    public config()
    {
        //REST to dev or staging or live server
        if( this.ENV == 3 )
        {
            this.REST_URL = ENV_3_REST_URL;
        }

        else if( this.ENV == 2 )
        {
            this.REST_URL = ENV_2_REST_URL;

        }
        else if( this.ENV == 1 )
        {
            this.REST_URL = ENV_1_REST_URL;
        }
    }

    public static config singleton()
    {
        if( config == null )
        {
            config = new config();
        }

        return config;
    }
    public String restUrl( Context ctx,  String cont, String query )
    {
        return REST_URL+cont+"?"+sessid_index+"="+ session.singleton( ctx ).getSessionId( ctx )+"&"+app_key_index+"="+rest_ver+"&"+query+"&"+out_format_index+"=json";
    }

//    public String restUrlNew( Context ctx,  String cont, String query )
//    {
//        return REST_URL_NEW+cont+"?"+sessid_index+"="+session.singleton( ctx ).getSessionId( ctx )+"&"+app_key_index+"="+rest_ver+"&"+query+"&"+out_format_index+"=json";
//    }

    /**
     * returns user image uploadId key part
     */
    public static String u_imageUploadIdKey()
    {
        return "PIU_";
    }


    /**
     * de reference all references to object and variable it will help prevent memory leak/massive memory usage
     */
    public void do_cleanup()
    {
        config = null;
    }

}
