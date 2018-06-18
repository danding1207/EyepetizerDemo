package com.msc.videoplayer

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.annotation.Nullable
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.view.View

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper
import com.google.android.exoplayer2.offline.FilteringManifestParser
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser
import com.google.android.exoplayer2.source.dash.manifest.RepresentationKey
import com.google.android.exoplayer2.source.dash.offline.DashDownloadAction
import com.google.android.exoplayer2.source.hls.offline.HlsDownloadAction
import com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.DebugTextViewHelper
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.msc.libcommon.base.ARouterPath
import com.msc.libcommon.base.BaseActivity
import com.msc.mmdemo.Utils.DensityUtil
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_video_player.*
import java.io.File

import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

@Route(path = ARouterPath.VIDEO_PLAYER_ACT)
class VideoPlayerActivity : BaseActivity(), View.OnClickListener, PlaybackPreparer, PlayerControlView.VisibilityListener {

    private var mediaDataSourceFactory: DataSource.Factory? = null
    private var player: SimpleExoPlayer? = null
    private val mediaSource: MediaSource? = null
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarDarkMode(this, ContextCompat.getColor(this, R.color.colorPrimary))
        videoUri = intent.extras.getString("videoUri")
        Logger.d("videoUri--->$videoUri")

        mediaDataSourceFactory = buildDataSourceFactory(true)
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER)
        }

        setContentView(R.layout.activity_video_player)

        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS)
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY)
            startWindow = savedInstanceState.getInt(KEY_WINDOW)
            startPosition = savedInstanceState.getLong(KEY_POSITION)
        } else {
            trackSelectorParameters = DefaultTrackSelector.ParametersBuilder().build()
            clearStartPosition()
        }

        // step1. 创建一个默认的TrackSelector
        val mainHandler = Handler()

        // 创建带宽
        val bandwidthMeter = DefaultBandwidthMeter()

        // 创建轨道选择工厂
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        // 创建轨道选择器实例
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        //step2. 创建播放器
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        player!!.playWhenReady = true

        val resources = resources
        val dm = resources.displayMetrics
        val width = dm.widthPixels
        val layoutParamsCardView: ConstraintLayout.LayoutParams = playerView!!.layoutParams as ConstraintLayout.LayoutParams
        layoutParamsCardView.width = width
        layoutParamsCardView.height = layoutParamsCardView.width * 720 / 1280
        playerView.layoutParams = layoutParamsCardView

        playerView.player = player
        playerView.setPlaybackPreparer(this)

        // 创建加载数据的工厂
        val dataSourceFactory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "com.msc.eyepetizer"))

        // 创建解析数据的工厂
        val extractorsFactory = DefaultExtractorsFactory()

        // 传入Uri、加载数据的工厂、解析数据的工厂，就能创建出MediaSource
        val videoSource = ExtractorMediaSource(Uri.parse(videoUri),
                dataSourceFactory, extractorsFactory, null, null)

        // Prepare
        player!!.prepare(videoSource)

    }

    override fun onClick(v: View) {

    }

    // PlaybackControlView.PlaybackPreparer implementation
    override fun preparePlayback() {
        initializePlayer()
    }

    // PlaybackControlView.VisibilityListener implementation
    override fun onVisibilityChange(visibility: Int) {
//        debugRootView.setVisibility(visibility)
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {

    }

    override fun onNewIntent(intent: Intent) {
        releasePlayer()
        clearStartPosition()
        setIntent(intent)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
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
        releaseAdsLoader()
    }

    fun releasePlayer() {
        if (player != null) {
//      debugViewHelper.stop()
//      debugViewHelper = null
            player!!.release()
            player = null
//      mediaSource = null
//      trackSelector = null
        }
    }

    fun clearStartPosition() {
        startAutoPlay = true
        startWindow = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }

    fun initializePlayer() {
        if (player == null) {
            val action = intent.action
            var uri: Uri
            if (ACTION_VIEW == action) {
                uri = Uri.parse(intent.extras.getString("videoUri"))
            } else {
//        showToast(getString(R.string.unexpected_intent_action, action));
                finish()
                return
            }

            if (Util.maybeRequestReadExternalStoragePermission(this, uri)) {
                // The player will be reinitialized if the permission is granted.
                return
            }

//            var drmSessionManager: DefaultDrmSessionManager<FrameworkMediaCrypto>? = null
//            if (intent.hasExtra(DRM_SCHEME_EXTRA) || intent.hasExtra(DRM_SCHEME_UUID_EXTRA)) {
//                String drmLicenseUrl = intent . getStringExtra (DRM_LICENSE_URL_EXTRA);
//                String[] keyRequestPropertiesArray =
//                intent.getStringArrayExtra(DRM_KEY_REQUEST_PROPERTIES_EXTRA);
//                boolean multiSession = intent . getBooleanExtra (DRM_MULTI_SESSION_EXTRA, false);
//                int errorStringId = R . string . error_drm_unknown;
//                if (Util.SDK_INT < 18) {
//                    errorStringId = R.string.error_drm_not_supported;
//                } else {
//                    try {
//                        String drmSchemeExtra = intent . hasExtra (DRM_SCHEME_EXTRA) ? DRM_SCHEME_EXTRA
//                        : DRM_SCHEME_UUID_EXTRA;
//                        UUID drmSchemeUuid = Util . getDrmUuid (intent.getStringExtra(drmSchemeExtra));
//                        if (drmSchemeUuid == null) {
//                            errorStringId = R.string.error_drm_unsupported_scheme;
//                        } else {
//                            drmSessionManager =
//                                    buildDrmSessionManagerV18(
//                                            drmSchemeUuid, drmLicenseUrl, keyRequestPropertiesArray, multiSession);
//                        }
//                    } catch (UnsupportedDrmException e) {
//                        errorStringId = e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
//                        ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown;
//                    }
//                }
//                if (drmSessionManager == null) {
//                    showToast(errorStringId);
//                    finish();
//                    return;
//                }
//            }

            var trackSelectionFactory: TrackSelection.Factory
            var abrAlgorithm = intent.getStringExtra(ABR_ALGORITHM_EXTRA)
            trackSelectionFactory = if (abrAlgorithm == null || ABR_ALGORITHM_DEFAULT.equals(abrAlgorithm)) {
                AdaptiveTrackSelection.Factory(BANDWIDTH_METER)
            } else if (ABR_ALGORITHM_RANDOM.equals(abrAlgorithm)) {
                RandomTrackSelection.Factory()
            } else {
                //                showToast(R.string.error_unrecognized_abr_algorithm)
                finish()
                return
            }

            var preferExtensionDecoders = intent.getBooleanExtra(PREFER_EXTENSION_DECODERS_EXTRA, false)

            @DefaultRenderersFactory.ExtensionRendererMode
            var extensionRendererMode =
                    if (useExtensionRenderers())
                        (if (preferExtensionDecoders) DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                    else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF

            var renderersFactory = DefaultRenderersFactory(this, extensionRendererMode)

            trackSelector = DefaultTrackSelector(trackSelectionFactory)
            trackSelector!!.parameters = trackSelectorParameters
            lastSeenTrackGroupArray = null



            player =
                    ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector)
            player!!.addListener(PlayerEventListener())
            player!!.playWhenReady = startAutoPlay
            player!!.addAnalyticsListener(EventLogger(trackSelector))
            playerView.setPlayer(player)
            playerView.setPlaybackPreparer(this)
            debugViewHelper = DebugTextViewHelper(player, debug_text_view)
            debugViewHelper!!.start()


            MediaSource[] mediaSources = new MediaSource[uris.length];
            for (int i = 0; i < uris.length; i++) {
                mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
            }


            mediaSource =
                    mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);
            String adTagUriString = intent . getStringExtra (AD_TAG_URI_EXTRA);
            if (adTagUriString != null) {
                Uri adTagUri = Uri . parse (adTagUriString);
                if (!adTagUri.equals(loadedAdTagUri)) {
                    releaseAdsLoader();
                    loadedAdTagUri = adTagUri;
                }
                MediaSource adsMediaSource = createAdsMediaSource (mediaSource, Uri.parse(adTagUriString));
                if (adsMediaSource != null) {
                    mediaSource = adsMediaSource;
                } else {
                    showToast(R.string.ima_not_loaded);
                }
            } else {
                releaseAdsLoader();
            }
        }
        boolean haveStartPosition = startWindow != C . INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(startWindow, startPosition);
        }
        player.prepare(mediaSource, !haveStartPosition, false);
        updateButtonVisibilities();
    }

    companion object {


        private val BANDWIDTH_METER = DefaultBandwidthMeter()
        private val DEFAULT_COOKIE_MANAGER: CookieManager

        init {
            DEFAULT_COOKIE_MANAGER = CookieManager()
            DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        }
    }


    /** Returns a [DataSource.Factory].  */
    fun buildDataSourceFactory(listener: TransferListener<in DataSource>?): DataSource.Factory {
        val upstreamFactory = DefaultDataSourceFactory(this, listener, buildHttpDataSourceFactory(listener))
        return buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache())
    }

    /** Returns a {@link HttpDataSource.Factory}. */
    fun buildHttpDataSourceFactory(listener: TransferListener<in DataSource>?): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(userAgent, listener)
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *     DataSource factory.
     * @return A new DataSource factory.
     */
    fun buildDataSourceFactory(useBandwidthMeter: Boolean): DataSource.Factory {
        return buildDataSourceFactory(if (useBandwidthMeter) BANDWIDTH_METER else null)
    }

    fun buildReadOnlyCacheDataSource(upstreamFactory: DefaultDataSourceFactory, cache: Cache): CacheDataSourceFactory {
        return CacheDataSourceFactory(
                cache,
                upstreamFactory,
                FileDataSourceFactory(),
                /* cacheWriteDataSinkFactory= */ null,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                /* eventListener= */ null)
    }

    /**
     * 获取缓存对象
     */
    @Synchronized
    fun getDownloadCache(): Cache {
        if (downloadCache == null) {
            val downloadContentDirectory = File(getDownloadDirectory(), DOWNLOAD_CONTENT_DIRECTORY)
            downloadCache = SimpleCache(downloadContentDirectory, NoOpCacheEvictor())
        }
        return downloadCache!!
    }

    /**
     * 获取缓存下载目录
     */
    fun getDownloadDirectory(): File {
        if (downloadDirectory == null) {
            downloadDirectory = getExternalFilesDir(null)
            if (downloadDirectory == null) {
                downloadDirectory = filesDir
            }
        }
        return downloadDirectory!!
    }

    protected var userAgent: String? = null

    private var downloadDirectory: File? = null
    private var downloadCache: Cache? = null
    private var downloadManager: DownloadManager? = null
