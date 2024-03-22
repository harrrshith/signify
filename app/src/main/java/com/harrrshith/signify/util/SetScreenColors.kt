package com.harrrshith.signify.util

import android.content.Context
import androidx.core.content.ContextCompat

fun Context.setStatusBarColor(color: Int) = ContextCompat.getColor(this, color)

fun Context.seNavigationBarColor(color: Int) = ContextCompat.getColor(this, color)