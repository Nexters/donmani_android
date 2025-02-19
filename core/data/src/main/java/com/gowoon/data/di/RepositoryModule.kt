package com.gowoon.data.di

import com.gowoon.data.repository.RecordRepositoryImpl
import com.gowoon.data.repository.TooltipRepositoryImpl
import com.gowoon.data.repository.UserRepositoryImpl
import com.gowoon.domain.repository.RecordRepository
import com.gowoon.domain.repository.TooltipRepository
import com.gowoon.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    fun bindTooltipRepository(tooltipRepositoryImpl: TooltipRepositoryImpl): TooltipRepository

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindRecordRepository(recordRepositoryImpl: RecordRepositoryImpl): RecordRepository
}