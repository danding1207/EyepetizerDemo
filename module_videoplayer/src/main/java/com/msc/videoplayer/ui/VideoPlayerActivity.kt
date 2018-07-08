package com.msc.videoplayer.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.offline.FilteringManifestParser
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser
import com.google.android.exoplayer2.source.dash.manifest.RepresentationKey
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser
import com.google.android.exoplayer2.source.hls.playlist.RenditionKey
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser
import com.google.android.exoplayer2.source.smoothstreaming.manifest.StreamKey
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.DebugTextViewHelper
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import com.msc.libcommon.util.DensityUtil
import com.msc.libcommon.viewcard.EndCardView
import com.msc.libcommon.viewcard.TextCardView
import com.msc.libcommon.viewcard.VideoHeaderCardView
import com.msc.libcommon.viewcard.VideoSmallCardView
import com.msc.libcommon.viewcardcell.EndCardViewCell
import com.msc.libcommon.viewcardcell.TextCardViewCell
import com.msc.libcommon.viewcardcell.VideoHeaderCardViewCell
import com.msc.libcommon.viewcardcell.VideoSmallCardViewCell
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcoremodel.datamodel.http.entities.CommonData
import com.msc.libcoremodel.datamodel.http.entities.VideoRelatedData
import com.msc.videoplayer.utils.DownloadManagerInitor
import com.msc.videoplayer.R
import com.msc.videoplayer.viewmodel.VideoPlayerViewModel
import com.orhanobut.logger.Logger
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.SimpleClickSupport
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video_player.*
import org.json.JSONArray
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

@Route(path = ARouterPath.VIDEO_PLAYER_ACT)
class VideoPlayerActivity : BaseActivity(), View.OnClickListener, PlaybackPreparer, PlayerControlView.VisibilityListener {

    private var mediaDataSourceFactory: DataSource.Factory? = null
    private var player: SimpleExoPlayer? = null
    private var mediaSource: MediaSource? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var trackSelectorParameters: DefaultTrackSelector.Parameters? = null
    private var debugViewHelper: DebugTextViewHelper? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null

    // Saved instance state keys.
    private val KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters"
    private val KEY_WINDOW = "window"
    private val KEY_POSITION = "position"
    private val KEY_AUTO_PLAY = "auto_play"

    private var startAutoPlay: Boolean = false
    private var startWindow: Int = 0
    private var startPosition: Long = 0

    // ABR算法
    val ABR_ALGORITHM_EXTRA = "abr_algorithm"
    private val ABR_ALGORITHM_DEFAULT = "default"
    private val ABR_ALGORITHM_RANDOM = "random"

    val PREFER_EXTENSION_DECODERS_EXTRA = "prefer_extension_decoders"

    private lateinit var viewModel: VideoPlayerViewModel
    private lateinit var engine: TangramEngine
    private lateinit var builder: TangramBuilder.InnerBuilder
    private lateinit var item :CommonData.CommonItemList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarDarkMode(this, ContextCompat.getColor(this, R.color.colorPrimary))
        setContentView(R.layout.activity_video_player)
        keepScreenLongLight()
        setFitsSystemWindows(this, true)
        rootView = constraintLayout

        item = Gson().fromJson<CommonData.CommonItemList>(
                intent.extras.getString("data"),
                CommonData.CommonItemList::class.java)

        viewModel = ViewModelProviders.of(this@VideoPlayerActivity)
                .get(VideoPlayerViewModel::class.java)
        viewModel.initData(item.data!!.id.toString())

        viewModel.item = item

        subscribeToModel(viewModel)

        builder = TangramBuilder.newInnerBuilder(this@VideoPlayerActivity )

        //Step 3: register business cells and cards

        builder.registerCell("textCard", TextCardViewCell::class.java, TextCardView::class.java)
        builder.registerCell("videoSmallCard", VideoSmallCardViewCell::class.java, VideoSmallCardView::class.java)
        builder.registerCell("end", EndCardViewCell::class.java, EndCardView::class.java)
        builder.registerCell("header", VideoHeaderCardViewCell::class.java, VideoHeaderCardView::class.java)

