package com.msc.libcoremodel.datamodel.http.entities

class ConfigsData {

    var autoCache: AutoCacheBean? = null
    var startPageAd: StartPageAdBean? = null
    var videoAdsConfig: VideoAdsConfigBean? = null
    var startPage: StartPageBean? = null
    var log: LogBean? = null
    var issueCover: IssueCoverBean? = null
    var startPageVideo: StartPageVideoBean? = null
    var consumption: ConsumptionBean? = null
    var launch: LaunchBean? = null
    var preload: PreloadBean? = null
    var version: String? = null
    var push: PushBean? = null
    var androidPlayer: AndroidPlayerBean? = null
    var profilePageAd: ProfilePageAdBean? = null
    var firstLaunch: FirstLaunchBean? = null
    var shareTitle: ShareTitleBean? = null
    var campaignInDetail: CampaignInDetailBean? = null
    var campaignInFeed: CampaignInFeedBean? = null
    var reply: ReplyBean? = null
    var startPageVideoConfig: StartPageVideoConfigBean? = null
    var pushInfo: PushInfoBean? = null
    var homepage: HomepageBean? = null

    class AutoCacheBean {

        var isForceOff: Boolean = false
        var videoNum: Int = 0
        var version: String? = null
        var offset: Int = 0

        override fun toString(): String {
            return "AutoCacheBean(isForceOff=$isForceOff, videoNum=$videoNum, version=$version, offset=$offset)"
        }

    }

    class StartPageAdBean {

        var displayTimeDuration: Int = 0
        var isShowBottomBar: Boolean = false
        var isCountdown: Boolean = false
        var actionUrl: String? = null
        var blurredImageUrl: String? = null
        var isCanSkip: Boolean = false
        var version: String? = null
        var imageUrl: String? = null
        var displayCount: Int = 0
        var startTime: Long = 0
        var endTime: Long = 0
        var newImageUrl: String? = null
        var adTrack: List<*>? = null
        override fun toString(): String {
            return "StartPageAdBean(displayTimeDuration=$displayTimeDuration, isShowBottomBar=$isShowBottomBar, isCountdown=$isCountdown, actionUrl=$actionUrl, blurredImageUrl=$blurredImageUrl, isCanSkip=$isCanSkip, version=$version, imageUrl=$imageUrl, displayCount=$displayCount, startTime=$startTime, endTime=$endTime, newImageUrl=$newImageUrl, adTrack=$adTrack)"
        }
    }

    class VideoAdsConfigBean {


        var version: String? = null
        var list: List<ListBean>? = null

        class ListBean {

            var id: Int = 0
            var icon: String? = null
            var title: String? = null
            var description: String? = null
            var url: String? = null
            var actionUrl: String? = null
            var imageUrl: String? = null
            var displayTimeDuration: Int = 0
            var displayCount: Int = 0
            var isShowImage: Boolean = false
            var showImageTime: Int = 0
            var loadingMode: Int = 0
            var isCountdown: Boolean = false
            var isCanSkip: Boolean = false
            var timeBeforeSkip: Int = 0
            var isShowActionButton: Boolean = false
            var videoType: String? = null
            var videoAdType: String? = null
            var categoryId: Int = 0
            var position: Int = 0
            var startTime: Long = 0
            var endTime: Long = 0
            var status: String? = null
            var adTrack: List<*>? = null
            override fun toString(): String {
                return "ListBean(id=$id, icon=$icon, title=$title, description=$description, url=$url, actionUrl=$actionUrl, imageUrl=$imageUrl, displayTimeDuration=$displayTimeDuration, displayCount=$displayCount, isShowImage=$isShowImage, showImageTime=$showImageTime, loadingMode=$loadingMode, isCountdown=$isCountdown, isCanSkip=$isCanSkip, timeBeforeSkip=$timeBeforeSkip, isShowActionButton=$isShowActionButton, videoType=$videoType, videoAdType=$videoAdType, categoryId=$categoryId, position=$position, startTime=$startTime, endTime=$endTime, status=$status, adTrack=$adTrack)"
            }
        }

