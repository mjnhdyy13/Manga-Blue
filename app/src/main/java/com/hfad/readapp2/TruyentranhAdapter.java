package com.hfad.readapp2;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class TruyentranhAdapter extends RecyclerView.Adapter<TruyentranhAdapter.ViewHolder> {

    private ArrayList<Manga> listAticle;

    private Context ct;
    private ItemClickListener itemClickListener;
    public RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform();

    public TruyentranhAdapter( ArrayList<Manga> listAticle,Context ct,ItemClickListener itemClickListener) {

        this.listAticle = listAticle;
        this.ct = ct;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View iteamview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_truyen,parent,false);
        return new ViewHolder(iteamview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Manga manga = listAticle.get(position);
        holder.tentruyen.setText(manga.getTentruyen());
        holder.tenchap.setText(manga.getChap());
        holder.bindata(manga);
        Glide.with(this.ct).load(manga.getLinkanh()).
                apply(options).
                into(holder.imgtruyen);

    }

    @Override
    public int getItemCount() {
        return listAticle.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tenchap,tentruyen;
        private Manga manga;
        private ImageView imgtruyen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("hiz",manga.getTentruyen());
                    itemClickListener.onClick(manga);
                }
            });
            tenchap = itemView.findViewById(R.id.chapter);
            tentruyen = itemView.findViewById(R.id.tentruyen);
            imgtruyen = itemView.findViewById(R.id.imgTruyen);


        }
        private void bindata(Manga manga){
            if(manga.getTentruyen()!=null){
                //Log.d("hiz",manga.getTentruyen());
            }
            this.manga = manga;
//            tenchap.setText(manga.getChap());
//            tentruyen.setText(manga.getTentruyen());
        }
    }
}

