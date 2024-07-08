package com.example.MAD;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    List<song> songs;
    ArrayList<File> songFiles;
    ListView songList;
    songAdapter adptr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songList = findViewById(R.id.listViewSongs);

        if(!checkPermission()){
            getPermission();
        }
        if(checkPermission()) {
            listSongs();

        }
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Pressed", Toast.LENGTH_SHORT).show();
                playThisSong(i);
            }
        });

    }

    private void playThisSong(int i){
        String name = songs.get(i).getSongName();
        startActivity(new Intent(MainActivity.this,playerActivity.class)
                .putExtra("songFiles",songFiles)
                .putExtra("name",name)
                .putExtra("position",i)

        );
    }

    private void listSongs(){
        songs = getSongs();
        adptr = new songAdapter(MainActivity.this,songs,songFiles);
        songList.setAdapter(adptr);
    }

    private void getPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[] {getApplicationContext().getPackageName()})));
                startActivityForResult(intent,2);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,2);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {READ_EXTERNAL_STORAGE},1);
        }
    }


    private boolean checkPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else {
            int read = ContextCompat.checkSelfPermission(MainActivity.this,READ_EXTERNAL_STORAGE);
            //we can also chevck for write permission here like this and add && write == PackageManager.PERMISSION_GRANTED
            return read == PackageManager.PERMISSION_GRANTED;
        }

    }


    ArrayList<File> loadSongs(File file){

        ArrayList<File> songList = new ArrayList<>();

        File[] files = file.listFiles();

        for (File i:files)
        {
            if(i.isDirectory() && !i.getPath().toString().endsWith("Android"))
            {
                songList.addAll(loadSongs(i));
            }
            else{
                if(i.getName().endsWith(".mp3"))
                {
                    songList.add(i);
                }
            }
        }

        return songList;
    }



    List<song> getSongs() {

        File rootFile = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        songs = new ArrayList<>();
        songFiles = loadSongs(rootFile);

        for (int i = 0; i < songFiles.size(); i++) {
            com.example.music.song s = new com.example.music.song();
            s.setSongName(songFiles.get(i).getName().replace(".mp3", ""));
            s.setSongDuration(songFiles.get(i).getPath());
            songs.add(s);

        }

        return songs;
    }

}