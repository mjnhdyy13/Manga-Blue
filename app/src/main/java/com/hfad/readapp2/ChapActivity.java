package com.hfad.readapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hfad.readapp2.download.DownloadActivity;
import com.hfad.readapp2.searchview.Suggestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;



public class ChapActivity extends AppCompatActivity {

    TextView txtTenTruyen;
    ImageView imgAnhTruyen;
    ListView lsvDanhSachChap;
    Manga manga;
    ArrayList<Chap> arrChap;
    ChapTruyenAdapter chapTruyenAdapter;
    String url_chap;
    Context ct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chap);
        init();
        anhXa();
        setUp();
        setClick();
        new DownloadTask1().execute(url_chap);


    }

    private void init() {

//        Bundle a = getIntent().getBundleExtra("data1");
//        suggestion = (Suggestion) a.getSerializable("truyen1");
//        url_chap = suggestion.getMlink();

        Bundle b = getIntent().getBundleExtra("data");
        manga = (Manga) b.getSerializable("truyen");
        url_chap = manga.getLinktruyen();

        arrChap = new ArrayList<>();

        chapTruyenAdapter = new ChapTruyenAdapter(this,0,arrChap);
    }
    public void Download(View view){
        Bundle b = new Bundle();
        b.putSerializable("truyen2",manga);
        Intent intent= new Intent(ChapActivity.this,DownloadActivity.class);
        intent.putExtra("data2",b);
        startActivity(intent);

        startActivity(intent);
    }

    private void anhXa() {
        imgAnhTruyen = findViewById(R.id.imgAnhTruyen);
        txtTenTruyen = findViewById(R.id.txvTenTruyen);
        lsvDanhSachChap = findViewById(R.id.lsvDanhSChap);

    }

    private void setUp() {
        txtTenTruyen.setText(manga.getTentruyen());
        Glide.with(this).load(manga.getLinkanh()).into(imgAnhTruyen);

        lsvDanhSachChap.setAdapter(chapTruyenAdapter);
    }

    private void setClick() {
        lsvDanhSachChap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(ChapActivity.this,DocTruyenActivity.class));
                Chap chapterT = arrChap.get(position);
                Log.d("xxxxxx",chapterT.getLinkchap());
                Bundle b = new Bundle();
                b.putSerializable("chap",chapterT);
                Intent intent= new Intent(ChapActivity.this,DocTruyenActivity.class);
                intent.putExtra("data1",b);
                startActivity(intent);
            }
        });
    }
    private class DownloadTask1 extends AsyncTask<String, Void, ArrayList<Chap>> {

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
                        arrChap.add(chapter);
                        //Log.d("xxx","hi");
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return arrChap;
        }

        @Override
        protected void onPostExecute(ArrayList<Chap> articles) {
            super.onPostExecute(articles);
            //Setup data recyclerView
            lsvDanhSachChap.setAdapter(chapTruyenAdapter);
        }

    }
}