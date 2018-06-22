package com.msc.libcoremodel.datamodel.http.entities

class MessagesData {

    var updateTime: Long = 0
    var nextPageUrl: String? = null
    var messageList: List<MessageListBean>? = null

    class MessageListBean {
        /**
         * id : 9475123
         * title : 官方通知
         * content : 还有不到 10 天，2018 上半年就要过去了。你还记得自己年初许下了什么愿望，立下了什么 flag 吗？不妨在这里大声 say out！说出来，然后继续实现它！点击参与话题>>
         * date : 1529497800000
         * actionUrl : eyepetizer://webview/?title=%E8%BF%98%E8%AE%B0%E5%BE%97%E5%B9%B4%E5%88%9D%E7%9A%84%20flag%20%E5%90%97%EF%BC%9F&url=http%3A%2F%2Fwww.kaiyanapp.com%2Ftopic_article.html%3Fnid%3D66%26shareable%3Dtrue%26cookie%3D
         * icon : http://img.wdjimg.com/image/video/418d281e65bf010c38c7b07bdd7b6a94_0_0.png
         * ifPush : true
         * pushStatus : 1
         * uid : null
         */
        var type: String? = "normal"
        var id: Int = 0
        var title: String? = null
        var content: String? = null
        var date: Long = 0
        var actionUrl: String? = null
        var icon: String? = null
        var isIfPush: Boolean = false
        var pushStatus: Int = 0
        var uid: String? = null

//        constructor( )

//        constructor(type: String?, content: String?) {
//            this.type = type
//            this.content = content
//        }
    }
}
