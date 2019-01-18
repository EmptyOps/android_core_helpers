package com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.helpers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.config.config;
import com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.log.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @category name space imi_ => Interaction helper
 */
public class file_helper
{
        public static file_helper file_helper = null;
	
	public file_helper()
	{
		// TODO Auto-generated constructor stub
	}
	
	public static file_helper singleton()
	{
		if( file_helper == null )
		{
			file_helper = new file_helper();
		}
		return file_helper;
	}

	/**
	 * 
	 * @return
	 */
//	public boolean isFileExists( String filePath )
//	{
//		File file = new File(filePath);
//		return file.exists();
//	}

	public boolean isFileExistsFromUrl(Activity activityObj, String url )
	{
		String path = fileSavePath(activityObj,url,false);
		File file = new File(path);
		return file.exists();
	}

	public static String getDocumentDirConstant()
	{
		if (Build.VERSION.SDK_INT >= 19)
		{
			return Environment.DIRECTORY_DOCUMENTS;
		}
		else
		{
			return "Documents";
		}
	}

	public static String getPictureDirConstant()
	{
		if (Build.VERSION.SDK_INT >= 19)
		{
			return Environment.DIRECTORY_PICTURES;
		}
		else
		{
			return "Pictures";
		}
	}

//	public String createCustomImageFilePath (Activity activityObj,String extension)
//	{
//		String docDir = getExternalOrAppPublicDir(activityObj, getDocumentDirConstant());
//		return docDir+"/"+ "upl_img."+extension;
//	}

	public String fileSavePath(Context activityObj, String url, boolean isExternalStoragerequired)
	{
		String docDir;

		if(isExternalStoragerequired)
		{
			docDir = getExternalStoragePublicDir(activityObj,getDocumentDirConstant());
		}
		else
		{
			docDir = getExternalOrAppPublicDir(activityObj,getDocumentDirConstant());
		}

		return docDir+"/"+fileName(url);
	}

	public String fileName(String url)
	{
		if( url == null ) 	return "";

		Uri url1 = Uri.parse(url);

		return url1.getLastPathSegment();
	}

