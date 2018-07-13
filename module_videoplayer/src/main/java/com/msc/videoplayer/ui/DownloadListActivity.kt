package com.msc.videoplayer.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.gson.Gson
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.StatusUtil
import com.liulishuo.okdownload.core.Util
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import com.msc.libcommon.util.FileUtil
import com.msc.libcommon.viewcard.DownloadCardView
import com.msc.libcommon.viewcard.EndCardView
import com.msc.libcommon.viewcard.VideoSmallCardView
import com.msc.libcommon.viewcardcell.DownloadCardViewCell
import com.msc.libcommon.viewcardcell.EndCardViewCell
import com.msc.libcommon.viewcardcell.VideoSmallCardViewCell
import com.msc.libcoremodel.datamodel.http.entities.CommonData
import com.msc.libcoremodel.datamodel.http.entities.VideoDownloadData
import com.msc.videoplayer.R
import com.msc.videoplayer.viewmodel.DownloadListViewModel
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import kotlinx.android.synthetic.main.activity_download_list.*
import org.json.JSONArray

@Route(path = ARouterPath.DOWNLOADLIST_ACT)
class DownloadListActivity : BaseActivity(){

    private lateinit var viewModel: DownloadListViewModel
    private lateinit var engine: TangramEngine
    private lateinit var builder: TangramBuilder.InnerBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_download_list)

        viewModel = ViewModelProviders.of(this@DownloadListActivity)
                .get(DownloadListViewModel::class.java)
        viewModel.initData()
        subscribeToModel(viewModel)

        builder = TangramBuilder.newInnerBuilder(this@DownloadListActivity )

        //Step 3: register business cells and cards
        builder.registerCell("download", DownloadCardViewCell::class.java, DownloadCardView::class.java)
        builder.registerCell("end", EndCardViewCell::class.java, EndCardView::class.java)

        engine = builder.build()
        engine.bindView(recyclerView_download_list)
        //Step 6: enable auto load more if your page's data is lazy loaded
        engine.enableAutoLoadMore(false)
        engine.addSimpleClickSupport(viewModel.listener)


        tv_edit.setOnClickListener(viewModel)
        tv_delete_all.setOnClickListener(viewModel)
        tv_delete.setOnClickListener(viewModel)
    }

    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: DownloadListViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData.observe(this, Observer<List<VideoDownloadData>> { list ->
            Logger.d("subscribeToModel onChanged onChanged")
            if(list!=null) {
                val data = Gson().toJson(list)
                val s = JSONArray(data)
                engine.setData(s)
            }
        })
        model.liveObservableEditModeData.observe(this, Observer<Boolean> { isEditMode ->
            Logger.d("subscribeToModel onChanged onChanged")
            if(isEditMode!!) {
                tv_edit.text = "取消"
                tv_delete_all.visibility = View.VISIBLE
                tv_delete.visibility = View.VISIBLE
            } else {
                tv_edit.text = "编辑"
                tv_delete_all.visibility = View.GONE
                tv_delete.visibility = View.GONE
            }
        })
    }

}
