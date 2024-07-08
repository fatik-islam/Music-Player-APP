package com.example.music;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class songAdapter extends ArrayAdapter {

    Context context;
    List<song> songs;
    ArrayList<File> songFiles;
    public songAdapter(@NonNull Context context, List<song> songs, ArrayList<File> songFiles) {
        super(context,R.layout.song_list_item,songs);
        this.context=context;
        this.songs=songs;
        this.songFiles=songFiles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.song_list_item,parent,false);
        TextView songName=view.findViewById(R.id.songName);

        song s= songs.get(position);
        songName.setText(s.getSongName());




        return view;
    }

}