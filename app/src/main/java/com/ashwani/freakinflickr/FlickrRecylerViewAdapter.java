package com.ashwani.freakinflickr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecylerViewAdapter extends RecyclerView.Adapter<FlickrRecylerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickerRecylerViewAdapt";
    protected List<Photo> photoList;
    private Context context;

    public FlickrRecylerViewAdapter(Context context, List<Photo> photoList) {
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //called by the Loayout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: New view Requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent,
                false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int i) {
        //called by the layout manager when it wants new data in the existing row

        Photo photoItem = photoList.get(i);
        Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + "-->" + i);

        Picasso.with(context).load(photoItem.getImages())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
        holder.title.setText(photoItem.getTitle());

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((photoList != null) && (photoList.size() != 0) ? photoList.size() : 0);
    }

    void loadNewData(List<Photo> newPhotos) {
        photoList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((photoList != null) && (photoList.size() != 0) ? photoList.get(position) : null);
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: Starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
