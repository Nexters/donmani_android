package com.gowoon.data.repository

import com.gowoon.data.mapper.toModel
import com.gowoon.datastore.RewardDataSource
import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RewardRepository
import com.gowoon.model.reward.Feedback
import com.gowoon.model.reward.Gift
import com.gowoon.network.di.DeviceId
import com.gowoon.network.service.RewardService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RewardRepositoryImpl @Inject constructor(
    @DeviceId private val deviceId: String,
    private val rewardDataSource: RewardDataSource,
    private val rewardService: RewardService
) : RewardRepository {
    override suspend fun getShowRewardReceivedTooltip(): Flow<Result<Boolean>> = try {
        rewardDataSource.getShowRewardReceivedTooltip().map { Result.Success(it) }
    } catch (e: Exception) {
        flow { emit(Result.Error(message = e.message)) }
    }

    override suspend fun setShowRewardReceivedTooltip(state: Boolean): Result<Unit> = try {
        rewardDataSource.setShowRewardReceivedTooltip(state).let { Result.Success(Unit) }
    } catch (e: Exception) {
        Result.Error(message = e.message)
    }

    override suspend fun getFeedbackSummary(): Flow<Result<Triple<Boolean, Boolean, Int>>> = flow {
        try {
            emit(
                rewardService.getFeedbackSummary(deviceId).let { result ->
                    if (result.isSuccessful) {
                        result.body()?.let { body ->
                            Result.Success(
                                Triple(
                                    body.responseData.isNotOpened,
                                    body.responseData.isFirstOpen,
                                    body.responseData.totalCount
                                )
                            )
                        } ?: Result.Error(message = "empty body")
                    } else {
                        Result.Error(code = result.code(), message = result.message())
                    }
                }
            )
        } catch (e: Exception) {
            emit(Result.Error(message = e.message))
        }
    }

    override suspend fun getShowFirstAccessRewardBottomSheet(): Flow<Result<Boolean>> = try {
        rewardDataSource.getShowFirstAccessRewardBottomSheet().map { Result.Success(it) }
    } catch (e: Exception) {
        flow { emit(Result.Error(message = e.message)) }
    }

    override suspend fun setShowFirstAccessRewardBottomSheet(state: Boolean): Result<Unit> = try {
        rewardDataSource.setShowFirstAccessRewardBottomSheet(state).let { Result.Success(Unit) }
    } catch (e: Exception) {
        Result.Error(message = e.message)
    }

    override suspend fun getFeedback(): Flow<Result<Feedback>> = flow {
        try {
            emit(
                rewardService.getFeedback(deviceId).let { result ->
                    if (result.isSuccessful) {
                        result.body()?.let { body ->
                            Result.Success(
                                body.responseData.toModel()
                            )
                        } ?: Result.Error(message = "empty body")
                    } else {
                        Result.Error(code = result.code(), message = result.message())
                    }
                }
            )
        } catch (e: Exception) {
            emit(Result.Error(message = e.message))
        }
    }

    override suspend fun getGiftCount(): Flow<Result<Int>> = flow {
        try {
            emit(
                rewardService.getGiftCount(deviceId).let { result ->
                    if (result.isSuccessful) {
                        result.body()?.let { body ->
                            Result.Success(body.responseData)
                        } ?: Result.Error(message = "empty body")
                    } else {
                        Result.Error(code = result.code(), message = result.message())
                    }
                }
            )
        } catch (e: Exception) {
            emit(Result.Error(message = e.message))
        }
    }

    override suspend fun openGift(): Flow<Result<List<Gift>>> = flow {
        try {
            emit(
                rewardService.openRewardList(deviceId).let { result ->
                    if (result.isSuccessful) {
                        result.body()?.let { body ->
                            Result.Success(body.responseData.map { it.toModel() })
                        } ?: Result.Error(message = "empty body")
                    } else {
                        Result.Error(code = result.code(), message = result.message())
                    }
                }
            )
        } catch (e: Exception) {
            emit(Result.Error(message = e.message))
        }
    }
}