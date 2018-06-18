package com.msc.libcommon.util

import com.msc.libcoremodel.datamodel.http.entities.AllRecData
import com.tmall.wireless.tangram.dataparser.concrete.Card

import java.util.ArrayList

object DataToCardListUtils {


    /**
     *
     */
    fun transformation(data: AllRecData): List<Card> {
        val newData = ArrayList<Card>()

        data.itemList!!.forEach { item ->

//            var card:Card = Card()



            println(item) }


        return newData
    }


}
