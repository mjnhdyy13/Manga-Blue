package com.hfad.readapp2.save;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Dao
@TypeConverters({Truyen.Converters.class})
public interface TruyenDAO {

    @Insert
    void insertTruyen(Truyen truyen);

    @Query("SELECT * FROM truyen")
    List<Truyen> getListTruyen();

    @Delete
    void deleteTruyen(Truyen truyen);
}
