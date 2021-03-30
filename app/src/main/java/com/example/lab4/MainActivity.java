package com.example.lab4;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MAIN__";
    private MediaPlayer mediaPlayer;
    private String url = "http://stream.whus.org:8000/whusfm";
    private Button internetRadioButton;
    private boolean radioOn;
    private boolean radioWasOnBefore;
    private Spinner radioSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        radioOn = false;
        radioWasOnBefore = false;

        mediaPlayer = new MediaPlayer();

        internetRadioButton = findViewById(R.id.radio_button);
        // it should work but I can't fix "E/MediaPlayer: Error (-38,0)" and "E/MediaPlayer: Error (1,-19)" bug
        internetRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radioOn) { // ON so Turn OFF
                    radioOn = false;
                    internetRadioButton.setText("Turn radio ON");
                    if (mediaPlayer.isPlaying()) {
                        Log.i(TAG, "Radio is playing- turning off " );
                        radioWasOnBefore = true;
                        mediaPlayer.pause();
                    }
                } else { // OFF so Turn ON
                    radioOn = true;
                    internetRadioButton.setText("Turn radio OFF");
                    if (!mediaPlayer.isPlaying()) {
                        if (radioWasOnBefore) {
                            mediaPlayer.release();
                            mediaPlayer = new MediaPlayer();

                        }
                        radioSetup(mediaPlayer);
                        mediaPlayer.prepareAsync();

                    }
                }

            }
        });

        radioSpinner = findViewById(R.id.radio_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.radio_stations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radioSpinner.setAdapter(adapter);

        radioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    url = "http://stream.whus.org:8000/whusfm";
                }
                if(i==1){
                    url ="https://18843.live.streamtheworld.com/WQHT.mp3";
                }

                if(i==2){
                    url = "https://20603.live.streamtheworld.com/WEZNFM.mp3";
                }

                if(i==3){
                    url = "https://ice10.securenetsystems.net/WDAQ";
                }
                if (radioOn) {
                    if (mediaPlayer.isPlaying()) {
                        radioWasOnBefore = true;
                    }
                    mediaPlayer.pause();
                    if (!mediaPlayer.isPlaying()) {
                        if (radioWasOnBefore) {
                            mediaPlayer.release();
                            mediaPlayer = new MediaPlayer();
                        }
                        radioSetup(mediaPlayer);
                        mediaPlayer.prepareAsync();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void radioSetup(MediaPlayer mediaPlayer) {

        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "mediaPlayer.start()" );
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i(TAG, "onError: " + String.valueOf(what).toString());
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(TAG, "onCompletion" );
                mediaPlayer.reset();
            }
        });

        try {
            mediaPlayer.setDataSource(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpMediaPlayer() {
        Handler handler = null;

        HandlerThread handlerThread = new HandlerThread("media player") {
            @Override
            public void onLooperPrepared() {
                Log.i(TAG, "onLooperPrepared");

            }
        };

    }

}