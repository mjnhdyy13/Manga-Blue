package com.hfad.readapp2;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class DocTruyenAdapter extends RecyclerView.Adapter<DocTruyenAdapter.ViewHolder> {
    private ArrayList<String> manh;
    private Context mContext;
    public RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform();


    public DocTruyenAdapter(ArrayList<String> manh, Context mContext) {
        this.manh = manh;
        this.mContext = mContext;
        //setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iteamview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doc,parent,false);


        return new ViewHolder(iteamview);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String linkanh = manh.get(position);


        Glide.with(this.mContext).

                load(linkanh).
                //override(250,750).
                //placeholder(R.drawable.loading).
                apply(options).
                diskCacheStrategy(DiskCacheStrategy.ALL).

                into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return manh.size();
    }

    /**
     * Lớp nắm giữ cấu trúc view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemview;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            imageView = itemView.findViewById(R.id.img_truyen);

        }
    }



}