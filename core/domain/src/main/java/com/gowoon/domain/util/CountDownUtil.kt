package com.gowoon.domain.util

fun Int.format(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return buildString {
        if (hours > 0) append("${hours}시간 ")
        if (minutes > 0) append("${minutes}분 ")
        if (seconds > 0) append("${seconds}초 ") else append("0초 ")
    }
}