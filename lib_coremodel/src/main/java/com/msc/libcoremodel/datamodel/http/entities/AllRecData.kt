package com.msc.libcoremodel.datamodel.http.entities

class AllRecData {

    var count: Int = 0
    var total: Int = 0
    var nextPageUrl: String? = null
    var adExist: Boolean = false
    var itemList: List<ItemListBeanX>? = null

    class ItemListBeanX {

        var type: String? = null
        var data: DataBeanXX? = null
        var tag: List<TagBean>? = null
        var id: Int = 0
        var adIndex: Int = 0


        class DataBeanXX {

            var dataType: String? = null
            var header: HeaderBean? = null
            var count: Int = 0
            var adTrack: String? = null
            var itemList: List<ItemListBean>? = null

            var id: Int = 0
            var type: String? = null
            var text: String? = null
            var subTitle: String? = null
            var actionUrl: String? = null
            var follow: FollowBean? = null
            var content: ContentBean? = null
            var title: String? = null
            var description: String? = null
            var image: String? = null
            var category: String? = null
            var cover: ContentBean.ContentDataBean.CoverBean? = null
            var duration: Int = 0
            var resourceType: String? = null
            var playInfo: List<ContentBean.ContentDataBean.PlayInfoBean>? = null


            class HeaderBean {
                /**
                 * id : 5
                 * title : 开眼今日编辑精选
                 * font : bigBold
                 * subTitle : FRIDAY, JUNE 15
                 * subTitleFont : lobster
                 * textAlign : left
                 * cover : null
                 * label : null
                 * actionUrl : eyepetizer://feed?tabIndex=2
                 * labelList : null
                 */

                var id: Int = 0
                var title: String? = null
                var font: String? = null
                var subTitle: String? = null
                var subTitleFont: String? = null
                var textAlign: String? = null
                var cover: String? = null
                var label: String? = null
                var actionUrl: String? = null
                var labelList: String? = null
                var description: String? = null



            }

            class ItemListBean {

                var type: String? = null
                var data: DataBeanX? = null
                var tag: String? = null
                var id: Int = 0
                var adIndex: Int = 0

                class DataBeanX {

                    var dataType: String? = null
                    var header: HeaderBeanX? = null
                    var content: ContentBean? = null
                    var adTrack: String? = null

                    class HeaderBeanX {

                        var id: Int = 0
                        var title: String? = null
                        var font: String? = null
                        var subTitle: String? = null
                        var subTitleFont: String? = null
                        var textAlign: String? = null
                        var cover: String? = null
                        var label: String? = null
                        var actionUrl: String? = null
                        var labelList: String? = null
                        var icon: String? = null
                        var iconType: String? = null
                        var description: String? = null
                        var time: Long = 0
                        var isShowHateVideo: Boolean = false
                    }

                    class ContentBean {

                        var type: String? = null
                        var data: DataBean? = null
                        var tag: String? = null
                        var id: Int = 0
                        var adIndex: Int = 0

                        class DataBean {

                            var dataType: String? = null
                            var id: Int = 0
                            var title: String? = null
                            var description: String? = null
                            var library: String? = null
                            var consumption: ConsumptionBean? = null
                            var resourceType: String? = null
                            var slogan: String? = null
                            var provider: ProviderBean? = null
                            var category: String? = null
                            var author: AuthorBean? = null
                            var cover: CoverBean? = null
                            var playUrl: String? = null
                            var thumbPlayUrl: String? = null
                            var duration: Int = 0
                            var webUrl: WebUrlBean? = null
                            var releaseTime: Long = 0
                            var campaign: String? = null
                            var waterMarks: String? = null
                            var adTrack: String? = null
                            var type: String? = null
                            var titlePgc: String? = null
                            var descriptionPgc: String? = null
                            var remark: String? = null
                            var isIfLimitVideo: Boolean = false
                            var searchWeight: Int = 0
                            var idx: Int = 0
                            var shareAdTrack: String? = null
                            var favoriteAdTrack: String? = null
                            var webAdTrack: String? = null
                            var date: Long = 0
                            var promotion: String? = null
                            var label: String? = null
                            var descriptionEditor: String? = null
                            var isCollected: Boolean = false
                            var isPlayed: Boolean = false
                            var lastViewTime: String? = null
                            var playlists: String? = null
                            var src: String? = null
                            var tags: List<TagsBean>? = null
                            var playInfo: List<PlayInfoBean>? = null
                            var labelList: List<*>? = null
                            var subtitles: List<*>? = null

