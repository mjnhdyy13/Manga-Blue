package com.hfad.readapp2.save;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hfad.readapp2.ItemClickListener;
import com.hfad.readapp2.Manga;
import com.hfad.readapp2.R;
import com.hfad.readapp2.TruyentranhAdapter;

import java.util.ArrayList;
import java.util.List;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder> {
    private List<Truyen> listAticle;

    private Context ct;

    private ItemClickOffline itemClickListener;
    public SaveAdapter(List<Truyen> listAticle, Context ct,ItemClickOffline listener) {

        this.itemClickListener =listener;
        this.listAticle = listAticle;
        this.ct = ct;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View iteamview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_save, parent, false);
        return new ViewHolder(iteamview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Truyen manga = listAticle.get(position);
        holder.tentruyen.setText(manga.getNamemanga());
        holder.bindata(manga);
        Log.d("test chap",manga.getArraychap().get(0).getTenChap());
        holder.tenchap.setText(manga.getArraychap().get(0).getTenChap());
        Glide.with(this.ct).load(manga.getLinkavatar()).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                into(holder.imgtruyen);
    }


    @Override
    public int getItemCount() {
        return listAticle.size();
    }

    public void removeItem(int position) {
        listAticle.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
    public void setData(List<Truyen> list){
        this.listAticle = list;
        notifyDataSetChanged();
    }
    public void restoreItem(Truyen item, int position) {
        listAticle.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tenchap, tentruyen;
        private ImageView imgtruyen;
        private Truyen truyen;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("hiz",manga.getTentruyen());
                    itemClickListener.onclick(truyen);
                }
            });
            tenchap = itemView.findViewById(R.id.nameC);
            tentruyen = itemView.findViewById(R.id.nameT);
            imgtruyen = itemView.findViewById(R.id.imgTS);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_front);


        }
        private void bindata(Truyen truyen){

            this.truyen = truyen;
        }
    }

}
