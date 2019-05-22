package com.ashwani.freakinflickr;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);

        Intent intent = getIntent();

        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        Resources resources = getResources();
        String text_title = resources.getString(R.string.photo_text_title, photo.getTitle());
        String text_tags = resources.getString(R.string.photo_text_tags, photo.getTags());

        if (photo != null) {
            TextView photoTitle = findViewById(R.id.photo_title);
            photoTitle.setText(text_title);

            TextView photoTags = findViewById(R.id.photo_tags);
            photoTags.setText(text_tags);

            TextView photoAuthor = findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getAuthor());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_image);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);
        }

    }

}