                            class ConsumptionBean {
                                /**
                                 * collectionCount : 115
                                 * shareCount : 16
                                 * replyCount : 4
                                 */

                                var collectionCount: Int = 0
                                var shareCount: Int = 0
                                var replyCount: Int = 0
                            }

                            class ProviderBean {
                                /**
                                 * name : Vimeo
                                 * alias : vimeo
                                 * icon : http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png
                                 */

                                var name: String? = null
                                var alias: String? = null
                                var icon: String? = null
                            }

                            class AuthorBean {
                                /**
                                 * id : 938
                                 * icon : http://img.kaiyanapp.com/e44ed5fcfa424ba35761ce5f1339bc16.jpeg?imageMogr2/quality/60/format/jpg
                                 * name : 欧美广告精选
                                 * description : 持续推送新奇、有趣、大开眼界的欧美创意广告
                                 * link :
                                 * latestReleaseTime : 1529024414000
                                 * videoNum : 373
                                 * adTrack : null
                                 * follow : {"itemType":"author","itemId":938,"followed":false}
                                 * shield : {"itemType":"author","itemId":938,"shielded":false}
                                 * approvedNotReadyVideoCount : 0
                                 * ifPgc : true
                                 */

                                var id: Int = 0
                                var icon: String? = null
                                var name: String? = null
                                var description: String? = null
                                var link: String? = null
                                var latestReleaseTime: Long = 0
                                var videoNum: Int = 0
                                var adTrack: String? = null
                                var follow: FollowBean? = null
                                var shield: ShieldBean? = null
                                var approvedNotReadyVideoCount: Int = 0
                                var isIfPgc: Boolean = false

                                class FollowBean {
                                    /**
                                     * itemType : author
                                     * itemId : 938
                                     * followed : false
                                     */

                                    var itemType: String? = null
                                    var itemId: Int = 0
                                    var isFollowed: Boolean = false
                                }

                                class ShieldBean {
                                    /**
                                     * itemType : author
                                     * itemId : 938
                                     * shielded : false
                                     */

                                    var itemType: String? = null
                                    var itemId: Int = 0
                                    var isShielded: Boolean = false
                                }
                            }

                            class CoverBean {
                                /**
                                 * feed : http://img.kaiyanapp.com/bfb4f4039c8dd3f13814f17593502daa.png?imageMogr2/quality/60/format/jpg
                                 * detail : http://img.kaiyanapp.com/bfb4f4039c8dd3f13814f17593502daa.png?imageMogr2/quality/60/format/jpg
                                 * blurred : http://img.kaiyanapp.com/c63793fa37b58efdc67cba5260f5d77e.png?imageMogr2/quality/60/format/jpg
                                 * sharing : null
                                 * homepage : http://img.kaiyanapp.com/bfb4f4039c8dd3f13814f17593502daa.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim
                                 */

                                var feed: String? = null
                                var detail: String? = null
                                var blurred: String? = null
                                var sharing: String? = null
                                var homepage: String? = null
                            }

                            class WebUrlBean {
                                /**
                                 * raw : http://www.eyepetizer.net/detail.html?vid=109015
                                 * forWeibo : http://www.eyepetizer.net/detail.html?vid=109015
                                 */

                                var raw: String? = null
                                var forWeibo: String? = null
                            }

