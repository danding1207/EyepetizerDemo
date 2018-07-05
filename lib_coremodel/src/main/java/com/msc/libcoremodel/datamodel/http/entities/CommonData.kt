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


        class CommonItemListData {

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
//TODO
//                "adTrack":null,
//                "desc":null,
            }

            var consumption: CommonItemListDataConsumption? = null


            class CommonItemListDataConsumption {
                var collectionCount: Int = 0
                var shareCount: Int = 0
                var replyCount: Int = 0

            }

            var resourceType: String? = null
            var slogan: String? = null

            class CommonItemListDataProvider {
                var name: String? = null
                var alias: String? = null
                var icon: String? = null
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
                }

                var shield: CommonItemListDataAuthorShield? = null

                class CommonItemListDataAuthorShield {
                    var itemType: String? = null
                    var itemId: Int = 0
                    var shielded: Boolean = false
                }

            }

            var author: CommonItemListDataAuthor? = null

            class CommonItemListDataCover {
                var feed: String? = null
                var detail: String? = null
                var blurred: String? = null
                var sharing: String? = null
                var homepage: String? = null
            }

            var cover: CommonItemListDataCover? = null

            var playUrl: String? = null
            var thumbPlayUrl: String? = null

            var duration: Int = 0

            class CommonItemListDataWebUrl {
                var raw: String? = null
                var forWeibo: String? = null
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
            }

            var subTitles: List<CommonItemListDatasubTitle>? = null

            class CommonItemListDatasubTitle {
                var type: String? = null
                var url: String? = null
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

        //TODO
        //"tag":null
        //"adTrack":null

    }

}
