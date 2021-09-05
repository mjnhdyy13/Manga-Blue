package com.hfad.readapp2;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hfad.readapp2.save.Truyen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChapTruyenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Chap> arraychap;
    private Context mContext;
    private ItemClickChap listener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    ProgressBar progressBar1;
    BeautifulProgressDialog progressDialog;



    public ChapTruyenAdapter(ArrayList<Chap> arraychap, Context mContext,ItemClickChap listener) {
        this.arraychap = arraychap;
        this.mContext = mContext;
        this.listener = listener;
        //setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chap, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading ,parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return arraychap.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return arraychap == null ? 0 : arraychap.size();
    }

//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView txtTenChap, txtNgayNhap;
//        private Chap chap;
//        private int position;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onclick(chap,position);
//                }
//            });
//            txtTenChap = itemView.findViewById(R.id.txvTenChap);
//            txtNgayNhap = itemView.findViewById(R.id.txvNgay);
//
//        }
//        private void bindata(Chap chap,int position){
//
//            this.position =position;
//            this.chap = chap;
//        }
//    }
    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTenChap, txtNgayNhap;
        private Chap chap;
        private int position;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onclick(chap,position);
                }
            });
            txtTenChap = itemView.findViewById(R.id.txvTenChap);
            txtNgayNhap = itemView.findViewById(R.id.txvNgay);
        }
        private void bindata(Chap chap,int position){
            this.position =position;
            this.chap = chap;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressBar1 = progressBar;
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed


    }

    public void removeLastItem(){
        arraychap.remove(arraychap.size() - 1);
        notifyDataSetChanged();
    }
    public void ProgressOff(){
        progressBar1.setVisibility(View.GONE);

    }
    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Chap chapTruyen = arraychap.get(position);
        viewHolder.txtTenChap.setText(chapTruyen.getTenChap());
        Log.d("test adapterchap",chapTruyen.getTenChap());
        viewHolder.bindata(chapTruyen,position);
        viewHolder.txtNgayNhap.setText(chapTruyen.getNgayDang());

    }



}