                            class TagsBean {
                                /**
                                 * id : 748
                                 * name : 广告精选
                                 * actionUrl : eyepetizer://tag/748/?title=%E5%B9%BF%E5%91%8A%E7%B2%BE%E9%80%89
                                 * adTrack : null
                                 * desc : null
                                 * bgPicture : http://img.kaiyanapp.com/431177a6b2177788aa4d8eff8073d9a7.jpeg?imageMogr2/quality/60/format/jpg
                                 * headerImage : http://img.kaiyanapp.com/431177a6b2177788aa4d8eff8073d9a7.jpeg?imageMogr2/quality/60/format/jpg
                                 * tagRecType : IMPORTANT
                                 */

                                var id: Int = 0
                                var name: String? = null
                                var actionUrl: String? = null
                                var adTrack: String? = null
                                var desc: String? = null
                                var bgPicture: String? = null
                                var headerImage: String? = null
                                var tagRecType: String? = null
                            }

                            class PlayInfoBean {
                                /**
                                 * height : 270
                                 * width : 480
                                 * urlList : [{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=109015&resourceType=video&editionType=low&source=aliyun","size":2578896},{"name":"qcloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=109015&resourceType=video&editionType=low&source=qcloud","size":2578896},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=109015&resourceType=video&editionType=low&source=ucloud","size":2578896}]
                                 * name : 流畅
                                 * type : low
                                 * url : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=109015&resourceType=video&editionType=low&source=aliyun
                                 */

                                var height: Int = 0
                                var width: Int = 0
                                var name: String? = null
                                var type: String? = null
                                var url: String? = null
                                var urlList: List<UrlListBean>? = null

                                class UrlListBean {
                                    /**
                                     * name : aliyun
                                     * url : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=109015&resourceType=video&editionType=low&source=aliyun
                                     * size : 2578896
                                     */

                                    var name: String? = null
                                    var url: String? = null
                                    var size: Int = 0
                                }
                            }
                        }
                    }
                }
            }

            class FollowBean {
                var itemId: Int = 0
                var itemType: String? = null
                var followed: Boolean? = false
            }

            class ContentBean {
                var adIndex: Int = 0
                var id: Int = 0
                var type: String? = null
                var data: ContentDataBean? = null

                class ContentDataBean {
                    var id: Int = 0
                    var uid: Int = 0
                    var createTime: Long = 0
                    var updateTime: Long = 0
                    var selectedTime: Long = 0
                    var releaseTime: Long = 0
                    var status: String? = null
                    var duration: Int = 0
                    var dataType: String? = null
                    var title: String? = null
                    var description: String? = null
                    var library: String? = null
                    var resourceType: String? = null
                    var checkStatus: String? = null
                    var url: String? = null
                    var playUrl: String? = null
                    var collected: Boolean? = false
                    var tags: List<TagBean>? = null
                    var consumption: ConsumptionBean? = null
                    var owner: OwnerBean? = null
                    var cover: CoverBean? = null
                    var author: ItemListBean.DataBeanX.ContentBean.DataBean.AuthorBean? = null
                    var playInfo: List<PlayInfoBean>? = null

                    class PlayInfoBean {
                        var height: Int = 0
                        var width: Int = 0
                        var name: String? = null
                        var type: String? = null
                        var url: String? = null
                    }

                    class ConsumptionBean {
                        var collectionCount: Int = 0
                        var shareCount: Int = 0
                        var replyCount: Int = 0
                    }
                    class OwnerBean {
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
                    class CoverBean {
                        var feed: String? = null
                        var detail: String? = null
                        var blurred: String? = null
                        var sharing: String? = null
                        var homepage: String? = null
                    }

                }

            }

        }

        class TagBean {
            var id: Int = 0
            var name: String? = null
            var actionUrl: String? = null
            var adTrack: String? = null
            var desc: String? = null
            var bgPicture: String? = null
            var headerImage: String? = null
            var tagRecType: String? = null
        }

    }
}
