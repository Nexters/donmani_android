package com.gowoon.motivation

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.domain.usecase.reward.GetInventoryUseCase
import com.gowoon.domain.usecase.reward.HideDecorationFirstBottomSheetUseCase
import com.gowoon.domain.usecase.reward.ShowDecorationFirstBottomSheetUseCase
import com.gowoon.domain.usecase.reward.UpdateDecorationUseCase
import com.gowoon.model.reward.DecorationPosition
import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.GiftCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DecorationViewModel @Inject constructor(
    private val getInventoryUseCase: GetInventoryUseCase,
    private val getRecordListUseCase: GetRecordListUseCase,
    private val updateDecorationUseCase: UpdateDecorationUseCase,
    private val showDecorationFirstBottomSheetUseCase: ShowDecorationFirstBottomSheetUseCase,
    private val hideDecorationFirstBottomSheetUseCase: HideDecorationFirstBottomSheetUseCase
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
                setState(currentState.copy(currentSelectedCategory = event.clicked))
            }

            is DecorationEvent.OnClickItem -> {
                Napier.d("clicked item ${event.category} - ${event.item?.name} - ${event.item?.resourceUrl}")
                setState(
                    currentState.copy(
                        savedItems = currentState.savedItems.toMutableMap().apply {
                            put(event.category, event.item)
                        }
                    )
                )
            }

            is DecorationEvent.ShowDialog -> {
                setState(currentState.copy(showDialog = event.show))
            }

            is DecorationEvent.SaveDecoration -> {
                requestUpdateDecoration()
            }

            is DecorationEvent.HideFirstBottomSheet -> {
                hideFirstBottomSheet()
            }
        }
    }

    private fun initialState() {
        viewModelScope.launch {
            getRecordListUseCase(LocalDate.now().year, LocalDate.now().monthValue).combine(
                getInventoryUseCase()
            ) { bbsState, inventory ->
                if (bbsState is Result.Success && inventory is Result.Success) {
                    Result.Success(
                        currentState.copy(
                            inventoryList = inventory.data,
                            savedItems = mutableMapOf<GiftCategory, Gift?>().apply {
                                put(
                                    GiftCategory.BACKGROUND,
                                    inventory.data[GiftCategory.BACKGROUND]?.find { it.id == bbsState.data.background?.id })
                                put(
                                    GiftCategory.EFFECT,
                                    inventory.data[GiftCategory.EFFECT]?.find { it.id == bbsState.data.effect?.id })
                                put(
                                    GiftCategory.DECORATION,
                                    inventory.data[GiftCategory.DECORATION]?.find { it.id == bbsState.data.decoration?.id })
                                put(
                                    GiftCategory.CASE,
                                    inventory.data[GiftCategory.CASE]?.find { it.id == bbsState.data.case?.id })
                                put(
                                    GiftCategory.BGM,
                                    inventory.data[GiftCategory.BGM]?.find { it.id == bbsState.data.bgm?.id })
                            }
                        )
                    )
                } else {
                    Result.Error(message = (bbsState as? Result.Error)?.message + (inventory as? Result.Error)?.message)
                }
            }.collect {
                when (it) {
                    is Result.Error -> {
                        // TODO error handling
                    }

                    is Result.Success -> {
                        setState(it.data)
                    }
                }
            }
        }
        viewModelScope.launch {
            showDecorationFirstBottomSheetUseCase().collect {
                when (it) {
                    is Result.Error -> {
                        // TODO error handling
                    }

                    is Result.Success -> {
                        if (it.data) {
                            setState(currentState.copy(showFirstOpenBottomSheet = true))
                        }
                    }
                }
            }
        }
    }

    private fun requestUpdateDecoration() {
        viewModelScope.launch {
            if (
                updateDecorationUseCase(
                    backgroundId = currentState.savedItems[GiftCategory.BACKGROUND]?.id ?: "",
                    effectId = currentState.savedItems[GiftCategory.EFFECT]?.id ?: "",
                    decorationId = currentState.savedItems[GiftCategory.DECORATION]?.id ?: "",
                    caseId = currentState.savedItems[GiftCategory.CASE]?.id ?: "",
                    bgmId = currentState.savedItems[GiftCategory.BGM]?.id ?: ""
                ) is Result.Error
            ) {
                // TODO error handling
            }
        }
    }

    private fun hideFirstBottomSheet() {
        viewModelScope.launch {
            if (hideDecorationFirstBottomSheetUseCase() is Result.Error) {
                // TODO error handling
            }
        }
    }

    fun getDecorationPosition(decorationId: String): DecorationPosition {
        return when (decorationId) {
            "20" -> DecorationPosition.BOTTOM_END
            "23" -> DecorationPosition.ABOVE_BOTTLE
            else -> DecorationPosition.TOP_START
        }
    }
}

data class DecorationState(
    val inventoryList: Map<GiftCategory, List<Gift>> = mapOf(),
    val currentSelectedCategory: GiftCategory = GiftCategory.BACKGROUND,
    val savedItems: Map<GiftCategory, Gift?> = mapOf(),
    val showDialog: Boolean = false,
    val showFirstOpenBottomSheet: Boolean = false
) : UiState

data class InventoryState(
    val currentCategory: GiftCategory,
    val categoryItems: List<Gift>,
    val currentSelectItem: String?
)

sealed interface DecorationEvent : UiEvent {
    data class OnClickCategory(val clicked: GiftCategory) : DecorationEvent
    data class OnClickItem(val category: GiftCategory, val item: Gift?) : DecorationEvent
    data class ShowDialog(val show: Boolean) : DecorationEvent
    data object SaveDecoration : DecorationEvent
    data object HideFirstBottomSheet : DecorationEvent
}

sealed interface DecorationEffect : UiEffect