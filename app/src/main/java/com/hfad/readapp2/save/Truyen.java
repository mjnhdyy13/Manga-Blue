package com.hfad.readapp2.save;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hfad.readapp2.Chap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "truyen")
public class Truyen {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String namemanga;
    private String linkavatar;
    private ArrayList<Chap> arraychap;

    public Truyen(String namemanga, String linkavatar, ArrayList<Chap> arraychap){
        this.arraychap = arraychap;
        this.linkavatar = linkavatar;
        this.namemanga = namemanga;
    }

    public static class Converters {
        @TypeConverter
        public String fromValuesToList(ArrayList<Chap> value) {
            if (value== null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Chap>>() {}.getType();
            return gson.toJson(value, type);
        }


        @TypeConverter
        public ArrayList<Chap> toOptionValuesList(String value) {
            if (value== null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<Chap>>() {
            }.getType();
            return gson.fromJson(value, type);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamemanga() {
        return namemanga;
    }

    public void setNamemanga(String namemanga) {
        this.namemanga = namemanga;
    }

    public String getLinkavatar() {
        return linkavatar;
    }

    public void setLinkavatar(String linkavatar) {
        this.linkavatar = linkavatar;
    }

    public ArrayList<Chap> getArraychap() {
        return arraychap;
    }

    public void setArraychap(ArrayList<Chap> arraychap) {
        this.arraychap = arraychap;
    }
}
