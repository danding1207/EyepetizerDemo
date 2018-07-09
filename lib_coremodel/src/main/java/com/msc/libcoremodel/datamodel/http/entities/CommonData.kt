package com.msc.libcoremodel.datamodel.http.entities

class CommonData {

    var itemList: List<CommonItemList>? = null
    var count: Int = 0
    var total: Int = 0
    var nextPageUrl: String? = null
    var adExist: Boolean = false

    class CommonItemList {

        var id: Int = 0
        var adIndex: Int = 0
        var type: String? = null
        var data: CommonItemListData? = null
        var color: String? = "black"

        var emptyTitle: String? = "black"
        var emptyDescription: String? = "black"

        class CommonItemListData {

            // dataType == BriefCard 时特殊
            var icon: String? = null
            var iconType: String? = null
            var ifPgc: Boolean = false
            var follow: CommonItemListDataFollow? = null
            class CommonItemListDataFollow {
                var itemId: Int = 0
                var itemType: String? = null
                var followed: Boolean = false
            }
            // dataType == BriefCard 时特殊

            // dataType == squareCardCollection 时特殊
            var count: Int = 0
            var itemList: List<CommonItemList>? = null
            // dataType == squareCardCollection 时特殊

            // dataType == FollowCard 时特殊

            var header: CommonItemListDataHeader? = null

            class CommonItemListDataHeader {
                var id: Int = 0

                var title: String? = null
                var font: String? = null
                var subTitle: String? = null
                var subTitleFont: String? = null
                var textAlign: String? = null
                var cover: String? = null
                var label: String? = null

                var actionUrl: String? = null
//            var labelList: String? = null

                var icon: String? = null
                var iconType: String? = null
                var description: String? = null
                var time: Long = 0
                var showHateVideo: Boolean = false

                override fun toString(): String {
                    return "{" +
                            "\"id\":$id, " +
                            "\"title\":\"$title\", " +
                            "\"font\":\"$font\", " +
                            "\"subTitle\":\"$subTitle\", " +
                            "\"subTitleFont\":\"$subTitleFont\", " +
                            "\"textAlign\":\"$textAlign\", " +
                            "\"cover\":\"$cover\", " +
                            "\"label\":\"$label\", " +
                            "\"actionUrl\":\"$actionUrl\", " +
                            "\"icon\":\"$icon\", " +
                            "\"iconType\":\"$iconType\", " +
                            "\"description\":\"$description\", " +
                            "\"time\":$time, " +
                            "\"showHateVideo\":$showHateVideo}"
                }

            }

            var content: CommonItemList? = null

            // dataType == FollowCard 时特殊


            // dataType == autoPlayFollowCard 时特殊
            var owner:  CommonItemListDataOwner? = null

            class CommonItemListDataOwner {
                var uid: Int = 0
                var registDate: Long = 0
                var releaseDate: Long = 0
                var ifPgc: Boolean? = false
                var followed: Boolean? = false
                var limitVideoOpen: Boolean? = false

                var nickname: String? = null
                var avatar: String? = null
                var userType: String? = null
                var description: String? = null
                var area: String? = null
                var gender: String? = null
                var cover: String? = null
                var actionUrl: String? = null
                var library: String? = null

                override fun toString(): String {
                    return "{" +
                            "\"uid\":$uid, " +
                            "\"registDate\":$registDate, " +
                            "\"releaseDate\":$releaseDate, " +
                            "\"ifPgc\":$ifPgc, " +
                            "\"followed\":$followed, " +
                            "\"limitVideoOpen\":$limitVideoOpen, " +
                            "\"nickname\":\"$nickname\", " +
                            "\"avatar\":\"$avatar\", " +
                            "\"userType\":\"$userType\", " +
                            "\"description\":\"$description\", " +
                            "\"area\":\"$area\", " +
                            "\"gender\":\"$gender\", " +
                            "\"cover\":\"$cover\", " +
                            "\"actionUrl\":\"$actionUrl\", " +
                            "\"library\":\"$library\"}"
                }


            }

            var uid: Int = 0
            var createTime: Long = 0
            var updateTime: Long = 0
            var selectedTime: Long = 0
            var status: String? = null
            var checkStatus: String? = null
            var url: String? = null
            // dataType == autoPlayFollowCard 时特殊

            // dataType == banner 时特殊
            var image: String? = null
            // dataType == banner 时特殊


            var id: Int = 0
            var type: String? = null
            var dataType: String? = null


            var text: String? = null
            var subTitle: String? = null

            var actionUrl: String? = null

            var title: String? = null
            var description: String? = null
            var library: String? = null

            var tags: List<CommonItemListDataTags>? = null


            class CommonItemListDataTags {
                var id: Int = 0
                var name: String? = null
                var actionUrl: String? = null
                var bgPicture: String? = null
                var headerImage: String? = null
                var tagRecType: String? = null

