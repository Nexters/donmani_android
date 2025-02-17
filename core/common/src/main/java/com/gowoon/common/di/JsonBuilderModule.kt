package com.gowoon.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@EntryPoint
@InstallIn(SingletonComponent::class)
interface JsonEntryPoint {
    fun getJson(): Json
}

@Module
@InstallIn(SingletonComponent::class)
object JsonBuilderModule {
    @Singleton
    @Provides
    fun providesJson(): Json = Json {
        useArrayPolymorphism = true
        ignoreUnknownKeys = true
    }
}