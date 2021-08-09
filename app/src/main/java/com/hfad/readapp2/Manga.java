package com.hfad.readapp2;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Manga implements Serializable {
    private String id,tentruyen,chap,linkanh,linktruyen;

    /*
    {
    "tenTruyen":"",
    "tenChap":"",
    "linkAnh":""
    }
     */
    public Manga(){

    }
    public Manga(JSONObject o) throws JSONException {
        id = o.getString("id");
        tentruyen = o.getString("tenTruyen");
        chap = o.getString("tenChap");
        linkanh = o.getString("linkAnh");
    }


    public Manga(String tentruyen, String tenchap, String linkanh,String linktruyen) {
        this.tentruyen = tentruyen;
        this.chap = tenchap;
        this.linkanh = linkanh;
        this.linktruyen = linktruyen;
    }

    public String getTentruyen() {
        return tentruyen;
    }

    public void setTentruyen(String tentruyen) {
        this.tentruyen = tentruyen;
    }

    public String getChap() {
        return chap;
    }

    public void setChap(String chap) {
        this.chap = chap;
    }

    public String getLinkanh() {
        return linkanh;
    }

    public void setLinkanh(String linkanh) {
        this.linkanh = linkanh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinktruyen() {
        return linktruyen;
    }

    public void setLinktruyen(String linktruyen) {
        this.linktruyen = linktruyen;
    }
}

