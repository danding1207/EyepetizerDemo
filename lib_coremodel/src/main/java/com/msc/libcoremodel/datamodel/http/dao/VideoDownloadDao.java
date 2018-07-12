package com.msc.libcoremodel.datamodel.http.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.msc.libcoremodel.datamodel.http.entities.VideoDownloadData;

import java.util.List;

@Dao
public interface VideoDownloadDao {

    /**
     * 查询所有
     *
     * @return
     */
    @Query("SELECT * FROM VideoDownloadHistory")
    List<VideoDownloadData> getVideoDownloadHistoryAll();

    /**
     * 根据指定字段查询
     * @param id
     * @return
     */
    @Query("SELECT * FROM VideoDownloadHistory WHERE ID = :id")
    List<VideoDownloadData> loadVideoDownloadHistoryById(int id);

    /**
     * 根据指定字段查询
     * @param fileId
     * @return
     */
    @Query("SELECT * FROM VideoDownloadHistory WHERE FILEID = :fileId")
    List<VideoDownloadData> loadVideoDownloadHistoryByFileId(int fileId);

    /**
     * 根据指定字段查询
     * @param downloadId
     * @return
     */
    @Query("SELECT * FROM VideoDownloadHistory WHERE DOWNLOADID = :downloadId")
    List<VideoDownloadData> loadVideoDownloadHistoryByDownloadId(int downloadId);

    /**
     * 项数据库添加数据
     *
     * @param
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VideoDownloadData> dataList);

    /**
     * 项数据库添加数据
     *
     * @param
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VideoDownloadData data);


    /**
     * 修改数据
     *
     * @param data
     */
    @Update()
    void update(VideoDownloadData data);

    /**
     * 删除数据
     *
     * @param data
     */
    @Delete()
    void delete(VideoDownloadData data);

    /**
     * 删除数据
     *
     * @param dataList
     */
    @Delete()
    void delete(List<VideoDownloadData> dataList);

}
