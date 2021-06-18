package com.asi.yalla_egy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImageViewForDoc extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_for_doc);
        ImageView ivhosder= (ImageView) findViewById(R.id.ivHolder);
        Glide.with(ImageViewForDoc.this).load(getIntent().getStringExtra("url"))
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.loooooosogooo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivhosder);
    }
}
