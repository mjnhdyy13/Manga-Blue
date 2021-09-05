package com.hfad.readapp2.download;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hfad.readapp2.Chap;
import com.hfad.readapp2.R;
import com.hfad.readapp2.save.Truyen;
import com.race604.drawable.wave.WaveDrawable;

import java.io.File;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AsynDownload extends AsyncTask<Truyen, Integer, Void> {
    Context ct;
    int count =0;
    ProgressDialog myPd_bar;
    int total;
    NotificationCompat.Builder notificationBuil;
    int progressMax=100;
    WaveDrawable mWaveDrawable;
    private NotificationManagerCompat notificationManagerCompat;
    ImageView imageView;
    TextView textView;
    Button button;
    public static final int progress_bar_type = 0;
//    GlideBuilder glideBuilder =new GlideBuilder();



//    private String sdRootPath = Environment.getExternalStorageDirectory().getPath();
//    private String appRootPath = null;
//    private String getStorageDirectory(){
//        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
//                sdRootPath : appRootPath;
//    }


    public AsynDownload(Context context, ImageView imageView, TextView textView, Button button){
        this.ct =context;
        this.imageView = imageView;
        this.textView = textView;
        this.button = button;
    }

    @Override
    protected void onPreExecute() {

        notificationManagerCompat = NotificationManagerCompat.from(ct);
        notificationBuil =
                new NotificationCompat.Builder(ct, "noty")
                        .setSmallIcon(R.drawable.baseline_download_black_24dp)
                        .setContentTitle("Download")
                        .setContentText("Download in progress")
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setOngoing(true)
                        .setOnlyAlertOnce(true)
                        .setProgress(progressMax,0,false);
        notificationManagerCompat.notify(2,notificationBuil.build());

        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        mWaveDrawable = new WaveDrawable(ct, R.drawable.tagai);
        imageView.setImageDrawable(mWaveDrawable);
        mWaveDrawable.setWaveAmplitude(10);

//        myPd_bar = new ProgressDialog(ct);
//        myPd_bar.setMessage("Downloading file. Please wait...");
//        myPd_bar.setIndeterminate(false);
//        myPd_bar.setMax(100);
//        myPd_bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        myPd_bar.setCancelable(true);
//        myPd_bar.show();

        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);


    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int number = values[0];
        mWaveDrawable.setLevel(number*100);
        notificationBuil.setProgress(progressMax,number,false);
        notificationManagerCompat.notify(2,notificationBuil.build());
        if(count == total){
            button.setVisibility(View.VISIBLE);
            textView.setText("Successful Download");
            textView.setTextColor(Color.rgb(200,0,0));
            imageView.setImageResource(R.drawable.yukino);
            notificationBuil.setContentText("Download finished")
                    .setProgress(0,0,false)
                    .setOngoing(false);
            notificationManagerCompat.notify(2,notificationBuil.build());
        }
        //tăng giá trị của Progressbar lên
        //myPd_bar.setProgress(number);
    }

    @Override
    protected Void doInBackground(Truyen... arrayLists) {

        Truyen truyen1 = arrayLists[0];
        ArrayList<Chap> arraychap = truyen1.getArraychap();

        for(int i=0; i< arraychap.size();i++) {
            Chap chap = arraychap.get(i);
            ArrayList<String> arrI;
            arrI = chap.getListimage();
            for (int j = 0; j < arrI.size(); j++) {
                total++;
            }
        }
        glidebuild(truyen1.getNamemanga());
        Log.d("kiem tra",String.valueOf(total));
        for(int i=0; i< arraychap.size();i++){

            Chap chap = arraychap.get(i);
            ArrayList<String> arrI;
            arrI = chap.getListimage();
            for(int j=0; j< arrI.size();j++){
                downloadImage(arrI.get(j),chap.getTenChap());
            }
            Log.d("tai xong", chap.getTenChap());
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

    String sdRootPath = Environment.getExternalStorageDirectory().getPath();
    String appRootPath = null;
    private String getStorageDirectory(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                sdRootPath : appRootPath;
    }
    private void glidebuild(String nameChap){


        GlideBuilder glideBuilder =new GlideBuilder();
        int diskCacheSizeBytes = 1024 * 1024 * 250; // 100 MB
        glideBuilder.setDiskCache(
                new DiskLruCacheFactory( getStorageDirectory()+"/GlideDisk/"+nameChap, diskCacheSizeBytes ));


        Glide.init(ct,glideBuilder);
    }
    public void downloadImage(String imageURL, String nameChap) {

        File cacheFile = Glide.getPhotoCacheDir(ct);


        String s = cacheFile.toString();
        Log.d("aaaaaaaaa",s);

        Glide.with(ct)
                .load(imageURL)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        Log.d("tai thanh cong", imageURL);
                        count=count+1;
                        publishProgress(count*100/total);
                        Log.d("downloadtask",String.valueOf(count));
                        //Toast.makeText(ct, "Saving Image...", Toast.LENGTH_SHORT).show();
                        //saveImage(bitmap, dir, fileName);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        //Toast.makeText(ct, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * Showing Dialog
     * */


}
