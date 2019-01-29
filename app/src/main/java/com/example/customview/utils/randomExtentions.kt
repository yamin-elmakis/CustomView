package com.example.customview.utils

import android.graphics.Color
import kotlin.random.Random

fun Random.nextColor(): Int {
    val alpha = nextInt(255)
    val red = nextInt(255)
    val green = nextInt(255)
    val blue = nextInt(255)
    return Color.argb(alpha, red, green, blue)
}