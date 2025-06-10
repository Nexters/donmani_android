package com.gowoon.domain.util

// 을/를 조사 유틸
fun attachObjectParticle(word: String): String {
    val lastChar = word.lastOrNull() ?: return word
    val hasLastPiece = (lastChar.code - 0xAC00) % 28 != 0
    return if (hasLastPiece) "${word}을" else "${word}를"
}