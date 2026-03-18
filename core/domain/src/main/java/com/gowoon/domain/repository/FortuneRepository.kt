package com.gowoon.domain.repository

import com.gowoon.domain.common.Result
import com.gowoon.model.fortune.Fortune
import com.gowoon.model.fortune.FortuneReadType
import kotlinx.coroutines.flow.Flow

interface FortuneRepository {
    suspend fun getFortuneDialogLastReadDate(): Flow<Result<String>>
    suspend fun updateFortuneDialogLastReadDate(fortuneReadType: FortuneReadType): Result<Unit>
    suspend fun getFortune(): Flow<Result<Fortune>>
}