package com.hsquaretech.android_common_helpers.android_common_helpers.android_core_helpers.log;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class collectLogTask extends AsyncTask<ArrayList<String>, Void, StringBuilder>
{
	private int MAX_LOG_MESSAGE_LENGTH = 100000;
	private String[] mFilterSpecs;
	private String mFormat;
	private String mBuffer;

	private String TAG;
	private String LINE_SEPARATOR;

	@Override
	protected void onPreExecute()
	{

	}

	@Override
	protected StringBuilder doInBackground(ArrayList<String>... params )
	{
		final StringBuilder log = new StringBuilder();
		try
		{
			ArrayList<String> commandLine = new ArrayList<String>();
			commandLine.add("logcat");//$NON-NLS-1$
			commandLine.add("-d");//$NON-NLS-1$
			ArrayList<String> arguments = ((params != null) && (params.length > 0)) ? params[0] : null;
			if ( null != arguments )
			{
				commandLine.addAll(arguments);
			}

			Process process = Runtime.getRuntime().exec(commandLine.toArray(new String[0]));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ( (line = bufferedReader.readLine()) != null )
			{
				log.append(line);
				log.append(LINE_SEPARATOR);
			}
		}
		catch ( IOException e )
		{
			//Log.e(TAG, "CollectLogTask.doInBackground failed", e);//$NON-NLS-1$
		}

		return log;
	}

	@Override
	protected void onPostExecute( final StringBuilder log )
	{
		if ( null != log )
		{
			// truncate if necessary
			int keepOffset = Math.max(log.length() - MAX_LOG_MESSAGE_LENGTH, 0);
			if ( keepOffset > 0 )
			{
				log.delete(0, keepOffset);
			}

		}
	}
}