        override fun toString(): String {
            return "VideoAdsConfigBean(version=$version, list=$list)"
        }
    }

    class StartPageBean {

        var imageUrl: String? = null
        var actionUrl: String? = null
        var version: String? = null
        override fun toString(): String {
            return "StartPageBean(imageUrl=$imageUrl, actionUrl=$actionUrl, version=$version)"
        }
    }

    class LogBean {

        var isPlayLog: Boolean = false
        var version: String? = null
        override fun toString(): String {
            return "LogBean(isPlayLog=$isPlayLog, version=$version)"
        }
    }

    class IssueCoverBean {

        var issueLogo: IssueLogoBean? = null
        var version: String? = null

        class IssueLogoBean {
            var weekendExtra: String? = null
            var isAdapter: Boolean = false
            override fun toString(): String {
                return "IssueLogoBean(weekendExtra=$weekendExtra, isAdapter=$isAdapter)"
            }
        }

        override fun toString(): String {
            return "IssueCoverBean(issueLogo=$issueLogo, version=$version)"
        }
    }

    class StartPageVideoBean {

        var displayTimeDuration: Int = 0
        var showImageTime: Int = 0
        var actionUrl: String? = null
        var isCountdown: Boolean = false
        var isShowImage: Boolean = false
        var isCanSkip: Boolean = false
        var version: String? = null
        var url: String? = null
        var loadingMode: Int = 0
        var displayCount: Int = 0
        var startTime: Long = 0
        var endTime: Long = 0
        var timeBeforeSkip: Int = 0
        var adTrack: List<*>? = null
        override fun toString(): String {
            return "StartPageVideoBean(displayTimeDuration=$displayTimeDuration, showImageTime=$showImageTime, actionUrl=$actionUrl, isCountdown=$isCountdown, isShowImage=$isShowImage, isCanSkip=$isCanSkip, version=$version, url=$url, loadingMode=$loadingMode, displayCount=$displayCount, startTime=$startTime, endTime=$endTime, timeBeforeSkip=$timeBeforeSkip, adTrack=$adTrack)"
        }
    }

    class ConsumptionBean {

        var isDisplay: Boolean = false
        var version: String? = null
        override fun toString(): String {
            return "ConsumptionBean(isDisplay=$isDisplay, version=$version)"
        }
    }

    class LaunchBean {

        var version: String? = null
        var adTrack: List<*>? = null
        override fun toString(): String {
            return "LaunchBean(version=$version, adTrack=$adTrack)"
        }
    }

    class PreloadBean {

        var version: String? = null
        var isOn: Boolean = false
        override fun toString(): String {
            return "PreloadBean(version=$version, isOn=$isOn)"
        }
    }

    class PushBean {

        var startTime: Int = 0
        var endTime: Int = 0
        var message: String? = null
        var version: String? = null
        override fun toString(): String {
            return "PushBean(startTime=$startTime, endTime=$endTime, message=$message, version=$version)"
        }
    }

    class AndroidPlayerBean {

        var version: String? = null
        var playerList: List<String>? = null
        override fun toString(): String {
            return "AndroidPlayerBean(version=$version, playerList=$playerList)"
        }
    }

    class ProfilePageAdBean {

        var imageUrl: String? = null
        var actionUrl: String? = null
        var startTime: Long = 0
        var endTime: Long = 0
        var version: String? = null
        var adTrack: List<*>? = null
        override fun toString(): String {
            return "ProfilePageAdBean(imageUrl=$imageUrl, actionUrl=$actionUrl, startTime=$startTime, endTime=$endTime, version=$version, adTrack=$adTrack)"
        }
    }

    class FirstLaunchBean {

        var isShowFirstDetail: Boolean = false
        var version: String? = null
        override fun toString(): String {
            return "FirstLaunchBean(isShowFirstDetail=$isShowFirstDetail, version=$version)"
        }
    }

