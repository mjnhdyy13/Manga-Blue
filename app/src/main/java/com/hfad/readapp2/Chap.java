package com.hfad.readapp2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Chap implements Serializable {
    private String tenChap,ngayDang,linkchap;
    private ArrayList<String> listimage;

    public Chap(){

    }

    public ArrayList<String> getListimage() {
        return listimage;
    }

    public void setListimage(ArrayList<String> listimage) {
        this.listimage = listimage;
    }

    public String getTenChap() {
        return tenChap;
    }

    public void setTenChap(String tenChap) {
        this.tenChap = tenChap;
    }

    public String getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(String ngayDang) {
        this.ngayDang = ngayDang;
    }

    public Chap(String tenChap, String ngayDang,String linkchap) {
        this.tenChap = tenChap;
        this.ngayDang = ngayDang;
        this.linkchap = linkchap;
    }
    public Chap(JSONObject o) throws JSONException {
        tenChap = o.getString("tenchap");
        ngayDang = o.getString("ngaynhap");
    }

    public String getLinkchap() {
        return linkchap;
    }

    public void setLinkchap(String linkchap) {
        this.linkchap = linkchap;
    }
}
