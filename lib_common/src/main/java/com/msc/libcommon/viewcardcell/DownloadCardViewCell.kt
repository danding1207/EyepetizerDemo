package com.msc.libcommon.viewcardcell

import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.SpeedCalculator
import com.liulishuo.okdownload.StatusUtil
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.core.breakpoint.BlockInfo
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend
import com.msc.libcommon.R
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcommon.util.DensityUtil
import com.msc.libcommon.util.FileUtil
import com.msc.libcommon.util.StringUtils
import com.msc.libcommon.viewcard.DownloadCardView
import com.msc.libcoremodel.datamodel.http.entities.VideoDownloadData

class DownloadCardViewCell : CommenDataCell<DownloadCardView, VideoDownloadData>() {

    private var task: DownloadTask? = null

    override fun bindView(view: DownloadCardView) {
        super.bindView(view)

        view.tvTitle!!.text = mData!!.fileName
         when (mData!!.downloadStatus) {
            StatusUtil.Status.COMPLETED -> {
                view.tvStatus!!.text ="已完成"
                view.ivAction!!.visibility = View.INVISIBLE
            }
            StatusUtil.Status.RUNNING, StatusUtil.Status.PENDING -> {
                view.tvStatus!!.text ="缓存中 / ${mData!!.totalOffset * 100 / mData!!.totalLength}"
                view.ivAction!!.visibility = View.VISIBLE
                view.ivAction!!.setImageResource(R.drawable.action_pause)
            }
            StatusUtil.Status.IDLE -> {
                view.tvStatus!!.text ="缓存暂停 / ${mData!!.totalOffset * 100 / mData!!.totalLength}"
                view.ivAction!!.visibility = View.VISIBLE
                view.ivAction!!.setImageResource(R.drawable.action_start)
            }
            else -> {
                view.tvStatus!!.text ="缓存暂停 / ${mData!!.totalOffset * 100 / mData!!.totalLength}"
                view.ivAction!!.visibility = View.VISIBLE
                view.ivAction!!.setImageResource(R.drawable.action_reload)
            }
        }

        Glide.with(view.context!!).load(mData!!.cover!!).into(view.ivSmallCover!!)

        val resources = view.context.resources
        val dm = resources.displayMetrics
        val width = dm.widthPixels
        val layoutParamsCardView: ConstraintLayout.LayoutParams = view.cardViewCover!!.layoutParams as ConstraintLayout.LayoutParams
        layoutParamsCardView.width = (width - DensityUtil.dp2px(view.context, 24f)) / 2
        layoutParamsCardView.height = layoutParamsCardView.width * 777 / 1242
        view.cardViewCover!!.layoutParams = layoutParamsCardView

        view.tvTime!!.text = StringUtils.durationToString(mData!!.duration)

        val filename = "${mData!!.fileId}.mp4"

        val url = mData!!.playUrl!!

        task = DownloadTask
                .Builder(url, FileUtil.getVideoFile(view.context!!))
                .setFilename(filename)
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(200)
                // ignore the same task has already completed in the past.
                .setPassIfAlreadyCompleted(true)
                .build()

        val status = StatusUtil.getStatus(task!!)

        if (status == StatusUtil.Status.PENDING || status == StatusUtil.Status.RUNNING) {

            task!!.enqueue(object : DownloadListener4WithSpeed() {
                private var totalLength: Long = 0
                private var readableTotalLength: String? = null

                override fun taskStart(task: DownloadTask) {
                }

                override fun infoReady(task: DownloadTask, info: BreakpointInfo,
                                       fromBreakpoint: Boolean,
                                       model: Listener4SpeedAssistExtend.Listener4SpeedModel) {
                    totalLength = info.totalLength
                    readableTotalLength = Util.humanReadableBytes(totalLength, true)
                    view.tvStatus!!.text = "缓存中 / ${info.totalOffset * 100 / totalLength}"
                }

                override fun connectStart(task: DownloadTask, blockIndex: Int,
                                          requestHeaders: Map<String, List<String>>) {
                }

                override fun connectEnd(task: DownloadTask, blockIndex: Int, responseCode: Int,
                                        responseHeaders: Map<String, List<String>>) {
                }

                override fun progressBlock(task: DownloadTask, blockIndex: Int,
                                           currentBlockOffset: Long,
                                           blockSpeed: SpeedCalculator) {
                }

                override fun progress(task: DownloadTask, currentOffset: Long,
                                      taskSpeed: SpeedCalculator) {
//                    val readableOffset = Util.humanReadableBytes(currentOffset, true)
//                    val progressStatus = "$readableOffset/$readableTotalLength"
//                    val speed = taskSpeed.speed()
//                    val progressStatusWithSpeed = "$progressStatus($speed)"
                    view.tvStatus!!.text = "缓存中 / ${currentOffset * 100 / totalLength}"
                }

                override fun blockEnd(task: DownloadTask, blockIndex: Int, info: BlockInfo,
                                      blockSpeed: SpeedCalculator) {
                }

                override fun taskEnd(task: DownloadTask, cause: EndCause,
                                     realCause: Exception?,
                                     taskSpeed: SpeedCalculator) {
//                    val statusWithSpeed = cause.toString() + " " + taskSpeed.averageSpeed()

                    // mark
                    task.tag = null
                    if (cause == EndCause.COMPLETED) {
                        view.tvStatus!!.text = "已完成"
                    }
                }
            })

        }

    }

}