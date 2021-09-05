package com.hfad.readapp2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hfad.readapp2.save.Truyen;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;



public class DocTruyenActivity extends AppCompatActivity {


    Chap chapT;
    ArrayList<String> arrUrlAnh;
    RecyclerView recyclerView;
    DocTruyenAdapter docTruyenAdapter;
    int onOroffline = 0;
    String test = null;
    int Pagecurrent;
    Truyen truyen;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_truyen);


        init();
        anhXa();
        configbottomnavigation();
        configRecyclerView();
        setUP();
        //setClick();
        String c = String.valueOf(onOroffline);
        Log.d("On hay off",c);



    }

    private void configbottomnavigation() {
        //bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        recyclerView.setOnTouchListener(new TranslateAnimationUtil(this,bottomNavigationView));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_left:
                        Toast.makeText(DocTruyenActivity.this,"Left",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(DocTruyenActivity.this, DocTruyenActivity.class);
                        Bundle bb = new Bundle();
                        bb.putSerializable("chap",truyen);
                        intent1.putExtra("data1",bb);
                        if(Pagecurrent == truyen.getArraychap().size()-1){
                            Toast.makeText(DocTruyenActivity.this,"Đây là chap đầu",Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        intent1.putExtra("indexchap",Pagecurrent+1);
                        startActivity(intent1);

                        break;
                    case R.id.nav_backhome:
                        Toast.makeText(DocTruyenActivity.this,"HOme",Toast.LENGTH_SHORT).show();
                        Bundle d = new Bundle();
                        d.putSerializable("truyenoffline",truyen);
                        Intent intent2= new Intent(DocTruyenActivity.this, ChapActivity.class);
                        intent2.putExtra("dataOff",d);
                        startActivity(intent2);
                        break;
                    case R.id.nav_right:
                        Toast.makeText(DocTruyenActivity.this,"Right",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DocTruyenActivity.this, DocTruyenActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("chap",truyen);
                        intent.putExtra("data1",b);
                        if(Pagecurrent == 0){
                            Toast.makeText(DocTruyenActivity.this,"Đây là chap cuối",Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        intent.putExtra("indexchap",Pagecurrent-1);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        // attaching bottom sheet behaviour - hide / show on scroll

    }

    private void init(){
        arrUrlAnh = new ArrayList<>();
        Bundle b = getIntent().getBundleExtra("data1");
        test = getIntent().getStringExtra("nameT");
        if(test != null) {
            glidebuild(getIntent().getStringExtra("nameT"));
            //glidebuild("chap 2");
            Log.d("testDocactivity",getIntent().getStringExtra("nameT"));
        }
        Pagecurrent = getIntent().getIntExtra("indexchap",0);
        truyen = (Truyen) b.getSerializable("chap");
        chapT = truyen.getArraychap().get(Pagecurrent);

//        if(chapT.getListimage() != null){
//            arrUrlAnh = chapT.getListimage();
//            onOroffline = 1;
//        }
//        Log.d("DoctruyenAcitivty", String.valueOf(Pagecurrent));
//        Log.d("DoctruyenAcitivty",chapT.getTenChap());
        arrUrlAnh = chapT.getListimage();
        onOroffline =1;
        docTruyenAdapter = new DocTruyenAdapter(arrUrlAnh,this);
    }
    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void anhXa(){
        bottomNavigationView = findViewById(R.id.bottom_nv);
        recyclerView = findViewById(R.id.revdoc);

    }
    private void setUP(){
        recyclerView.setAdapter(docTruyenAdapter);

    }
    private void setClick(){
    }


    String sdRootPath = Environment.getExternalStorageDirectory().getPath();
    String appRootPath = null;
    private String getStorageDirectory(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                sdRootPath : appRootPath;
    }
    private void glidebuild(String nameChap){
        int diskCacheSizeBytes = 1024 * 1024 * 250; // 100 MB
        GlideBuilder glideBuilder =new GlideBuilder();
        glideBuilder.setDiskCache(
                new DiskLruCacheFactory( getStorageDirectory()+"/GlideDisk/"+nameChap, diskCacheSizeBytes )
        );
        Glide.init(DocTruyenActivity.this,glideBuilder);
    }



}