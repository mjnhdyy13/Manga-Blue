package com.hfad.readapp2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hfad.readapp2.searchview.BaseExampleFragmentCallbacks;
import com.hfad.readapp2.searchview.MakeSuggestion;
import com.hfad.readapp2.searchview.Suggestion;
import com.hfad.readapp2.searchview.asyn;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements ItemClickListener, MakeSuggestion {


    public static final String MY_URL = "https://truyentranhlh.net/";
    RecyclerView recyclerView;
    TruyentranhAdapter adapter;
    ArrayList<Manga> truyentranhlist;
    DrawerLayout drawerLayout1;

    ImageView nowifi;
    BaseExampleFragmentCallbacks mcallback ;
    private FloatingSearchView mSearchView;
    private asyn a = null;
    MakeSuggestion makeSuggestion = this;

    @Override
    public void getSuggestion(List<Suggestion> suggestions) {
        mSearchView.swapSuggestions(suggestions);
        mSearchView.hideProgress();
    }
    public HomeFragment(BaseExampleFragmentCallbacks baseExampleFragmentCallbacks ){
        this.mcallback = baseExampleFragmentCallbacks;
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.grvTruyen);
        TextView textView = (TextView) view.findViewById(R.id.tv_head);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_auto_awesome_black_24dp, 0, 0, 0);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        drawerLayout1 = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        nowifi = (ImageView) view.findViewById(R.id.nowifi);

        init();
        Configsearchview();
        configRecyclerView();
        setUP();
        isOnline();
        new DownloadTask().execute(MY_URL);
        //isOnline();




    }
    public void isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            nowifi.setVisibility(View.GONE);
            Log.d("Check internet","online");
        } else {
            nowifi.setVisibility(View.VISIBLE);
            Log.d("Check internet","offline");
        }
    }
    private void configRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        //recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(layoutManager);
    }



    private void setUP() {
        recyclerView.setAdapter(adapter);
    }

    private void Configsearchview() {
        //mSearchView.setBackgroundColor(Color.parseColor("#787878"));
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
        mcallback.onAttachSearchViewToDrawer(mSearchView);


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
                Glide.with(getActivity()).
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
                    isOnline();
                }
            }
        });
//        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
//            @Override
//            public void onHomeClicked() {
//
//                drawerLayout1.openDrawer(GravityCompat.START);
//                Log.d("TAG", "onHomeClicked()");
//            }
//        });
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Suggestion suggestion= (Suggestion) searchSuggestion;
                Manga manga1 = new Manga(suggestion.getBody(),"chap 1",suggestion.getAnh(),suggestion.getMlink());

                //Toast.makeText(getApplicationContext(),"Ban vua chon "+suggestion.getMlink(),Toast.LENGTH_SHORT).show();
                Bundle b = new Bundle();
                b.putSerializable("truyen",manga1);
                Intent intent= new Intent(getActivity(),ChapActivity.class);
                intent.putExtra("data",b);
                startActivity(intent);
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });

    }

    private void init() {
        truyentranhlist = new ArrayList<>();
        adapter = new TruyentranhAdapter(truyentranhlist,getContext(),this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        recyclerView = (RecyclerView) getView().findViewById(R.id.grvTruyen);
//        TextView textView = (TextView) getView().findViewById(R.id.tv_head);
//        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_auto_awesome_black_24dp, 0, 0, 0);
//        mSearchView = (FloatingSearchView) getView().findViewById(R.id.floating_search_view);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onClick(Manga manga) {
        Bundle b = new Bundle();
        b.putSerializable("truyen",manga);
        Intent intent= new Intent(getActivity(),ChapActivity.class);
        intent.putExtra("data",b);
        startActivity(intent);

    }



//    @Override
//    public boolean onActivityBackPress() {
//        if (!mSearchView.setSearchFocused(false)) {
//            return false;
//        }
//        return true;
//    }

    private class DownloadTask extends AsyncTask<String, Void, ArrayList<Manga>> {

        private static final String TAG = "DownloadTask";

        @Override
        protected ArrayList<Manga> doInBackground(String... strings) {
            Document document = null;
            Glide.get(getContext()).clearDiskCache();
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
            recyclerView.setAdapter(adapter);


        }

    }


}