	@SuppressLint("NewApi")
	public static boolean isExternalStorageRemovable()
	{
		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD )
		{
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	@SuppressLint("NewApi")
	public static File getExternalCacheDir(Context context )
	{
		if ( hasExternalCacheDir() )
		{
			return context.getExternalCacheDir();
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}

	public static boolean hasExternalCacheDir()
	{
		return imui.singleton().versionSDK_INT() >= Build.VERSION_CODES.FROYO;
	}
	
	/**
	 * @deprecated
	 * returns application private files directory
	 */
	public static String getApplicationCacheDir(Activity activityObj)
	{
		return activityObj.getApplicationContext().getCacheDir().toString(); 
	}

	/**
	 * check if external storage is available or not
	 */
	public static boolean isExternalStorageMounted() 
	{
	    if (Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState() ))
	    {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * returns application private files directory
	 */
	public static String getApplicationPrivateDir(Activity activityObj)
	{
		return activityObj.getApplicationContext().getFilesDir().toString();
	}

	/**
	 * 
	 * @param name should be like below however it is not constraint 
	 * android.os.Environment.DIRECTORY_MUSIC, 
	 * android.os.Environment.DIRECTORY_PODCASTS, 
	 * android.os.Environment.DIRECTORY_RINGTONES, 
	 * android.os.Environment.DIRECTORY_ALARMS, 
	 * android.os.Environment.DIRECTORY_NOTIFICATIONS, 
	 * android.os.Environment.DIRECTORY_PICTURES, or 
	 * android.os.Environment.DIRECTORY_MOVIES.
	 * 
	 * @param mode modes supported as are below
	 * android.content.Context.MODE_PRIVATE
	 * android.content.Context.MODE_WORLD_READABLE
	 * android.content.Context.MODE_WORLD_WRITEABLE
	 * @return
	 */
	public static String getApplicationDir(Context activityObj, String name, int mode)
	{
		try
		{
			return activityObj.getApplicationContext().getDir(name, mode).toString();
		}
		catch (SecurityException e)
		{
			log.singleton().logError(activityObj, 1, "Security exception ", e);
		}

		return "";
	}

	
	/**
	 * will check if it is API level above 7 then only it will return value  
	 * @param type 
	 * android.os.Environment.DIRECTORY_MUSIC, 
	 * android.os.Environment.DIRECTORY_PODCASTS, 
	 * android.os.Environment.DIRECTORY_RINGTONES, 
	 * android.os.Environment.DIRECTORY_ALARMS, 
	 * android.os.Environment.DIRECTORY_NOTIFICATIONS, 
	 * android.os.Environment.DIRECTORY_PICTURES, or 
	 * android.os.Environment.DIRECTORY_MOVIES.
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static String getExternalStoragePrivateDir(Activity activityObj, String type)
	{
		if( imui.singleton().isFroyo() && isExternalStorageMounted() )
		{
			/**
			 * similar to Environment.getExternalStorageDirectory()
			 */
			String dirType = "";
			if( type.equals(config.singleton().PICT_DIR ) )
			{
				dirType = getPictureDirConstant();
			}
			
			return activityObj.getApplicationContext().getExternalFilesDir(dirType).toString();
		}
		else
		{
			return ""; 
		}
	} 

	/**
	 * will check if it is API level above 7 then only it will return value  
	 * @param type 
	 * android.os.Environment.DIRECTORY_MUSIC, 
	 * android.os.Environment.DIRECTORY_PODCASTS, 
	 * android.os.Environment.DIRECTORY_RINGTONES, 
	 * android.os.Environment.DIRECTORY_ALARMS, 
	 * android.os.Environment.DIRECTORY_NOTIFICATIONS, 
	 * android.os.Environment.DIRECTORY_PICTURES, or 
	 * android.os.Environment.DIRECTORY_MOVIES.
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static String getExternalStoragePublicDir(Context activityObj, String type)
	{
		if( imui.singleton().isFroyo() && isExternalStorageMounted() )
		{
			/**
			 * similar to Environment.getExternalStorageDirectory()
			 */
			String dirType = "";
			if( type.equals(config.singleton().PICT_DIR ) )
			{
				dirType = getPictureDirConstant();
			}

			return Environment.getExternalStoragePublicDirectory(dirType).toString();
		}
		else
		{
			return ""; 
		}
	} 

	/**
	 * returns private external or application directory
	 * @param type 
	 * android.os.Environment.DIRECTORY_MUSIC, 
	 * android.os.Environment.DIRECTORY_PODCASTS, 
	 * android.os.Environment.DIRECTORY_RINGTONES, 
	 * android.os.Environment.DIRECTORY_ALARMS, 
	 * android.os.Environment.DIRECTORY_NOTIFICATIONS, 
	 * android.os.Environment.DIRECTORY_PICTURES, or 
	 * android.os.Environment.DIRECTORY_MOVIES.
	 */
	public static String getExternalOrAppPrivateDir(Activity activityObj, String type)
	{
		String dir = getExternalStoragePrivateDir(activityObj, type);
		if( imlb.singleton().isStrEmptyStrict(dir) )
		{
			dir = getApplicationDir(activityObj, type, android.content.Context.MODE_PRIVATE);
		}

		return dir;
	}

	/**
	 * returns publicly readable external or application directory
	 * @param type
	 * android.os.Environment.DIRECTORY_MUSIC,
	 * android.os.Environment.DIRECTORY_PODCASTS,
	 * android.os.Environment.DIRECTORY_RINGTONES,
	 * android.os.Environment.DIRECTORY_ALARMS,
	 * android.os.Environment.DIRECTORY_NOTIFICATIONS,
	 * android.os.Environment.DIRECTORY_PICTURES, or
	 * android.os.Environment.DIRECTORY_MOVIES.
	 *
	 */
	public static String getExternalOrAppPublicDir(Context activityObj, String type)
	{

//		 * On 16-03-2015 priority is given to application public directory instead of SDCard, now if application directory had
//		 * limited space then external directory is used.

		String dir;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
		{
			dir = getApplicationDir(activityObj, type, Context.MODE_PRIVATE);
		}
		else
		{
			dir = getApplicationDir(activityObj, type, Context.MODE_WORLD_READABLE);
		}

		if( imlb.isStrEmptyStrict(dir) )
		{
			dir = getExternalStoragePublicDir(activityObj, type);
		}

		return dir;
	}


	/**
	 * saves bitmap as a file on android app directory
	 */
	public static File persistImage(Activity activityObj, Bitmap bitmap, String name)
	{
		File imageFile = new File( activityObj.getApplicationContext().getFilesDir(), name );

		OutputStream os;
		try
		{
			os = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
			os.close();
		}
		catch (Exception e)
		{
			imui.singleton().errorMsg( (Context) activityObj, 1, "Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
		}

		return imageFile;
	}

	/**
	 * function will copy file into directory
	 */
	public static void copyFileToDirectory(Context activityObj, File src, File dst)
	{
         try
         {
             copyFile(src, dst);
         }
         catch (Exception e)
         {
             imui.singleton().errorMsg( activityObj, 1, "Stack: " + e.getStackTrace() + " Msg: " + e.getMessage() );
         }
	}

	/**
	 * function will copy file into another file
	 */
	public static void copyFile(File src, File dst) throws IOException
	{
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0)
	    {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}

	public static String fileExt(String fpath)
	{
		File file = new File(fpath);
		return file.getPath().substring(file.getPath().lastIndexOf("."));
	}

	/**
	 * function will try to delete file from private storage directories which could be either internal or external(SD card)
	 */
	public static boolean deleteFilePrivate(Activity activityObj, File file)
	{
		return file.delete();
	}

	/**
	 * function will try to delete file from public storage directories which could be either internal or external(SD card)
	 */
	public static boolean deleteFilePublic( File dir)
	{
//		return file.delete();
		if (dir != null && dir.isDirectory())
		{
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++)
			{
				boolean success = deleteFilePublic(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}


	public static void deleteFilePublicnew(Context activityObj, String file)
	{
		File srcFile = new File(file);
		if(!srcFile.delete())
		{
			srcFile.delete();
		}
	}

	public static String getAppDir(Context activityObj)
	{
		PackageManager m = activityObj.getPackageManager();
		String s = activityObj.getPackageName();
		PackageInfo p = null;
		try
		{
			p = m.getPackageInfo(s, 0);
			s = p.applicationInfo.dataDir;
		}
		catch (PackageManager.NameNotFoundException e)
		{

		}
		return s;
	}
	public static void deleteFileAppDir(Context activityObj)
	{
		String dir;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
		{
			dir = getApplicationDir(activityObj, getDocumentDirConstant(), Context.MODE_PRIVATE);
		}
		else
		{
			dir = getApplicationDir(activityObj, getDocumentDirConstant(), android.content.Context.MODE_WORLD_READABLE);
		}

		if( imlb.isStrEmptyStrict(dir) )
		{
			dir = getExternalStoragePublicDir(activityObj, getDocumentDirConstant());
		}

		File dir1 = new File(dir);
		if (dir1 .isDirectory())
		{
			String[] children = dir1 .list();
			for (int i = 0; i < children.length; i++)
			{
				new File(dir1 , children[i]).delete();
			}
		}
	}

	public String fileMimetypeByExtension(String ext)
	{
		ext = ext.toLowerCase();
		if( ext.equals("jpg") || ext.equals("jpeg") )
		{
			return "image/jpeg";
		}
		else if( ext.equals("png") )
		{
			return "image/png";
		}
		else if( ext.equals("gif") )
		{
			return "image/gif";
		}
		else if( ext.equals("flv") )// Flash
		{
			return "video/x-flv";
		}
		else if( ext.equals("mp4") )// MPEG-4
		{
			return "video/mp4";
		}
		else if( ext.equals("mov")  || ext.equals("moov") )// QuickTime
		{
			return "video/quicktime";
		}
		else if( ext.equals("movie") )// Movie
		{
			return "video/x-sgi-movie";
		}
		else if( ext.equals("mng") )
		{
			return "video/mng";
		}
		else if( ext.equals("x-mng") )
		{
			return "video/x-mng";
		}
		else if( ext.equals("avi") )// A/V Interleave
		{
			return "video/x-msvideo";
		}
		else if( ext.equals("wmv") )// Windows Media
		{
			return "video/x-ms-wmv";
		}
		else if( ext.equals("ogv") )
		{
			return "video/ogg";
		}
		else if( ext.equals("ogg") )
		{
			return "audio/ogg";
		}
		else if( ext.equals("mpeg") || ext.equals("mpga"))
		{
			return "audio/mpeg";
		}
		else if( ext.equals("3gp") )// 3GP Mobile
		{
			return "video/3gpp";
		}
		else if( ext.equals("m3u8") )// iPhone Index
		{
			return "application/x-mpegURL";
		}
		else if( ext.equals("ts") )// iPhone Segment
		{
			return "video/MP2T";
		}
		else if( ext.equals("mjpg") )
		{
			return "video/x-motion-jpeg";
		}


		/**
		 * Other Extension/MIME type should be:
		 .avi	application/x-troff-msvideo
		 .avi	video/avi
		 .avi	video/msvideo
		 .avi	video/x-msvideo

		 .jpe	image/jpeg
		 .jpe	image/pjpeg
		 .jpeg	image/jpeg
		 .jpeg	image/pjpeg
		 .jpg	image/jpeg
		 .jpg	image/pjpeg
		 */


		return "";
	}

	/**
	 * de reference all references to object and variable it will help prevent memory leak/massive memory usage 
	 */
	public void do_cleanup()
	{
		file_helper = null;
	}

	public static void moveFile(Context activityObj, File src, File dst, String filepath) throws IOException
	{
		copyFileToDirectory(activityObj,src,dst);
			//delete file
		deleteFilePublic(src);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			final Uri contentUri = Uri.fromFile(src);
			scanIntent.setData(contentUri);
			activityObj.sendBroadcast(scanIntent);
		}
		else
		{
			final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
			activityObj.sendBroadcast(intent);
		}
	}
}