    class ShareTitleBean {

        var weibo: String? = null
        var wechatMoments: String? = null
        var qzone: String? = null
        var version: String? = null
        override fun toString(): String {
            return "ShareTitleBean(weibo=$weibo, wechatMoments=$wechatMoments, qzone=$qzone, version=$version)"
        }
    }

    class CampaignInDetailBean {

        var imageUrl: String? = null
        var isAvailable: Boolean = false
        var actionUrl: String? = null
        var showType: String? = null
        var version: String? = null
        override fun toString(): String {
            return "CampaignInDetailBean(imageUrl=$imageUrl, isAvailable=$isAvailable, actionUrl=$actionUrl, showType=$showType, version=$version)"
        }
    }

    class CampaignInFeedBean {

        var imageUrl: String? = null
        var isAvailable: Boolean = false
        var actionUrl: String? = null
        var version: String? = null
        override fun toString(): String {
            return "CampaignInFeedBean(imageUrl=$imageUrl, isAvailable=$isAvailable, actionUrl=$actionUrl, version=$version)"
        }
    }

    class ReplyBean {

        var version: String? = null
        var isOn: Boolean = false
        override fun toString(): String {
            return "ReplyBean(version=$version, isOn=$isOn)"
        }
    }

    class StartPageVideoConfigBean {

        var version: String? = null
        var list: List<ListBeanX>? = null

        class ListBeanX {

            var id: Int = 0
            var icon: String? = null
            var title: String? = null
            var description: String? = null
            var url: String? = null
            var actionUrl: String? = null
            var imageUrl: String? = null
            var displayTimeDuration: Int = 0
            var displayCount: Int = 0
            var isShowImage: Boolean = false
            var showImageTime: Int = 0
            var loadingMode: Int = 0
            var isCountdown: Boolean = false
            var isCanSkip: Boolean = false
            var timeBeforeSkip: Int = 0
            var isShowActionButton: Boolean = false
            var videoType: String? = null
            var videoAdType: String? = null
            var categoryId: Int = 0
            var position: Int = 0
            var startTime: Long = 0
            var endTime: Long = 0
            var status: String? = null
            var adTrack: List<*>? = null
            override fun toString(): String {
                return "ListBeanX(id=$id, icon=$icon, title=$title, description=$description, url=$url, actionUrl=$actionUrl, imageUrl=$imageUrl, displayTimeDuration=$displayTimeDuration, displayCount=$displayCount, isShowImage=$isShowImage, showImageTime=$showImageTime, loadingMode=$loadingMode, isCountdown=$isCountdown, isCanSkip=$isCanSkip, timeBeforeSkip=$timeBeforeSkip, isShowActionButton=$isShowActionButton, videoType=$videoType, videoAdType=$videoAdType, categoryId=$categoryId, position=$position, startTime=$startTime, endTime=$endTime, status=$status, adTrack=$adTrack)"
            }
        }
    }

    class PushInfoBean {

        var normal: String? = null
        var version: String? = null
        override fun toString(): String {
            return "PushInfoBean(normal=$normal, version=$version)"
        }
    }

    class HomepageBean {

        var cover: String? = null
        var version: String? = null
        override fun toString(): String {
            return "HomepageBean(cover=$cover, version=$version)"
        }
    }

    override fun toString(): String {
        return "ConfigsData(autoCache=$autoCache, startPageAd=$startPageAd, videoAdsConfig=$videoAdsConfig, startPage=$startPage, log=$log, issueCover=$issueCover, startPageVideo=$startPageVideo, consumption=$consumption, launch=$launch, preload=$preload, version=$version, push=$push, androidPlayer=$androidPlayer, profilePageAd=$profilePageAd, firstLaunch=$firstLaunch, shareTitle=$shareTitle, campaignInDetail=$campaignInDetail, campaignInFeed=$campaignInFeed, reply=$reply, startPageVideoConfig=$startPageVideoConfig, pushInfo=$pushInfo, homepage=$homepage)"
    }
}
