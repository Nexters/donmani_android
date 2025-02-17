package com.gowoon.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.gowoon.common.di.JsonEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.serialization.json.Json

@Composable
fun rememberHiltJson(): Json {
    val context = LocalContext.current.applicationContext
    return remember {
        EntryPointAccessors.fromApplication(context, JsonEntryPoint::class.java).getJson()
    }
}