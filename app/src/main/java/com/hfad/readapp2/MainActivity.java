package com.hfad.readapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Button;
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
import com.hfad.readapp2.searchview.BaseExampleFragmentCallbacks;
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


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, BaseExampleFragmentCallbacks, DrawerlayoutOpen {

    public static final String MY_URL = "https://truyentranhlh.net/";
    private static final int Fragment_home =1;
    private static final int Fragment_save =2;

    private int currentFragment = Fragment_home;
    DrawerlayoutOpen drawerlayoutOpen;


    DrawerLayout drawerLayout;
    NavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        setUP();

    }


    private void anhXa(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        drawerlayoutOpen.GetDrawerlayout(drawerLayout);
    }
    private void setUP(){
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new HomeFragment(this));

    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.nav_home:
                if(Fragment_home != currentFragment) {
                    replaceFragment(new HomeFragment(this));
                    currentFragment = Fragment_home;
                    return true;
                }
            case R.id.nav_down:
                if(Fragment_save != currentFragment) {
                    replaceFragment(new SaveFragment(this));
                    currentFragment = Fragment_save;
                    return true;
                }
            case R.id.nav_sent:
                Toast.makeText(this,"We are tesing 1",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_trash:
                Toast.makeText(this,"We are tesing 2",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }
    @Override
    public void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
        searchView.attachNavigationDrawerToMenuButton(drawerLayout);
    }

    @Override
    public void GetDrawerlayout(ImageView button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }


}