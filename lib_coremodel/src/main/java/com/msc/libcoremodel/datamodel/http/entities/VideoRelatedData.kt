package com.msc.libcoremodel.datamodel.http.entities

class VideoRelatedData {

    var count: Int = 0
    var total: Int = 0
    var nextPageUrl: String? = null
    var isAdExist: Boolean = false
    var itemList: List<ItemListBean>? = null

    class ItemListBean {

        var type: String? = null
        var data: DataBean? = null
//        var tag: Any? = null
        var id: Int = 0
        var adIndex: Int = 0

        class DataBean {

            var dataType: String? = null
            var id: Int = 0
            var type: String? = null
            var text: String? = null
            var subTitle: String? = null
            var actionUrl: String? = null
//            var adTrack: Any? = null
//            var follow: Any? = null
        }
    }
}
