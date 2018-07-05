package com.msc.modulehomepage.viewcardcell

import android.net.Uri
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.msc.libcommon.util.StringUtils
import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.msc.libcommon.base.CommenDataCell
import com.msc.libcoremodel.datamodel.http.entities.CommonData
import com.msc.modulehomepage.viewcard.AutoPlayFollowCardView

class AutoPlayFollowCardViewCell : CommenDataCell<AutoPlayFollowCardView, CommonData.CommonItemList>() {

    override fun bindView(view: AutoPlayFollowCardView) {
        super.bindView(view)
        view.tvName!!.text = mData!!.data!!.content!!.data!!.owner!!.nickname
        view.tvDescription!!.text = mData!!.data!!.content!!.data!!.description
        view.tvDate!!.text = StringUtils.getStringDate(mData!!.data!!.content!!.data!!.updateTime)
        view.tvLikeNum!!.text = mData!!.data!!.content!!.data!!.consumption!!.collectionCount.toString()
        view.tvMessageNum!!.text = mData!!.data!!.content!!.data!!.consumption!!.replyCount.toString()
        Glide.with(view.context!!).load(mData!!.data!!.content!!.data!!.owner!!.avatar).into(view.ivAuthorCover!!)

        // 1. Create a default TrackSelector
        val bandwidthMeter = DefaultBandwidthMeter()

        val trackSelector: TrackSelector =
                DefaultTrackSelector(bandwidthMeter)

        // 2. Create a default LoadControl
        val loadControl = DefaultLoadControl()

        // 3. Create the player
        val player =
                ExoPlayerFactory.newSimpleInstance(view.context, trackSelector, loadControl)

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(view.context,
                Util.getUserAgent(view.context, "com.msc.eyepetizer"),
                bandwidthMeter)
        // Produces Extractor instances for parsing the media data.
        val extractorsFactory = DefaultExtractorsFactory()
        // This is the MediaSource representing the media to be played.
        val mediaSource = ExtractorMediaSource(Uri.parse(mData!!.data!!.content!!.data!!.playUrl),
                dataSourceFactory, extractorsFactory, null, null)

        player!!.playWhenReady = true
        player.setVideoTextureView(view.textureView)
        player.prepare(mediaSource, false, false)
    }

}