        engine = builder.build()
        engine.bindView(recyclerView)
        //Step 6: enable auto load more if your page's data is lazy loaded
        engine.enableAutoLoadMore(false)
        engine.addSimpleClickSupport(viewModel.listener)


        downloadManagerInitor = DownloadManagerInitor.getInstance(this)

        mediaDataSourceFactory = buildDataSourceFactory(true)
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER)
        }

        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS)
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY)
            startWindow = savedInstanceState.getInt(KEY_WINDOW)
            startPosition = savedInstanceState.getLong(KEY_POSITION)
        } else {
            trackSelectorParameters = DefaultTrackSelector.ParametersBuilder().build()
            clearStartPosition()
        }

        val width = DensityUtil.getScreenWidth(this)
        val height = width * 720 / 1280
        val layoutParamsCardView: ConstraintLayout.LayoutParams = playerControlView!!.layoutParams as ConstraintLayout.LayoutParams
        layoutParamsCardView.width = width
        layoutParamsCardView.height = height
        playerControlView.layoutParams = layoutParamsCardView

        playerControlView.bindActivty(constraintLayout, this)

    }

    override fun onClick(v: View) {

    }

    // PlaybackControlView.PlaybackPreparer implementation
    override fun preparePlayback() {
        Logger.d("mediaDataSourceFactory    preparePlayback")

        initializePlayer()
    }

    // PlaybackControlView.VisibilityListener implementation
    override fun onVisibilityChange(visibility: Int) {
//        debugRootView.setVisibility(visibility)
        Logger.d("mediaDataSourceFactory    onVisibilityChange")

    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {
        Logger.d("mediaDataSourceFactory    onPointerCaptureChanged")

    }

    override fun onNewIntent(intent: Intent) {
        releasePlayer()
        clearStartPosition()
        setIntent(intent)

        Logger.d("mediaDataSourceFactory    onNewIntent")


    }

    override fun onStart() {
        super.onStart()
        Logger.d("mediaDataSourceFactory    onStart")

        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.d("mediaDataSourceFactory    onResume")

        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun releasePlayer() {
        if (player != null) {
//            debugViewHelper!!.stop()
//            debugViewHelper = null
            player!!.release()
            player = null
            mediaSource = null
            trackSelector = null
        }
    }

    fun clearStartPosition() {
        startAutoPlay = true
        startWindow = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }

    fun initializePlayer() {
        if (player == null) {

            Logger.d("mediaDataSourceFactory    initializePlayer")
            val uri: Uri = if(item.data!!.playUrl!=null) {
                Uri.parse(item.data!!.playUrl)
            } else if (item.data!!.content!=null
                    && item.data!!.content!!.data!=null
                    && item.data!!.content!!.data!!.playUrl!=null) {
                Uri.parse(item.data!!.content!!.data!!.playUrl)
            } else {
                Snackbar.make(rootView!!, "播放地址无效", Snackbar.LENGTH_SHORT)
                return
            }

            Logger.d("mediaDataSourceFactory    uri")

            if (Util.maybeRequestReadExternalStoragePermission(this, uri)) {
                // The player will be reinitialized if the permission is granted.
                return
            }

            var trackSelectionFactory: TrackSelection.Factory
            var abrAlgorithm = intent.getStringExtra(ABR_ALGORITHM_EXTRA)
            trackSelectionFactory = if (abrAlgorithm == null || ABR_ALGORITHM_DEFAULT.equals(abrAlgorithm)) {
                AdaptiveTrackSelection.Factory(BANDWIDTH_METER)
            } else if (ABR_ALGORITHM_RANDOM == abrAlgorithm) {
                RandomTrackSelection.Factory()
            } else {
                //                showToast(R.string.error_unrecognized_abr_algorithm)
                finish()
                return
            }

            Logger.d("mediaDataSourceFactory    trackSelectionFactory")


            var preferExtensionDecoders = intent.getBooleanExtra(PREFER_EXTENSION_DECODERS_EXTRA, false)

            @DefaultRenderersFactory.ExtensionRendererMode
            var extensionRendererMode =
                    if (downloadManagerInitor.useExtensionRenderers())
                        (if (preferExtensionDecoders) DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                    else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF

            var renderersFactory = DefaultRenderersFactory(this, extensionRendererMode)

            trackSelector = DefaultTrackSelector(trackSelectionFactory)
            trackSelector!!.parameters = trackSelectorParameters
            lastSeenTrackGroupArray = null

            Logger.d("mediaDataSourceFactory    trackSelector")


            player =
                    ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector)
//            player!!.addListener(PlayerEventListener())
            player!!.playWhenReady = startAutoPlay
            player!!.addAnalyticsListener(EventLogger(trackSelector))
//            playerView.player = player

//            playerControlView.player = player

            playerControlView.player = player

//            playerView.setPlaybackPreparer(this)
//            debugViewHelper = DebugTextViewHelper(player, debug_text_view)
//            debugViewHelper!!.start()


            mediaSource = buildMediaSource(uri)

            Logger.d("mediaDataSourceFactory    mediaSource")

        }

        var haveStartPosition = (startWindow != C.INDEX_UNSET)
        if (haveStartPosition) {
            player!!.seekTo(startWindow, startPosition)
        }
        player!!.prepare(mediaSource, !haveStartPosition, false)
        updateButtonVisibilities()

        Logger.d("mediaDataSourceFactory    player!!.prepare")

    }

    // User controls

    fun updateButtonVisibilities() {
//        controls_root.removeAllViews()
        if (player == null) {
            return
        }

        var mappedTrackInfo: MappingTrackSelector.MappedTrackInfo? = trackSelector!!.currentMappedTrackInfo
                ?: return

        for (i in 0..(mappedTrackInfo!!.rendererCount - 1)) {

            var trackGroups = mappedTrackInfo.getTrackGroups(i)

            if (trackGroups.length != 0) {
                var button = Button(this)
                var label: Int
                when (player!!.getRendererType(i)) {
                    C.TRACK_TYPE_AUDIO ->
                        label = R.string.exo_track_selection_title_audio
                    C.TRACK_TYPE_VIDEO ->
                        label = R.string.exo_track_selection_title_video
                    C.TRACK_TYPE_TEXT ->
                        label = R.string.exo_track_selection_title_text
                    else ->
                        label = R.string.exo_track_selection_title_audio

                }
                button.setText(label)
                button.tag = i
                button.setOnClickListener(this)
//                controls_root.addView(button)
            }
        }
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set [.BANDWIDTH_METER] as a listener to the new
     * DataSource factory.
     * @return A new DataSource factory.
     */
    private fun buildDataSourceFactory(useBandwidthMeter: Boolean): DataSource.Factory {
        return downloadManagerInitor
                .buildDataSourceFactory(if (useBandwidthMeter) BANDWIDTH_METER else null)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return buildMediaSource(uri, null)
    }

    @SuppressWarnings("unchecked")
    private fun buildMediaSource(uri: Uri, @Nullable overrideExtension: String?): MediaSource {
        @C.ContentType
        var type = Util.inferContentType(uri, overrideExtension)

        when (type) {
            C.TYPE_DASH ->
                return DashMediaSource.Factory(
                        DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .setManifestParser(
                                FilteringManifestParser(
                                        DashManifestParser(), downloadManagerInitor.downloadTracker.getOfflineStreamKeys<RepresentationKey>(uri) as (List<RepresentationKey>)))
                        .createMediaSource(uri)
            C.TYPE_SS ->
                return SsMediaSource.Factory(
                        DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .setManifestParser(
                                FilteringManifestParser(
                                        SsManifestParser(), downloadManagerInitor.downloadTracker.getOfflineStreamKeys<StreamKey>(uri) as (List<StreamKey>)))
                        .createMediaSource(uri)
            C.TYPE_HLS ->
                return HlsMediaSource.Factory(mediaDataSourceFactory)
                        .setPlaylistParser(
                                FilteringManifestParser(
                                        HlsPlaylistParser(), downloadManagerInitor.downloadTracker.getOfflineStreamKeys<RenditionKey>(uri) as (List<RenditionKey>)))
                        .createMediaSource(uri)
            C.TYPE_OTHER ->
                return ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            else ->
                throw IllegalStateException("Unsupported type: " + type);

        }


    }

    companion object {

        @JvmStatic
        lateinit var downloadManagerInitor: DownloadManagerInitor
            private set

        private val BANDWIDTH_METER = DefaultBandwidthMeter()
        private val DEFAULT_COOKIE_MANAGER: CookieManager

        init {
            DEFAULT_COOKIE_MANAGER = CookieManager()
            DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        }
    }


    /**
     * 订阅数据变化来刷新UI
     * @param model
     */
    private fun subscribeToModel(model: VideoPlayerViewModel) {
        //观察数据变化来刷新UI
        model.liveObservableData!!.observe(this, Observer<CommonData> { commonData ->
            Logger.d("subscribeToModel onChanged onChanged")
            if(commonData!=null) {
                val data = Gson().toJson(commonData!!.itemList)
                val s = JSONArray(data)
                engine.setData(s)
            }
        })
    }

    class SampleClickSupport : SimpleClickSupport() {
        init {
            setOptimizedMode(true)
        }
        override fun defaultClick(targetView: View?, cell: BaseCell<*>?, eventType: Int) {
            super.defaultClick(targetView, cell, eventType)
//            val mData: AllRecData.ItemListBeanX.DataBeanXX? = Gson().fromJson<AllRecData.ItemListBeanX.DataBeanXX>(cell!!.optStringParam("data"), AllRecData.ItemListBeanX.DataBeanXX::class.java)
//            when (cell.stringType) {
//                "followCard", "autoPlayFollowCard", "pictureFollowCard" -> {
//                    when (mData!!.content!!.type) {
//                        "video" -> {
//                            if(mData.content!!.data!!.playInfo==null) {
//                                ARouter.getInstance()
//                                        .build(ARouterPath.VIDEO_PLAYER_ACT)
//                                        .withString("videoUri", mData.content!!.data!!.playUrl)
//                                        .navigation()
//                            } else {
//                                Observable.fromIterable(mData.content!!.data!!.playInfo)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .filter {
//                                            return@filter "high" == it.type && it.url != null
//                                        }
//                                        .subscribe { it ->
//                                            ARouter.getInstance()
//                                                    .build(ARouterPath.VIDEO_PLAYER_ACT)
//                                                    .withString("videoUri", it.url)
//                                                    .navigation()
//                                        }
//                            }
//
//                        }
//                        "ugcPicture" -> {
//                            ARouter.getInstance()
//                                    .build(ARouterPath.PICTURE_DETAIL_ACT)
//                                    .withString("avatarUrl", mData.content!!.data!!.owner!!.avatar)
//                                    .withString("nickname", mData.content!!.data!!.owner!!.nickname)
//                                    .withString("description", mData.content!!.data!!.description)
//                                    .withString("collectionCount", mData.content!!.data!!.consumption!!.collectionCount.toString())
//                                    .withString("shareCount", mData.content!!.data!!.consumption!!.shareCount.toString())
//                                    .withString("replyCount", mData.content!!.data!!.consumption!!.replyCount.toString())
//                                    .withString("pictureUrl", mData.content!!.data!!.url)
//                                    .navigation()
//                        }
//                    }
//
//                }
//                "videoSmallCard" -> {
//                    if (mData !== null && mData.resourceType != null && "video" == mData.resourceType) {
//                        Observable.fromIterable(mData.playInfo)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .filter {
//                                    return@filter "high" == it.type && it.url != null
//                                }
//                                .subscribe { it ->
//                                    ARouter.getInstance()
//                                            .build(ARouterPath.VIDEO_PLAYER_ACT)
//                                            .withString("videoUri", it.url)
//                                            .navigation()
//                                }
//                    }
//                }
//            }
        }
    }

}
