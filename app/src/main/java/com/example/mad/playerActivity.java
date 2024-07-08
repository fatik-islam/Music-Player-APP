package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playerActivity extends AppCompatActivity {

    TextView songName;
    SeekBar bar;
    Button play_btn, next_btn, previous_btn;
    static MediaPlayer plyr;
    static int playingSong;
    Thread updateSeekbar;


    String name;
    int position;
    ArrayList<File> songFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        songName=findViewById(R.id.song_name_player);
        bar=findViewById(R.id.player_bar);
        play_btn=findViewById(R.id.play_pause_btn);
        next_btn=findViewById(R.id.next_btn);
        previous_btn=findViewById(R.id.previous_btn);

        if(plyr != null){
            plyr.stop();
            plyr.release();
        }

        Intent intent = getIntent();
        Bundle bundel = intent.getExtras();

        songFiles = (ArrayList) bundel.getParcelableArrayList("songFiles");
        name = intent.getStringExtra("name");
        position = bundel.getInt("position",0);
        Uri uri = Uri.parse(songFiles.get(position).toString());
        playingSong=position;

        songName.setText(name);

        plyr = MediaPlayer.create(getApplicationContext(),uri);
        plyr.start();

        updateSeekbar = new Thread(){
            @Override
            public void run() {
                int totalDuration = plyr.getDuration();
                int currentDuration = 0;

                while (currentDuration<totalDuration){
                    try {
                        sleep(100);
                        currentDuration = plyr.getCurrentPosition();
                        bar.setProgress(currentDuration);
                    }catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        bar.setMax(plyr.getDuration());
        updateSeekbar.start();

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                plyr.seekTo(seekBar.getProgress());
            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPause();
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNext();
            }
        });
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPrevious();
            }
        });



    }


    private void playNext(){
        if(playingSong<songFiles.size()){
            if(playingSong==songFiles.size()-1){
                playingSong=0;
            }
            else{
                playingSong++;
            }


            if(plyr.isPlaying()){
                plyr.stop();
                plyr.release();
            }
            Uri uri = Uri.parse(songFiles.get(playingSong).toString());
            plyr = MediaPlayer.create(getApplicationContext(),uri);
            plyr.start();
            play_btn.setBackgroundResource(R.drawable.pause_btn);
            songName.setText(songFiles.get(playingSong).getName().replace(".mp3",""));

        }

    }

    private void playPrevious(){
        if(playingSong>=0){
            if(playingSong==0){
                playingSong=songFiles.size()-1;
            }
            else {
                playingSong--;
            }

            if(plyr.isPlaying()){
                plyr.stop();
                plyr.release();
            }
            Uri uri = Uri.parse(songFiles.get(playingSong).toString());
            plyr = MediaPlayer.create(getApplicationContext(),uri);
            plyr.start();
            play_btn.setBackgroundResource(R.drawable.pause_btn);
            songName.setText(songFiles.get(playingSong).getName().replace(".mp3",""));

        }

    }

    private void playPause(){
        if(plyr.isPlaying()){
            play_btn.setBackgroundResource(R.drawable.play_icon);
            plyr.pause();
        }
        else {
            play_btn.setBackgroundResource(R.drawable.pause_btn);
            plyr.start();
        }
    }


}