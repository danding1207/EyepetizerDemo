package com.msc.libcoremodel.datamodel.http.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.msc.libcoremodel.datamodel.http.dao.SearchHotsDao;
import com.msc.libcoremodel.datamodel.http.dao.VideoDownloadDao;
import com.msc.libcoremodel.datamodel.http.entities.SearchHotsData;
import com.msc.libcoremodel.datamodel.http.entities.VideoDownloadData;
import com.msc.libcoremodel.datamodel.http.typeconverters.DownloadStatusConverters;

@Database(entities = {VideoDownloadData.class}, version = 1, exportSchema = false)
@TypeConverters({DownloadStatusConverters.class})
public abstract class VideoDownloadDatabase extends RoomDatabase {

    public static VideoDownloadDatabase getDefault(Context context) {
        return buildDatabase(context);
    }

    private static VideoDownloadDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                VideoDownloadDatabase.class, "VideoDownloadHistory.db")
                .allowMainThreadQueries()
                .build();
    }

    public abstract VideoDownloadDao getVideoDownloadDao();
}