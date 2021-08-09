package com.hfad.readapp2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_truyen);

        //glidebuild("chap 1");
        init();
        anhXa();
        configRecyclerView();
        setUP();
        //setClick();
        new DownloadTask2().execute(chapT.getLinkchap());




    }
    private void init(){
        arrUrlAnh = new ArrayList<>();
        Bundle b = getIntent().getBundleExtra("data1");
        chapT = (Chap) b.getSerializable("chap");
//        arrUrlAnh.add("https://cdn4.lhmanga.com/Store/Manga/2290e768-0de6-432d-842a-1ad5cf0e3039_610a0fb04c112.jpg");
//        arrUrlAnh.add("https://cdn4.lhmanga.com/Store/Manga/8eeabefc-eb8d-4f34-a8b2-9436501ae149_610a0fb0572d8.jpg");
//        arrUrlAnh.add("https://cdn4.lhmanga.com/Store/Manga/6e4b5b91-296e-4463-a096-c04e9766a1c3_610a0fb0632d3.jpg");
//        //arrUrlAnh.add("http://cdn6.truyentranh8.net/hdd2/u2/cacom/32635/000-680802/005.jpg");
//        //arrUrlAnh.add("http://cdn6.truyentranh8.net/hdd2/u2/cacom/32635/000-680802/006.jpg");

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
        recyclerView = findViewById(R.id.revdoc);

    }
    private void setUP(){
        recyclerView.setAdapter(docTruyenAdapter);

    }
    private void setClick(){
    }
//    public void right(View view){
//        docTheoTrang(1);
//
//    }
//    public void left(View view){
//        docTheoTrang(-1);
//
//    }
//    public void docTheoTrang(int i){
//        soTrangdangDoc= soTrangdangDoc+i;
//        if(soTrangdangDoc==0){
//            soTrangdangDoc=1;
//        }
//        if(soTrangdangDoc>soTrang){
//            soTrangdangDoc=soTrang;
//        }
//        txvSotrang.setText(soTrangdangDoc+" / "+soTrang);
//        Glide.with(this).load(arrUrlAnh.get(soTrangdangDoc-1)).into(imgAnh);
//    }
    private class DownloadTask2 extends AsyncTask<String, Void, ArrayList<String>> {

        private static final String TAG = "DownloadTask2";

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            Document document = null;
            //ArrayList<Manga> listArticle = new ArrayList<>();
            try {
                document = (Document) Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Elements links = document.select("img");
                    for (Element element : links) {
                        Element linkchap = element.select("img[data-src]").first();
                        //Parse to model
                        if (linkchap != null) {
                            String title = linkchap.attr("data-src");
                            arrUrlAnh.add(title);
                            Log.d("hianh", title);
                        }

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return arrUrlAnh;
        }

        @Override
        protected void onPostExecute(ArrayList<String> articles) {
            super.onPostExecute(articles);
            //Setup data recyclerView
            recyclerView.setAdapter(docTruyenAdapter);
        }

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