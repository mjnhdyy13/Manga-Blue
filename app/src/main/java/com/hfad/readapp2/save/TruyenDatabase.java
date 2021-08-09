package com.hfad.readapp2.save;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Truyen.class}, version = 1)
@TypeConverters({Truyen.Converters.class})
public abstract class TruyenDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "truyen.db";
    private static TruyenDatabase instance;

    public static synchronized TruyenDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),TruyenDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract TruyenDAO truyenDAO();

}
