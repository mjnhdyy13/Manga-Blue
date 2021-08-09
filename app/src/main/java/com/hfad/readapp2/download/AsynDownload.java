package com.hfad.readapp2.download;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hfad.readapp2.Chap;
import com.hfad.readapp2.R;

import java.io.File;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AsynDownload extends AsyncTask<ArrayList<Chap>, Void, Void> {
    Context ct;
//    GlideBuilder glideBuilder =new GlideBuilder();



//    private String sdRootPath = Environment.getExternalStorageDirectory().getPath();
//    private String appRootPath = null;
//    private String getStorageDirectory(){
//        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
//                sdRootPath : appRootPath;
//    }


    public AsynDownload(Context context){
        this.ct =context;
    }
    @Override
    protected Void doInBackground(ArrayList<Chap>... arrayLists) {
        GlideBuilder glideBuilder =new GlideBuilder();
        //glidebuild("chap 1");
        for(int i=1; i< arrayLists[0].size();i++){

            Chap chap = arrayLists[0].get(i);
            ArrayList<String> arrI;
            arrI = chap.getListimage();
            glidebuild(chap.getTenChap(),glideBuilder);
            for(int j=0; j< arrI.size();j++){
                downloadImage(arrI.get(j),chap.getTenChap());
            }
            Log.d("hetchap", chap.getTenChap());

        ///glidebuild("chap 2");
        //Glide.get(ct).clearDiskCache();
        /*ArrayList<String> arrUrlAnh = new ArrayList<>();
        arrUrlAnh.add("http://cdn6.truyentranh8.net/hdd2/u2/cacom/32635/000-680802/001.jpg");
        arrUrlAnh.add("http://cdn6.truyentranh8.net/hdd2/u2/cacom/32635/000-680802/003.jpg");
        arrUrlAnh.add("http://cdn6.truyentranh8.net/hdd2/u2/cacom/32635/000-680802/004.jpg");
        arrUrlAnh.add("http://cdn6.truyentranh8.net/hdd2/u2/cacom/32635/000-680802/005.jpg");
        arrUrlAnh.add("http://cdn6.truyentranh8.net/hdd2/u2/cacom/32635/000-680802/006.jpg");
        for(int i=0;i<arrUrlAnh.size();i++){
            downloadImage(arrUrlAnh.get(i),"chap");*/

        //glidebuild("chap 2");

        }
        return null;
    }
//    public Boolean verifyPermissions() {
//        // This will return the current Status
//        int permissionExternalMemory = ActivityCompat.checkSelfPermission(ct, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
//            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//            // If permission not granted then ask for permission real time.
//            ActivityCompat.requestPermissions(activity, STORAGE_PERMISSIONS, 1);
//            return false;
//        }
//        return true;
//    }
    String sdRootPath = Environment.getExternalStorageDirectory().getPath();
    String appRootPath = null;
    private String getStorageDirectory(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                sdRootPath : appRootPath;
    }
    private void glidebuild(String nameChap,GlideBuilder glideBuilder){
        int diskCacheSizeBytes = 1024 * 1024 * 250; // 100 MB
        glideBuilder.setDiskCache(
                new DiskLruCacheFactory( getStorageDirectory()+"/GlideDisk/"+nameChap, diskCacheSizeBytes )
        );
        Glide.init(ct,glideBuilder);
    }
    public void downloadImage(String imageURL, String nameChap) {

        File cacheFile = Glide.getPhotoCacheDir(ct);


        String s = cacheFile.toString();
        Log.d("aaaaaaaaa",s);
        Glide.with(ct)
                .load(imageURL)

                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        Log.d("tai thanh cong", imageURL);
                        Toast.makeText(ct, "Saving Image...", Toast.LENGTH_SHORT).show();
                        //saveImage(bitmap, dir, fileName);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Toast.makeText(ct, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
