package com.msc.libcoremodel.datamodel.http.entities

class MessagesData {


    /**
     * messageList : [{"id":9475123,"title":"官方通知","content":"还有不到 10 天，2018 上半年就要过去了。你还记得自己年初许下了什么愿望，立下了什么 flag 吗？不妨在这里大声 say out！说出来，然后继续实现它！点击参与话题>>","date":1529497800000,"actionUrl":"eyepetizer://webview/?title=%E8%BF%98%E8%AE%B0%E5%BE%97%E5%B9%B4%E5%88%9D%E7%9A%84%20flag%20%E5%90%97%EF%BC%9F&url=http%3A%2F%2Fwww.kaiyanapp.com%2Ftopic_article.html%3Fnid%3D66%26shareable%3Dtrue%26cookie%3D","icon":"http://img.wdjimg.com/image/video/418d281e65bf010c38c7b07bdd7b6a94_0_0.png","ifPush":true,"pushStatus":1,"uid":null},{"id":454949,"title":"官方通知","content":"开眼好友功能上线啦~在评论区看到志同道合的朋友，点个「关注」即可收到 TA 最新的评论和喜欢咯~！点击查看功能详情>>","date":1520841600000,"actionUrl":"eyepetizer://webview/?title=%E5%A5%BD%E5%8F%8B%E5%8A%9F%E8%83%BD%E4%B8%8A%E7%BA%BF%E5%95%A6~&url=http%3A%2F%2Fwww.kaiyanapp.com%2Fguide%2Ffollow-guide.html","icon":"http://img.wdjimg.com/image/video/418d281e65bf010c38c7b07bdd7b6a94_0_0.png","ifPush":false,"pushStatus":0,"uid":null},{"id":451311,"title":"官方通知","content":"每逢年节胖三斤，来晒晒你家的年味吧~ 各位来自祖国东南西北，以及身处海外的同胞们，听说今年过年有新传统，来开眼晒一晒自己家的年味！","date":1518703200000,"actionUrl":"eyepetizer://webview/?title=%E6%96%B0%E6%98%A5%E5%BF%AB%E4%B9%90~%EF%BC%81&url=http%3A%2F%2Fwww.kaiyanapp.com%2Ftopic_article.html%3Fnid%3D54%26cookie%3D%26shareable%3Dtrue","icon":"http://img.wdjimg.com/image/video/418d281e65bf010c38c7b07bdd7b6a94_0_0.png","ifPush":false,"pushStatus":0,"uid":null},{"id":451213,"title":"官方通知","content":"2018 开眼用户调查开始啦~ 为我们提供宝贵的信息和意见，帮助我们变得更好。点击开始填写 >>","date":1517293867000,"actionUrl":"eyepetizer://webview/?title=2018%20%E5%BC%80%E7%9C%BC%E7%94%A8%E6%88%B7%E8%B0%83%E6%9F%A5%E9%97%AE%E5%8D%B7&url=https%3A%2F%2Fjinshuju.net%2Ff%2FwIy1eF%3Fx_field_1%3D5ab5bd3e87e04215bf7820e58576aa192784ca51","icon":"http://img.wdjimg.com/image/video/418d281e65bf010c38c7b07bdd7b6a94_0_0.png","ifPush":false,"pushStatus":0,"uid":null},{"id":451098,"title":"官方通知","content":"开眼话题互动：还有不到一周 2017 年就要正式成为你永久的回忆了，在这一年中有哪些让你难忘的事情，有哪些收获，你又是什么的状态呢？","date":1514347200000,"actionUrl":"eyepetizer://webview/?title=%E8%AF%9D%E9%A2%98%E4%BA%92%E5%8A%A8&url=http%3A%2F%2Fwww.kaiyanapp.com%2Ftopic_article.html%3Fnid%3D30%26cookie%3D%26shareable%3Dtrue","icon":"http://img.wdjimg.com/image/video/418d281e65bf010c38c7b07bdd7b6a94_0_0.png","ifPush":false,"pushStatus":0,"uid":null}]
     * updateTime : 1529497800000
     * nextPageUrl : null
     */

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

        var id: Int = 0
        var title: String? = null
        var content: String? = null
        var date: Long = 0
        var actionUrl: String? = null
        var icon: String? = null
        var isIfPush: Boolean = false
        var pushStatus: Int = 0
        var uid: String? = null
    }
}