//    private val downloadTracker: DownloadTracker? = null

    private val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
    private val MAX_SIMULTANEOUS_DOWNLOADS = 2
    private val DOWNLOAD_ACTION_FILE = "actions"

    private val DOWNLOAD_DESERIALIZERS =
            arrayOf(
                    DashDownloadAction.DESERIALIZER,
                    HlsDownloadAction.DESERIALIZER,
                    SsDownloadAction.DESERIALIZER,
                    ProgressiveDownloadAction.DESERIALIZER
            )

    /** Returns whether extension renderers should be used.  */
    fun useExtensionRenderers(): Boolean {
        return "withExtensions" == BuildConfig.FLAVOR
    }

    fun buildMediaSource(uri: Uri): MediaSource {
        return buildMediaSource(uri, null)
    }

    @SuppressWarnings("unchecked")
    fun buildMediaSource(uri: Uri, @Nullable overrideExtension: String): MediaSource {

        @C.ContentType
        var type = Util.inferContentType(uri, overrideExtension)

        when (type) {
            C.TYPE_DASH ->
                return DashMediaSource.Factory(
                        DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .setManifestParser(
                                FilteringManifestParser<>(
                                        DashManifestParser(), (List<RepresentationKey>) getOfflineStreamKeys (uri)))
                        .createMediaSource(uri)


            case C . TYPE_SS :
                return new SsMediaSource . Factory (
                    new DefaultSsChunkSource . Factory (mediaDataSourceFactory)
                ,
            buildDataSourceFactory(false))
                .setManifestParser(
                    new FilteringManifestParser < > (
                            new SsManifestParser (), (List<StreamKey>) getOfflineStreamKeys (uri))
                )
                .createMediaSource(uri);
            case C . TYPE_HLS :
                return new HlsMediaSource . Factory (mediaDataSourceFactory)
                    .setPlaylistParser(
                            new FilteringManifestParser < > (
                                    new HlsPlaylistParser (), (List<RenditionKey>) getOfflineStreamKeys (uri))
                )
                .createMediaSource(uri);
            case C . TYPE_OTHER :
                return new ExtractorMediaSource . Factory (mediaDataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException ("Unsupported type: " + type);
            }
        }
    }

    fun getOfflineStreamKeys(uri: Uri): List<?> {
        return getDownloadTracker().getOfflineStreamKeys(uri)
    }


//    fun getDownloadTracker(): DownloadTracker {
//        initDownloadManager()
//        return downloadTracker
//    }

    @Synchronized
    fun initDownloadManager() {
        if (downloadManager == null) {
            var downloaderConstructorHelper =
                    DownloaderConstructorHelper(
                            getDownloadCache(), buildHttpDataSourceFactory(/* listener= */ null))

            downloadManager =
                    DownloadManager(
                            downloaderConstructorHelper,
                            MAX_SIMULTANEOUS_DOWNLOADS,
                            DownloadManager.DEFAULT_MIN_RETRY_COUNT,
                            File(getDownloadDirectory(), DOWNLOAD_ACTION_FILE),
                            DashDownloadAction.DESERIALIZER,
                            HlsDownloadAction.DESERIALIZER,
                            SsDownloadAction.DESERIALIZER,
                            ProgressiveDownloadAction.DESERIALIZER)

//            downloadTracker =
//                    DownloadTracker(
//                            /* context= */ this,
//                            buildDataSourceFactory(/* listener= */ null),
//                            File(getDownloadDirectory(), DOWNLOAD_TRACKER_ACTION_FILE),
//                            DOWNLOAD_DESERIALIZERS)
//            downloadManager!!.addListener(downloadTracker)
        }
    }

}
