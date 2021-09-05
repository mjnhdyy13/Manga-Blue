package com.hfad.readapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.hfad.readapp2.download.DownloadActivity;
import com.hfad.readapp2.save.Truyen;
import com.hfad.readapp2.searchview.Suggestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



public class ChapActivity extends AppCompatActivity implements ItemClickChap {

    //ProgressBar progressBar;
    TextView txtTenTruyen;
    ImageView imgAnhTruyen,imghome;
    RecyclerView recyclerView;
    Manga manga;
    int limit =20;
    int current = 0;
    int maximum = 1000;

    String sdRootPath = Environment.getExternalStorageDirectory().getPath();
    String appRootPath = null;

    BeautifulProgressDialog progressDialog;
    Truyen truyen;
    ArrayList<String> arrImage;
    ArrayList<Chap> arrChap;
    ChapTruyenAdapter chapTruyenAdapter;
    String url_chap;
    Context ct;
    int onOroff = 0;
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chap);
        init();
        anhXa();
        configRecyclerView();
        setUp();
        if(onOroff ==0) {
            progressDialog.show();
            new DownloadTask1().execute(url_chap);

            initScrollListener();
        }


        //initScrollListener();

    }
    private void initScrollListener() {

        Log.d("kiem tra max start 1",String.valueOf(maximum));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == arrChap.size() - 1) {
                        //bottom of list!
                        Log.d("zzzzzzzzz","ok");
                        if(maximum!=current) {
                            Log.d("hi maximum",String.valueOf(maximum)+String.valueOf(current));
                            loadMore();
                        }
                        isLoading = true;
                    }
                }
            }
        });


    }


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
        Glide.init(ChapActivity.this,glideBuilder);
    }
    private void loadMore() {
//        arrChap.add(null);
//        chapTruyenAdapter.notifyItemInserted(arrChap.size() - 1);
        progressDialog.show();
        Log.d("load",String.valueOf(arrChap.size()));


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(maximum==current){
                    Log.d("kiem tra max start",String.valueOf(maximum));
                    return;
                }
//                arrChap.remove(arrChap.size() - 1);
                Log.d("load1",String.valueOf(arrChap.size()));
                int scrollPosition = arrChap.size();
//                chapTruyenAdapter.notifyItemRemoved(scrollPosition);
//                chapTruyenAdapter.notifyDataSetChanged();
//                chapTruyenAdapter.ProgressOff();
//                chapTruyenAdapter.removeLastItem();
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 20;

//                while (currentSize - 1 < nextLimit) {
//                    //arrChap.add(new Chap("chap1","1/2/2021","lllllllllllllllllllllll"));
//                    limit = nextLimit;
//                    new DownloadTask1().execute(url_chap);
//                    currentSize++;
//                }

                limit = nextLimit;

                //current =current+1;
                new DownloadTask1().execute(url_chap);
                //chapTruyenAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }

    private void init() {


        Bundle b = getIntent().getBundleExtra("data");
        //manga = (Manga) b.getSerializable("truyen");
        if(b == null){
            Bundle c = getIntent().getBundleExtra("dataOff");
            truyen = (Truyen) c.getSerializable("truyenoffline");
            onOroff =1;
            arrChap = truyen.getArraychap();
        }else {
            manga = (Manga) b.getSerializable("truyen");
            url_chap = manga.getLinktruyen();
            arrChap = new ArrayList<>();
        }


        chapTruyenAdapter = new ChapTruyenAdapter(arrChap,this,this);


        progressDialog = new BeautifulProgressDialog(ChapActivity.this, BeautifulProgressDialog.withGIF, "Please Wait");
        Uri myUri = Uri.fromFile(new File("//android_asset/girl1.gif"));
        progressDialog.setGifLocation(myUri);
        progressDialog.setLayoutColor(getResources().getColor(R.color.white));


    }
    public void Download(View view){
        Bundle b = new Bundle();
        if(truyen == null){
            truyen = new Truyen(manga.getTentruyen(),manga.getLinkanh(),arrChap);
        }
        b.putSerializable("truyen2",truyen);
        Intent intent= new Intent(ChapActivity.this,DownloadActivity.class);
        intent.putExtra("data2",b);
        startActivity(intent);

    }

    private void anhXa() {
        imgAnhTruyen = findViewById(R.id.imgAnhTruyen);
        txtTenTruyen = findViewById(R.id.txvTenTruyen);
        recyclerView = findViewById(R.id.lsvDanhSChap);
        imghome= findViewById(R.id.imghome);
//        progressBar = findViewById(R.id.progressbar);

    }

    private void setUp() {
        glidebuild("box_avatar");
        imghome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ChapActivity.this,MainActivity.class);
                startActivity(intent2);
            }
        });
        if(onOroff ==1){
            txtTenTruyen.setText(truyen.getNamemanga());
            Glide.with(this).load(truyen.getLinkavatar()).into(imgAnhTruyen);
        }else {
            txtTenTruyen.setText(manga.getTentruyen());
            Glide.with(this).load(manga.getLinkanh()).into(imgAnhTruyen);
        }
        recyclerView.setAdapter(chapTruyenAdapter);

    }

    @Override
    public void onclick(Chap chap,int position) {
        Toast.makeText(this,chap.getTenChap(),Toast.LENGTH_SHORT).show();

        if(truyen==null){
            truyen = new Truyen(manga.getTentruyen(),manga.getLinkanh(),arrChap);
        }
        Bundle b = new Bundle();
        b.putSerializable("chap",truyen);
        Intent intent= new Intent(ChapActivity.this,DocTruyenActivity.class);
        intent.putExtra("data1",b);
        intent.putExtra("indexchap",position);
        if(onOroff==1){
            intent.putExtra("nameT",truyen.getNamemanga());
        }
        startActivity(intent);
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
                    int max = links.size()-1;
                    maximum = max;
                    Log.d("hi max",String.valueOf(max));
                    //for (Element element : links) {


                    for(int i=current;i<limit;i++){
                        Log.d("hi curr",String.valueOf(current));
                        Element element = links.get(i);
                        Chap chapter = new Chap();
                        Element linkchap = element.select("a[href]").first();
                        Element tenchap = element.select("a[href]").first();
                        Element date = element.getElementsByClass("chapter-time").first();
                        //Parse to model
                        if (linkchap != null) {
                            String title = linkchap.attr("href");
                            arrImage = getlinkanh(title);
                            chapter.setListimage(arrImage);
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
                        if(current==max){
                            Log.d("break",String.valueOf(current));
                            break;
                        }else {
                            current++;
                        }
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
            recyclerView.setAdapter(chapTruyenAdapter);

            progressDialog.dismiss();
        }

    }
    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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