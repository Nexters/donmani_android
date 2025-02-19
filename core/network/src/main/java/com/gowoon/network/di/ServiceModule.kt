package com.gowoon.network.di

import com.gowoon.network.service.ExpenseService
import com.gowoon.network.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun providesExpenseService(retrofit: Retrofit): ExpenseService {
        return retrofit.create(ExpenseService::class.java)
    }
}