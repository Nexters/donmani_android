package com.gowoon.data.di

import com.gowoon.data.repository.TooltipRepositoryImpl
import com.gowoon.domain.repository.TooltipRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    fun bindTooltipRepository(tooltipRepositoryImpl: TooltipRepositoryImpl): TooltipRepository
}