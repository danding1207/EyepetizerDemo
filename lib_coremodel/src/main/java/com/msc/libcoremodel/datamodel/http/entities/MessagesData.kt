package com.msc.libcoremodel.datamodel.http.entities

class MessagesData {

    var updateTime: Long = 0
    var nextPageUrl: String? = null
    var messageList: List<MessageListBean>? = null

    class MessageListBean {

        var color: String? = "black"

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
