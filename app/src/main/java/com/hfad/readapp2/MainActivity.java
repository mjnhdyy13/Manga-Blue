package com.hfad.readapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.hfad.readapp2.save.SaveFragment;
import com.hfad.readapp2.searchview.MakeSuggestion;
import com.hfad.readapp2.searchview.Suggestion;
import com.hfad.readapp2.searchview.asyn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ItemClickListener, MakeSuggestion, NavigationView.OnNavigationItemSelectedListener {

    public static final String MY_URL = "https://truyentranhlh.net/";


    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    TruyentranhAdapter adapter;
    ArrayList<Manga> truyentranhlist;
    NavigationView navigationView;

    private FloatingSearchView mSearchView;
    private asyn a = null;
    MakeSuggestion makeSuggestion = this;
    @Override
    public void getSuggestion(List<Suggestion> suggestions) {
        mSearchView.swapSuggestions(suggestions);
        mSearchView.hideProgress();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        anhXa();
        Configsearchview();
        configRecyclerView();
        setUP();


        new DownloadTask().execute(MY_URL);
        //Toast.makeText(this,"-------------",Toast.LENGTH_SHORT).show();
    }

    private void Configsearchview() {
        mSearchView.setBackgroundColor(Color.parseColor("#787878"));
        mSearchView.setViewTextColor(Color.parseColor("#e9e9e9"));
        mSearchView.setHintTextColor(Color.parseColor("#e9e9e9"));
        mSearchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
        mSearchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
        mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
        mSearchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
        mSearchView.setDividerColor(Color.parseColor("#BEBEBE"));
        mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));

//        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
//            @Override
//            public void onHomeClicked() {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();
                    if (a!= null){
                        a.cancel(true);
                    }
                    a = (asyn) new asyn(makeSuggestion).execute("https://truyentranhlh.net/tim-kiem?q="+newQuery);

                }
            }
        });
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {

                Suggestion suggestion= (Suggestion) item;
                Glide.with(MainActivity.this).
                        asBitmap().
                        load(suggestion.getAnh()).
                        centerCrop().
                        into(leftIcon);
            }

        });
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_update)
                {
                    new DownloadTask().execute(MY_URL);
                }
            }
        });
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Suggestion suggestion= (Suggestion) searchSuggestion;
                Manga manga1 = new Manga(suggestion.getBody(),"chap 1",suggestion.getAnh(),suggestion.getMlink());

                //Toast.makeText(getApplicationContext(),"Ban vua chon "+suggestion.getMlink(),Toast.LENGTH_SHORT).show();
                Bundle b = new Bundle();
                b.putSerializable("truyen",manga1);
                Intent intent= new Intent(MainActivity.this,ChapActivity.class);
                intent.putExtra("data",b);
                startActivity(intent);
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });
    }



    private void init(){
        truyentranhlist = new ArrayList<>();
////        truyentranhlist.add(new Manga("Kishuku Gakkou No Juliet","Chapter 119","https://hinhnendephd.com/wp-content/uploads/2019/07/hinh-nen-anime-boy-cute-lanh-lung-dang-yeu-cho-iphone-1.jpg"));
////        truyentranhlist.add(new Manga("Công Chúa Pháo Hôi Muốn Tùy Tiện Đến Cùng","Chapter 16","https://st.imageinstant.net/data/comics/191/cong-chua-phao-hoi-muon-tuy-tien-den-cun-2805.jpg"));
////        truyentranhlist.add(new Manga("Công Lược Trưởng Thành Của Vương Phi","Chapter 35","https://st.imageinstant.net/data/comics/168/cong-luoc-truong-thanh-cua-vuong-phi.jpg"));
//
        adapter = new TruyentranhAdapter(truyentranhlist,this,this);
//
    }

    private void anhXa(){
        recyclerView = findViewById(R.id.grvTruyen);
        TextView textView = (TextView) findViewById(R.id.tv_head);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_auto_awesome_black_24dp, 0, 0, 0);
        mSearchView = findViewById(R.id.floating_search_view);
        //drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //navigationView = (NavigationView) findViewById(R.id.nav_view);
    }
    private void setUP(){

        recyclerView.setAdapter(adapter);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);
        //recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(layoutManager);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.parent_view, fragment).commit();
    }
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_down:
                showFragment(new SaveFragment());
                return true;
            case R.id.nav_drafts:
                Toast.makeText(this,"We are tesing",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_sent:
                Toast.makeText(this,"We are tesing",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_trash:
                Toast.makeText(this,"We are tesing",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, ArrayList<Manga>>  {

        private static final String TAG = "DownloadTask";

        @Override
        protected ArrayList<Manga> doInBackground(String... strings) {
            Document document = null;
            //ArrayList<Manga> listArticle = new ArrayList<>();
            try {
                document = (Document) Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Elements sub = document.select("div.thumb-item-flow");
                    for (int i=0;i<26;i++) {
                        Element element = sub.get(i);
                        Manga article = new Manga();
                        Element titleSubject = element.getElementsByClass("thumb_attr series-title").first();
                        Element imgSubject = element.select("div[data-bg]").first();
                        Element descrip = element.getElementsByClass("thumb_attr chapter-title text-truncate").first();
                        Element linkmanga = element.select("a").first();
                        //Parse to model
                        //Log.d("hi",descrip.text());
                        if (titleSubject != null) {
                            Element titleS = titleSubject.selectFirst("[title]");
                            String title = titleS.attr("title");
                            article.setTentruyen(title);
                        }
                        if (imgSubject != null) {
                            String src = imgSubject.attr("data-bg");

                            article.setLinkanh(src);
                        }
                        if (descrip != null) {
                            String des = descrip.attr("title");
                            article.setChap(des);
                        }
                        if(linkmanga != null){
                            String linkm = linkmanga.attr("href");
                            article.setLinktruyen(linkm);
                        }
                        //Add to list
                        truyentranhlist.add(article);

                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return truyentranhlist;
        }

        @Override
        protected void onPostExecute(ArrayList<Manga> articles) {
            super.onPostExecute(articles);
            //Setup data recyclerView
            //adapter = new TruyentranhAdapter(truyentranhlist,MainActivity.this, this);
            recyclerView.setAdapter(adapter);
        }

    }

    public void onClick(Manga manga) {
//        Log.i("zzz",manga.getTentruyen());
//        Toast.makeText(this,manga.getTentruyen(),Toast.LENGTH_SHORT).show();
        Bundle b = new Bundle();
        b.putSerializable("truyen",manga);
        Intent intent= new Intent(MainActivity.this,ChapActivity.class);
        intent.putExtra("data",b);
        startActivity(intent);
    }
}