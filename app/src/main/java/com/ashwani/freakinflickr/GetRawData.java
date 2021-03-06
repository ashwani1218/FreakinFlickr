package com.ashwani.freakinflickr;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

class GetRawData extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetRawData";

    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallBack;

    interface OnDownloadComplete{
        void onDownloadComplete(String data,DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete callBack) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        mCallBack=callBack;
    }

    void ruinInSameThread(String s){
        Log.d(TAG, "ruinInSameThread Starts");

//        onPostExecute(doInBackground(s));

        if(mCallBack != null){
            mCallBack.onDownloadComplete(doInBackground(s),mDownloadStatus);
        }

        Log.d(TAG, "ruinInSameThread ends");
    }
    @Override
    protected void onPostExecute(String s) {
//        Log.d(TAG, "onPostExecute: Parameter = " + s);
        if(mCallBack !=null){
            mCallBack.onDownloadComplete(s,mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: Ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The Response Code was " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

//            String line;
//            while (null != (line=reader.readLine())){
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();


        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception Reading Data " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Needs Permissions? " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG, "doInBackground: ERROR CLOSING STREAM " + e.getMessage());
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

}
