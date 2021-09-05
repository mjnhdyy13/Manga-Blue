package com.hfad.readapp2.download;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.hfad.readapp2.Chap;
import com.hfad.readapp2.ChapActivity;
import com.hfad.readapp2.DocTruyenAdapter;
import com.hfad.readapp2.MainActivity;
import com.hfad.readapp2.Manga;
import com.hfad.readapp2.R;
import com.hfad.readapp2.save.Truyen;
import com.hfad.readapp2.save.TruyenDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity  {

    Button button,btsuccess;
    ArrayList<Chap> arraychapT;
    Truyen truyen;
    RecyclerView recyclerView1;

    ImageView imageView,backbutton;
    TextView textView;
    ShowDownloadAdapter showDownloadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        if (!verifyPermissions()) {
            return;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("noty","noty", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        init();
        anhXa();
        configRecyclerView();
        setUP();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsynDownload(DownloadActivity.this,imageView,textView,btsuccess).execute(truyen);
                addTruyen(truyen);
            }
        });
        btsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                btsuccess.setVisibility(View.GONE);
            }
        });
    }

    private void addTruyen( Truyen truyen) {

        TruyenDatabase.getInstance(this).truyenDAO().insertTruyen(truyen);
        Toast.makeText(this,"Add successes",Toast.LENGTH_SHORT).show();
    }

    public Boolean verifyPermissions() {
        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(DownloadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(DownloadActivity.this, STORAGE_PERMISSIONS, 1);
            return false;
        }
        return true;
    }


    private void init(){
        Bundle b = getIntent().getBundleExtra("data2");
        truyen = (Truyen) b.getSerializable("truyen2");


        arraychapT = truyen.getArraychap();
        showDownloadAdapter = new ShowDownloadAdapter(arraychapT,this);
    }
    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(linearLayoutManager);
    }

    private void anhXa(){
        backbutton = findViewById(R.id.homedownload);
        imageView = findViewById(R.id.img_download);
        textView = findViewById(R.id.text_DL);
        button = findViewById(R.id.startDownload);
        btsuccess = findViewById(R.id.bt_success);
        recyclerView1= findViewById(R.id.recdown);

    }
    private void setUP(){
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.d("back ","chap");
            }
        });
        recyclerView1.setAdapter(showDownloadAdapter);

    }

}