                override fun toString(): String {
                    return "{" +
                            "\"id\":$id, " +
                            "\"name\":\"$name\", " +
                            "\"actionUrl\":\"$actionUrl\", " +
                            "\"bgPicture\":\"$bgPicture\", " +
                            "\"headerImage\":\"$headerImage\", " +
                            "\"tagRecType\":\"$tagRecType\"}"
                }
//TODO
//                "adTrack":null,
//                "desc":null,
            }

            var consumption: CommonItemListDataConsumption? = null


            class CommonItemListDataConsumption {
                var collectionCount: Int = 0
                var shareCount: Int = 0
                var replyCount: Int = 0
                override fun toString(): String {
                    return "{" +
                            "\"collectionCount\":$collectionCount, " +
                            "\"shareCount\":$shareCount, " +
                            "\"replyCount\":$replyCount}"
                }

            }

            var resourceType: String? = null
            var slogan: String? = null

            class CommonItemListDataProvider {
                var name: String? = null
                var alias: String? = null
                var icon: String? = null
                override fun toString(): String {
                    return "{" +
                            "\"name\":\"$name\", " +
                            "\"alias\":\"$alias\", " +
                            "\"icon\":\"$icon\"}"
                }

            }

            var provider: CommonItemListDataProvider? = null

            var category: String? = null

            class CommonItemListDataAuthor {
                var id: Int = 0
                var videoNum: Int = 0
                var approvedNotReadyVideoCount: Int = 0
                var latestReleaseTime: Long = 0

                var icon: String? = null
                var name: String? = null
                var description: String? = null
                var link: String? = null

                var ifPgc: Boolean = false

//TODO
//                "adTrack":null,

                var follow: CommonItemListDataAuthorFollow? = null

                class CommonItemListDataAuthorFollow {
                    var itemType: String? = null
                    var itemId: Int = 0
                    var followed: Boolean = false
                    override fun toString(): String {
                        return "{" +
                                "\"itemType\":\"$itemType\", " +
                                "\"itemId\":$itemId, " +
                                "\"followed\":$followed}"
                    }

                }

                var shield: CommonItemListDataAuthorShield? = null

                class CommonItemListDataAuthorShield {
                    var itemType: String? = null
                    var itemId: Int = 0
                    var shielded: Boolean = false
                    override fun toString(): String {
                        return "{" +
                                "\"itemType\":\"$itemType\", " +
                                "\"itemId\":$itemId, " +
                                "\"shielded\":$shielded}"
                    }
                }

                override fun toString(): String {
                    return "{" +
                            "\"id\":$id, " +
                            "\"videoNum\":$videoNum, " +
                            "\"approvedNotReadyVideoCount\":$approvedNotReadyVideoCount, " +
                            "\"latestReleaseTime\":$latestReleaseTime, " +
                            "\"icon\":\"$icon\", " +
                            "\"name\":\"$name\", " +
                            "\"description\":\"$description\", " +
                            "\"link\":\"$link\", " +
                            "\"ifPgc\":$ifPgc, " +
                            "\"follow\":${follow.toString()}, " +
                            "\"shield\":${shield.toString()}}"
                }

            }

            var author: CommonItemListDataAuthor? = null

            class CommonItemListDataCover {
                var feed: String? = null
                var detail: String? = null
                var blurred: String? = null
                var sharing: String? = null
                var homepage: String? = null
                override fun toString(): String {
                    return "{" +
                            "\"feed\":\"$feed\", " +
                            "\"detail\":\"$detail\", " +
                            "\"blurred\":\"$blurred\", " +
                            "\"sharing\":\"$sharing\", " +
                            "\"homepage\":\"$homepage\"}"
                }

            }

            var cover: CommonItemListDataCover? = null

            var playUrl: String? = null
            var thumbPlayUrl: String? = null

            var duration: Int = 0

            class CommonItemListDataWebUrl {
                var raw: String? = null
                var forWeibo: String? = null
                override fun toString(): String {
                    return "{" +
                            "\"raw\":\"$raw\", " +
                            "\"forWeibo\":\"$forWeibo\"}"
                }

            }

            var webUrl: CommonItemListDataWebUrl? = null

            var releaseTime: Long = 0


            var playInfo: List<CommonItemListDataPlayInfo>? = null

            class CommonItemListDataPlayInfo {
                var height: Int = 0
                var width: Int = 0

                var name: String? = null
                var type: String? = null
                var url: String? = null

                var urlList: List<CommonItemListDataPlayInfoUrlList>? = null

                class CommonItemListDataPlayInfoUrlList {
                    var name: String? = null
                    var url: String? = null
                    var size: Long = 0
                    override fun toString(): String {
                        return "{" +
                                "\"name\":\"$name\", " +
                                "\"url\":\"$url\", " +
                                "\"size\":$size}"
                    }

                }

