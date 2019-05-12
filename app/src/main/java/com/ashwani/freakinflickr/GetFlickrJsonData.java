package com.ashwani.freakinflickr;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetFlickrJsonData";
    private List<Photo> photoList = null;
    private String baseURL;
    private String language;
    private boolean matchAll;
    private boolean runningOnSameThread = false;

    private final OnDataAvailable callBack;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJsonData(OnDataAvailable callBack, String baseURL, String language, boolean matchAll) {
        this.baseURL = baseURL;
        this.language = language;
        this.matchAll = matchAll;
        this.callBack = callBack;
    }

    void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread starts");
        String destinationUri = createUri(searchCriteria, language, matchAll);
        runningOnSameThread = true;

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread ends");
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute Starts");

        if (callBack != null) {
            callBack.onDataAvailable(photoList, DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute Ends");

    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground Starts");
        String destinationUri = createUri(params[0], language, matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.ruinInSameThread(destinationUri);
        Log.d(TAG, "doInBackground ends");
        return photoList;
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll) {
        Log.d(TAG, "createUri Starts");

//        Uri uri=Uri.parse(baseURL).buildUpon()
//                .appendQueryParameter("tags",searchCriteria)
//                .appendQueryParameter("tagmode",matchAll?"ALL":"ANY")
//                .appendQueryParameter("lang",lang)
//                .appendQueryParameter("format","json")
//                .appendQueryParameter("nojsoncallback","1").build();

        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build()
                .toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: Starts status =" + status);

        if (status == DownloadStatus.OK) {
            photoList = new ArrayList<>();

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);
                    photoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete " + photoObject.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing jason data " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        if (runningOnSameThread && callBack != null) {
            callBack.onDataAvailable(photoList, status);
        }

        Log.d(TAG, "onDownloadComplete ends");
    }
}
