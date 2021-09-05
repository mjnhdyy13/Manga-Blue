package com.hfad.readapp2.download;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.readapp2.Chap;
import com.hfad.readapp2.DocTruyenAdapter;
import com.hfad.readapp2.R;

import java.util.ArrayList;

public class ShowDownloadAdapter extends RecyclerView.Adapter<ShowDownloadAdapter.ViewHolder> {

    private ArrayList<Chap> arraychap;
    private Context mContext;
    public ShowDownloadAdapter(ArrayList<Chap> arraychap, Context mContext) {
        this.arraychap = arraychap;
        this.mContext = mContext;
        //setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iteamview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download,parent,false);
        return new ViewHolder(iteamview);

    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chap chap = arraychap.get(position);
        String tenchap = chap.getTenChap();
        holder.txvchap.setText(tenchap);
//        holder.txvchap.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_label_important_black_24dp, 0, 0, 0);

    }

    @Override
    public int getItemCount() {
        return arraychap.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemview;
        public TextView txvchap;

        public ViewHolder(View itemView) {
            super(itemView);

            txvchap= itemView.findViewById(R.id.textView1);

        }
    }
}