                override fun toString(): String {
                    return "{" +
                            "\"height\":$height, " +
                            "\"width\":$width, " +
                            "\"name\":\"$name\", " +
                            "\"type\":\"$type\", " +
                            "\"url\":\"$url\", " +
//                            "\"urlList\":$urlList" +
                            "\"urlList\":${
                                urlList!!.forEach { it->it.toString() }
                            }" +
                            "}"
                }

            }

            var descriptionEditor: String? = null

            var date: Long = 0
            var searchWeight: Int = 0
            var idx: Int = 0
            var src: Int = 0

            var ifLimitVideo: Boolean = false
            var collected: Boolean = false
            var played: Boolean = false

            var labelList: List<CommonItemListDataLabelList>? = null

            class CommonItemListDataLabelList {
                var text: String? = null
                var actionUrl: String? = null
                override fun toString(): String {
                    return "{" +
                            "\"text\":\"$text\", " +
                            "\"actionUrl\":\"$actionUrl\"}"
                }

            }

            var subTitles: List<CommonItemListDatasubTitle>? = null

            class CommonItemListDatasubTitle {
                var type: String? = null
                var url: String? = null
                override fun toString(): String {
                    return "{" +
                            "\"type\":\"$type\", " +
                            "\"url\":\"$url\"}"
                }

            }

            override fun toString(): String {
                return "{" +
                        "\"count\":$count, " +
//                        "\"itemList\":$itemList, " +
                        "\"itemList\":${
                            itemList!!.forEach { it->it.toString() }
                        }, " +
                        "\"header\":${header.toString()}, " +
                        "\"content\":${content.toString()}, " +
                        "\"owner\":${owner.toString()}, " +
                        "\"uid\":$uid, " +
                        "\"createTime\":$createTime, " +
                        "\"updateTime\":$updateTime, " +
                        "\"selectedTime\":$selectedTime, " +
                        "\"status\":\"$status\", " +
                        "\"checkStatus\":\"$checkStatus\", " +
                        "\"url\":\"$url\", " +
                        "\"image\":\"$image\", " +
                        "\"id\":$id, " +
                        "\"type\":\"$type\", " +
                        "\"dataType\":\"$dataType\", " +
                        "\"text\":\"$text\", " +
                        "\"subTitle\":\"$subTitle\", " +
                        "\"actionUrl\":\"$actionUrl\", " +
                        "\"title\":\"$title\", " +
                        "\"description\":\"$description\", " +
                        "\"library\":\"$library\", " +
//                        "\"tags\":$tags, " +
                        "\"tags\":${
                            tags!!.forEach { it->it.toString() }
                        }, " +
                        "\"consumption\":${consumption.toString()}, " +
                        "\"resourceType\":\"$resourceType\", " +
                        "\"slogan\":\"$slogan\", " +
                        "\"provider\":${provider.toString()}, " +
                        "\"category\":\"$category\", " +
                        "\"author\":${author.toString()}, " +
                        "\"cover\":${cover.toString()}, " +
                        "\"playUrl\":\"$playUrl\", " +
                        "\"thumbPlayUrl\":\"$thumbPlayUrl\", " +
                        "\"duration\":$duration, " +
                        "\"webUrl\":${webUrl.toString()}, " +
                        "\"releaseTime\":$releaseTime, " +
//                        "\"playInfo\":$playInfo, " +
                        "\"playInfo\":${
                            playInfo!!.forEach { it->it.toString() }
                        }, " +
                        "\"descriptionEditor\":\"$descriptionEditor\", " +
                        "\"date\":$date, " +
                        "\"searchWeight\":$searchWeight, " +
                        "\"idx\":$idx, " +
                        "\"src\":$src, " +
                        "\"ifLimitVideo\":$ifLimitVideo, " +
                        "\"collected\":$collected, " +
                        "\"played\":$played, " +
//                        "\"labelList\":$labelList, " +
                        "\"labelList\":${
                            labelList!!.forEach { it->it.toString() }
                        }, " +
//                        "\"subTitles\":$subTitles" +
                        "\"subTitles\":${
                            subTitles!!.forEach { it->it.toString() }
                        }" +
                        "}"
            }

            //TODO
//            "campaign":null,
//            "waterMarks":null,
//            "adTrack":null,
//            "titlePgc":null,
//            "descriptionPgc":null,
//            "remark":null,
//            "lastViewTime":null,
//            "playlists":null,
//            "shareAdTrack":null,
//            "favoriteAdTrack":null,
//            "webAdTrack":null,
//            "promotion":null,
//            "label":null,
//            "adTrack":null,
//            "follow":null

        }

        override fun toString(): String {
            return "{" +
                    "\"id\":$id, " +
                    "\"adIndex\":$adIndex, " +
                    "\"type\":\"$type\", " +
                    "\"data\":${if(data==null) null else data.toString()}\", " +
                    "\"color\":\"$color\"}"
        }

        //TODO
        //"tag":null
        //"adTrack":null



    }


    val  EMPTYCOMOONITEMLISTDATA: CommonItemList.CommonItemListData=
            CommonItemList.CommonItemListData()





}
