package com.msc.libcoremodel.datamodel.http.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.msc.libcoremodel.datamodel.http.dao.SearchHotsDao;
import com.msc.libcoremodel.datamodel.http.entities.SearchHotsData;

@Database(entities = {SearchHotsData.class}, version = 1, exportSchema = false)
public abstract class SearchHotsDatabase extends RoomDatabase {

    public static SearchHotsDatabase getDefault(Context context) {
        return buildDatabase(context);
    }

    private static SearchHotsDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), SearchHotsDatabase.class, "SearchHotsHistory.db")
                .allowMainThreadQueries()
                .build();
    }

    public abstract SearchHotsDao getSearchHotsDao();
}