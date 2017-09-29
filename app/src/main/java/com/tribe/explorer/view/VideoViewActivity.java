package com.tribe.explorer.view;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tribe.explorer.R;
import com.tribe.explorer.model.Utils;

import java.io.File;

public class VideoViewActivity extends AppCompatActivity {
    VideoView videoview;

    // Insert your Video URL
    String VideoURL;
    Toolbar toolbar;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dialog = Utils.showDialog(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initViews();
    }

    private void initViews() {
        dialog.show();
        VideoURL = getIntent().getStringExtra("videoUrl");
        videoview = (VideoView) findViewById(R.id.VideoView);
        try {
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoview);
            Uri video = Uri.fromFile(new File(VideoURL));
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error ", e.getMessage());
            dialog.dismiss();
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                dialog.dismiss();
                videoview.start();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}