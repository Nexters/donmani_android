package com.gowoon.motivation

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.domain.usecase.reward.GetInventoryUseCase
import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.GiftCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DecorationViewModel @Inject constructor(
    private val getInventoryUseCase: GetInventoryUseCase,
    private val getRecordListUseCase: GetRecordListUseCase
) : BaseViewModel<DecorationState, DecorationEvent, DecorationEffect>() {
    override fun createInitialState(): DecorationState {
        return DecorationState()
    }

    init {
        initialState()
    }

    override fun handleEvent(event: DecorationEvent) {
        when (event) {
            is DecorationEvent.OnClickCategory -> {
                // TODO 장착된 아이템 반영
                setState(
                    currentState.copy(
                        currentSelectedInventory = InventoryState(
                            currentCategory = event.clicked,
                            categoryItems = currentState.inventoryList[event.clicked] ?: listOf()
                        )
                    )
                )
            }

            is DecorationEvent.OnClickDone -> TODO()
        }
    }

    private fun initialState() {
        viewModelScope.launch {
            getRecordListUseCase(
                LocalDate.now().year, LocalDate.now().monthValue
            ).combine(getInventoryUseCase()) { records, inventory ->
                currentState.copy(
                    inventoryList = result.data,
                    currentSelectedInventory = InventoryState(
                        // TODO 현재 장착 아이템 반영
                        currentCategory = GiftCategory.BACKGROUND,
                        categoryItems = result.data[GiftCategory.BACKGROUND]
                            ?: listOf(),

                        )
                )
            }
            getInventoryUseCase().collect { result ->
                when (result) {
                    is Result.Error -> {
                        // TODO error handling
                    }

                    is Result.Success -> {
                        setState(
                            currentState.copy(
                                inventoryList = result.data,
                                currentSelectedInventory = InventoryState(
                                    // TODO 현재 장착 아이템 반영
                                    currentCategory = GiftCategory.BACKGROUND,
                                    categoryItems = result.data[GiftCategory.BACKGROUND]
                                        ?: listOf(),

                                    )
                            )
                        )
                    }
                }
            }
        }
    }
}

data class DecorationState(
    val inventoryList: Map<GiftCategory, List<Gift>> = mapOf(),
    val currentSelectedInventory: InventoryState = InventoryState()
) : UiState

data class InventoryState(
    val currentCategory: GiftCategory = GiftCategory.BACKGROUND,
    val categoryItems: List<Gift> = listOf(),
    val currentSelectItem: Gift? = null
)

sealed interface DecorationEvent : UiEvent {
    data class OnClickCategory(val clicked: GiftCategory) : DecorationEvent
    data object OnClickDone : DecorationEvent
}

sealed interface DecorationEffect : UiEffect