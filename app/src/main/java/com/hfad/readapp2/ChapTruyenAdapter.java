package com.hfad.readapp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

public class ChapTruyenAdapter extends ArrayAdapter<Chap> {
    private Context ct;
    private ArrayList<Chap> arr;
    private int resource;
    public ChapTruyenAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Chap> arrayList) {
        super(context, resource, arrayList);
        this.ct = context;
        this.resource = resource;
        this.arr = arrayList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_chap, null);
        }
        if (arr.size() > 0) {
            TextView txtTenChap, txtNgayNhap;
            txtTenChap = convertView.findViewById(R.id.txvTenChap);
            txtNgayNhap = convertView.findViewById(R.id.txvNgay);

            Chap chapTruyen = arr.get(position);
            txtTenChap.setText(chapTruyen.getTenChap());
            txtNgayNhap.setText(chapTruyen.getNgayDang());

        }
        return convertView;
    }

}
