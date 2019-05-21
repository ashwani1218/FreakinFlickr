package com.ashwani.freakinflickr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetFlickrJsonData.OnDataAvailable {
    private static final String TAG = "MainActivity";
    private FlickrRecylerViewAdapter flickrRecylerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylcer_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        flickrRecylerViewAdapter = new FlickrRecylerViewAdapter(this, new ArrayList<Photo>());
        recyclerView.setAdapter(flickrRecylerViewAdapter);

//        GetRawData getRawData=new GetRawData(this);
//        getRawData.execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=android&tagmode=any&format=json&nojsoncallback=1");


        Log.d(TAG, "onCreate: Ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume Starts");
        super.onResume();
        GetFlickrJsonData getFlickrJsonData=new GetFlickrJsonData(this,
                "https://api.flickr.com/services/feeds/photos_public.gne","en-us",
                true);
//        getFlickrJsonData.executeOnSameThread("android, nougat");
        getFlickrJsonData.execute("android,nougat");
        Log.d(TAG, "onResume ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected: returned");

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status){
        Log.d(TAG, "onDataAvailable: Starts");
        if (status==DownloadStatus.OK){
            flickrRecylerViewAdapter.loadNewData(data);
            Log.d(TAG, "onDataAvailable: data is "+data);
        }else{
            Log.e(TAG, "onDataAvailable failed with status "+status);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }
}
