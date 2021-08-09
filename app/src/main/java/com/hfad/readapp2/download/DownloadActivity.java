package com.hfad.readapp2.download;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    Manga manga;
    String url_chap;
    Button button;
    ArrayList<Chap> arraychapT;

    RecyclerView recyclerView1;
    ShowDownloadAdapter showDownloadAdapter;

    ArrayList<String> listtruyen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        if (!verifyPermissions()) {
            return;
        }
        init();
        anhXa();
        configRecyclerView();
        setUP();

        new LoadDownTask().execute(url_chap);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new AsynDownload(DownloadActivity.this).execute(arraychapT);
                addTruyen();


            }
        });
    }

    private void addTruyen() {

        if(arraychapT == null) {
            Log.d("loi","zzzzzzzzzzz");
            return;
        }
        Truyen truyen = new Truyen(manga.getTentruyen(), manga.getLinkanh(), arraychapT);
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
        manga = (Manga) b.getSerializable("truyen2");
        url_chap = manga.getLinktruyen();


        arraychapT = new ArrayList<>();
        showDownloadAdapter = new ShowDownloadAdapter(arraychapT,this);
    }
    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView1.setHasFixedSize(true);
//        recyclerView1.setItemViewCacheSize(20);
//
//        recyclerView1.setDrawingCacheEnabled(true);
//        recyclerView1.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView1.setLayoutManager(linearLayoutManager);
    }

    private void anhXa(){
        button = findViewById(R.id.startDownload);
        recyclerView1= findViewById(R.id.recdown);

    }
    private void setUP(){
        recyclerView1.setAdapter(showDownloadAdapter);

    }

    private class LoadDownTask extends AsyncTask<String, Void, ArrayList<Chap>> {

        private static final String TAG = "DownloadTask1";

        @Override
        protected ArrayList<Chap> doInBackground(String... strings) {
            Document document = null;

            try {
                document = (Document) Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Elements sub = document.getElementsByClass("list-chapters at-series");
                    Elements links = sub.select("a[href]");
                    for (Element element : links) {
                        Chap chapter = new Chap();
                        Element linkchap = element.select("a[href]").first();
                        Element tenchap = element.select("a[href]").first();
                        Element date = element.getElementsByClass("chapter-time").first();
                        //Parse to model

                        if (linkchap != null) {
                             String title = linkchap.attr("href");
                             listtruyen = getlinkanh(title);
                             chapter.setListimage(listtruyen);
                             //Log.d("zzzz",chapter.getListimage().get(2) +" "+ title);
                             chapter.setLinkchap(title);

                            //Log.d("hi",title);
                        }
                        if (tenchap != null) {
                            String ten = tenchap.attr("title");
                            chapter.setTenChap(ten);
                            //Log.d("hi",ten);
                        }
                        if (date != null) {
                            String day = date.text();
                            chapter.setNgayDang(day);
                            //Log.d("hi",day);
                        }
                        arraychapT.add(chapter);
                        //Log.d("xxx","hi");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return arraychapT;
        }
        @Override
        protected void onPostExecute(ArrayList<Chap> articles) {
            super.onPostExecute(articles);
            recyclerView1.setAdapter(showDownloadAdapter);
        }

    }
    public ArrayList<String> getlinkanh(String s){
        ArrayList<String> arr = new ArrayList<>();
        Document document = null;
        try {
            document = Jsoup.connect(s).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document != null) {
            Elements links = document.select("img");
            for (Element element : links) {
                Element linkchap = element.select("img[data-src]").first();
                //Parse to model
                if (linkchap != null) {
                    String title = linkchap.attr("data-src");
                    arr.add(title);
                }
            }
        }
        return arr;
    }


}