package com.gowoon.ui.component

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.vector.PathParser

fun DefaultShape(): Shape {
    val path = PathParser().parsePathString("M 51.703 8.82 C 129.283 -2.94 208.193 -2.94 285.763 8.82 C 290.353 9.52 293.733 13.45 293.733 18.08 L 293.733 39.92 C 293.733 44.34 290.153 47.92 285.733 47.92 L 284.653 47.92 C 284.653 60.18 285.653 59.49 288.233 61.78 C 288.653 62.15 289.093 62.54 289.553 62.92 C 307.583 73.27 319.733 92.72 319.733 115 L 319.733 340.15 C 319.733 373.28 292.873 400.15 259.733 400.15 L 79.733 400.15 C 46.593 400.15 19.733 373.28 19.733 340.15 L 19.733 115 C 19.733 92.72 31.883 73.27 49.913 62.92 C 50.373 62.54 50.813 62.15 51.233 61.78 C 53.813 59.49 54.813 60.18 54.813 47.92 L 51.733 47.92 C 47.313 47.92 43.733 44.34 43.733 39.92 L 43.733 18.1 C 43.733 13.46 47.123 9.53 51.703 8.83 Z").toPath()
    return GenericShape { size, _ ->
        val scaleX = size.width / 328f
        val scaleY = size.height / 391f
        val matrix = Matrix().apply {
            scale(scaleX, scaleY)
        }
        val scaledPath = path.copy().apply {
            transform(matrix)
        }

        addPath(scaledPath)
    }
}

fun BeadShape(): Shape {
    val path = PathParser().parsePathString("M 165.942 0 C 257.303 0 331.883 74.58 331.883 165.942 C 331.883 217.674 307.967 264.013 270.64 294.495 C 279.684 299.369 287.224 306.94 292.037 316.461 C 309.346 350.614 284.528 391 246.247 391 L 86.673 391 C 48.393 391 23.574 350.614 40.884 316.461 C 45.582 307.168 52.894 299.722 61.658 294.837 C 24.093 264.366 0 217.861 0 165.942 C 0 74.58 74.58 0 165.942 0 Z").toPath()
    return GenericShape { size, _ ->
        val scaleX = size.width / 328f
        val scaleY = size.height / 391f
        val matrix = Matrix().apply {
            scale(scaleX, scaleY)
        }
        val scaledPath = path.copy().apply {
            transform(matrix)
        }
        addPath(scaledPath)
    }
}

fun SmoothShape(): Shape {
    val path = PathParser().parsePathString("M 191.33 0 C 203.2 0 212.83 9.63 212.83 21.5 C 212.83 30.74 207 38.61 198.83 41.65 L 198.83 53.75 C 210.47 48.15 223.48 45.15 237.27 45.15 C 261.35 45.18 284.43 55.83 301.45 74.75 C 318.47 93.69 328.05 119.35 328.08 146.13 C 328.08 210.31 279.88 267.62 236.62 306.12 L 245.72 340.88 C 252.35 366.23 233.22 391 207.02 391 L 120.64 391 C 94.44 391 75.31 366.23 81.94 340.88 L 91.12 305.81 C 47.93 267.31 0 210.15 0 146.13 C 0.03 119.35 9.6 93.69 26.63 74.76 C 43.65 55.83 66.73 45.18 90.81 45.15 C 104.83 45.15 118.04 48.25 129.83 54.03 L 129.83 41.65 C 121.65 38.61 115.83 30.74 115.83 21.5 C 115.83 9.63 125.45 0 137.33 0 L 191.33 0 Z").toPath()
    return GenericShape { size, _ ->
        val scaleX = size.width / 328f
        val scaleY = size.height / 391f
        val matrix = Matrix().apply {
            scale(scaleX, scaleY)
        }
        val scaledPath = path.copy().apply {
            transform(matrix)
        }
        addPath(scaledPath)
    }
}