package com.msc.libcoremodel.datamodel.http.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.msc.libcoremodel.datamodel.http.entities.SearchHotsData;
import java.util.List;

@Dao
public interface  SearchHotsDao {

    /**
     * 查询所有
     *
     * @return
     */
    @Query("SELECT * FROM SearchHotsHistory")
    List<SearchHotsData> getSearchHotsHistoryAll();

    /**
     * 根据指定字段查询
     *
     * @return
     */
    @Query("SELECT * FROM SearchHotsHistory WHERE ID = :id")
    List<SearchHotsData> loadSearchHotsDataById(String id);

    /**
     * 根据指定字段查询
     *
     * @return
     */
    @Query("SELECT * FROM SearchHotsHistory WHERE NAME = :name")
    List<SearchHotsData> loadSearchHotsDataByName(String name);

    /**
     * 项数据库添加数据
     *
     * @param dataList
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SearchHotsData> dataList);

    /**
     * 项数据库添加数据
     *
     * @param data
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchHotsData data);


    /**
     * 修改数据
     *
     * @param data
     */
    @Update()
    void update(SearchHotsData data);

    /**
     * 删除数据
     *
     * @param data
     */
    @Delete()
    void delete(SearchHotsData data);

    /**
     * 删除数据
     *
     * @param dataList
     */
    @Delete()
    void delete(List<SearchHotsData> dataList);

}
