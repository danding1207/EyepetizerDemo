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
        }
    }

    class StartPageBean {

        var imageUrl: String? = null
        var actionUrl: String? = null
        var version: String? = null
    }

    class LogBean {

        var isPlayLog: Boolean = false
        var version: String? = null
    }

    class IssueCoverBean {

        var issueLogo: IssueLogoBean? = null
        var version: String? = null

        class IssueLogoBean {
            var weekendExtra: String? = null
            var isAdapter: Boolean = false
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
    }

    class ConsumptionBean {

        var isDisplay: Boolean = false
        var version: String? = null
    }

    class LaunchBean {

        var version: String? = null
        var adTrack: List<*>? = null
    }

    class PreloadBean {

        var version: String? = null
        var isOn: Boolean = false
    }

    class PushBean {

        var startTime: Int = 0
        var endTime: Int = 0
        var message: String? = null
        var version: String? = null
    }

    class AndroidPlayerBean {

        var version: String? = null
        var playerList: List<String>? = null
    }

    class ProfilePageAdBean {

        var imageUrl: String? = null
        var actionUrl: String? = null
        var startTime: Long = 0
        var endTime: Long = 0
        var version: String? = null
        var adTrack: List<*>? = null
    }

    class FirstLaunchBean {

        var isShowFirstDetail: Boolean = false
        var version: String? = null
    }

    class ShareTitleBean {

        var weibo: String? = null
        var wechatMoments: String? = null
        var qzone: String? = null
        var version: String? = null
    }

    class CampaignInDetailBean {

        var imageUrl: String? = null
        var isAvailable: Boolean = false
        var actionUrl: String? = null
        var showType: String? = null
        var version: String? = null
    }

    class CampaignInFeedBean {

        var imageUrl: String? = null
        var isAvailable: Boolean = false
        var actionUrl: String? = null
        var version: String? = null
    }

    class ReplyBean {

        var version: String? = null
        var isOn: Boolean = false
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
        }
    }

    class PushInfoBean {

        var normal: String? = null
        var version: String? = null
    }

    class HomepageBean {

        var cover: String? = null
        var version: String? = null
    }
}
