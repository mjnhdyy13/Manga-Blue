package com.hfad.readapp2.save;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.google.android.material.snackbar.Snackbar;
import com.hfad.readapp2.ChapActivity;
import com.hfad.readapp2.DrawerlayoutOpen;
import com.hfad.readapp2.HomeFragment;
import com.hfad.readapp2.MainActivity;
import com.hfad.readapp2.Manga;
import com.hfad.readapp2.R;
import com.hfad.readapp2.TruyentranhAdapter;
import com.hfad.readapp2.searchview.BaseExampleFragmentCallbacks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SaveFragment extends Fragment implements ItemClickOffline,RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    RecyclerView recyclerView;
    SaveAdapter saveAdapter;
    List<Truyen> arrayListS;


    RelativeLayout relativeLayout;
    DrawerlayoutOpen drawerlayoutOpen;
    ImageView bthome;
    String sdRootPath = Environment.getExternalStorageDirectory().getPath();
    String appRootPath = null;
    public SaveFragment() {
        // Required empty public constructor
    }

    public SaveFragment(DrawerlayoutOpen drawerlayoutOpen ){
        this.drawerlayoutOpen = drawerlayoutOpen;
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcysave);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.deltaRelative);
        bthome = (ImageView) view.findViewById(R.id.homesave);
        init();
        //glidebuild("box_avatar");
        configRecyclerView();
        setUP();

    }


    private void glidebuild(String nameChap){
        int diskCacheSizeBytes = 1024 * 1024 * 250; // 100 MB
        GlideBuilder glideBuilder =new GlideBuilder();
        glideBuilder.setDiskCache(
                new DiskLruCacheFactory( getStorageDirectory()+"/GlideDisk/"+nameChap, diskCacheSizeBytes )
        );
        Glide.init(getContext(),glideBuilder);
    }
    private void setUP() {
        recyclerView.setAdapter(saveAdapter);
    }

    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    private void init() {
        drawerlayoutOpen.GetDrawerlayout(bthome);
        arrayListS = new ArrayList<>();
        arrayListS = TruyenDatabase.getInstance(getContext()).truyenDAO().getListTruyen();
        saveAdapter = new SaveAdapter(arrayListS,getContext(),this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save, container, false);
    }

    @Override
    public void onclick(Truyen truyen) {
        Bundle b = new Bundle();
        b.putSerializable("truyenoffline",truyen);
        Intent intent= new Intent(getActivity(), ChapActivity.class);
        intent.putExtra("dataOff",b);
        startActivity(intent);
    }


    private String getStorageDirectory(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                sdRootPath : appRootPath;
    }
    public static boolean deleteDirectory(File path) {
        // TODO Auto-generated method stub
        if( path.exists() ) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof SaveAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = arrayListS.get(viewHolder.getAdapterPosition()).getNamemanga();

            // backup of removed item for undo purpose
            final Truyen deletedItem = arrayListS.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            new AlertDialog.Builder(getContext())
                    .setTitle("Confirm delete this")
                    .setMessage("Are you sure")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveAdapter.removeItem(viewHolder.getAdapterPosition());
                            TruyenDatabase.getInstance(getContext()).truyenDAO().deleteTruyen(deletedItem);
                            File mypath = new File(getStorageDirectory() + "/GlideDisk/" + deletedItem.getNamemanga());
                            Log.d("pathfile", mypath.toString());
                            deleteDirectory(mypath);
                            Toast.makeText(getContext(),"Delete "+deletedItem.getNamemanga()+" successfully",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //saveAdapter.restoreItem(deletedItem,deletedIndex);
                            saveAdapter.setData(arrayListS);
                        }
                    })
                    .show();
            // remove the item from recycler view


        }
    }



}