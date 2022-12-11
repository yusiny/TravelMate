package com.algorithm_termproject.travelmate.utils

import android.util.Log
import com.algorithm_termproject.travelmate.data.Place

fun aryToStr(list: ArrayList<Place>): String? {
    val str = StringBuilder()
    for (place in list) {
        str.append(place.name).append(", ")
    }
    return str.toString()
}

fun showMatrix(matrix: Array<IntArray>, size: Int): String{
    var str = " Matrix \n"
    for (i in 0 until size) {
        str += "[$i] "
        for (j in 0 until size) {
            str += String.format("%4d", matrix[i][j]) + " "
        }

        str += "\n"
    }

    return str
}