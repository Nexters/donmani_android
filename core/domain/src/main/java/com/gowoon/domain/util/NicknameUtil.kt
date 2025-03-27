package com.gowoon.domain.util

object NicknameUtil {
    const val NICKNAMEMAX_LENGTH = 12
    const val DEFAULT_SUFFIX = "님의 별통이"
    fun isValidLength(input: String): Boolean {
        return input.length in 2..12
    }

    fun isValidCharacter(input: String): Boolean {
        val regex = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\s]*$".toRegex()
        return regex.matches(input)
    }
}