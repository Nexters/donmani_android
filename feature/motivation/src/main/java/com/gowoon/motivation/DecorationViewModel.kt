package com.gowoon.motivation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.domain.usecase.reward.GetInventoryUseCase
import com.gowoon.domain.usecase.reward.HideDecorationFirstBottomSheetUseCase
import com.gowoon.domain.usecase.reward.ReadHiddenItemUseCase
import com.gowoon.domain.usecase.reward.ShowDecorationFirstBottomSheetUseCase
import com.gowoon.domain.usecase.reward.UpdateDecorationUseCase
import com.gowoon.model.common.BBSState
import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.GiftCategory
import com.gowoon.motivation.navigation.DecorationNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DecorationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInventoryUseCase: GetInventoryUseCase,
    private val getRecordListUseCase: GetRecordListUseCase,
    private val updateDecorationUseCase: UpdateDecorationUseCase,
    private val showDecorationFirstBottomSheetUseCase: ShowDecorationFirstBottomSheetUseCase,
    private val hideDecorationFirstBottomSheetUseCase: HideDecorationFirstBottomSheetUseCase,
    private val readHiddenItemUseCase: ReadHiddenItemUseCase
) : BaseViewModel<DecorationState, DecorationEvent, DecorationEffect>() {
    private val selectedCategory = savedStateHandle.toRoute<DecorationNavigationRoute>().selected
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
                setState(
                    currentState.copy(
                        bbsState = currentState.bbsState.copy(records = currentState.bbsState.records.toMutableList()),
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
                requestUpdateDecoration(event.callback)
            }

            is DecorationEvent.HideFirstBottomSheet -> {
                hideFirstBottomSheet()
            }

            is DecorationEvent.HideHiddenBottomSheet -> {
                hideHiddenItemBottomSheet()
            }
        }
    }

    private fun initialState() {
        viewModelScope.launch {
            getRecordListUseCase(LocalDate.now().year, LocalDate.now().monthValue).combine(
                getInventoryUseCase()
            ) { bbsState, inventory ->
                if (bbsState is Result.Success && inventory is Result.Success) {
                    val hidden =
                        inventory.data[GiftCategory.DECORATION]?.find { it.hidden }?.isNew == true
                    Result.Success(
                        currentState.copy(
                            inventoryList = inventory.data,
                            currentSelectedCategory = selectedCategory?.let {
                                GiftCategory.valueOf(
                                    it
                                )
                            } ?: GiftCategory.BACKGROUND,
                            bbsState = bbsState.data,
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
//                                put(
//                                    GiftCategory.BGM,
//                                    inventory.data[GiftCategory.BGM]?.find { it.id == bbsState.data.bgm?.id })
                            },
                            showHiddenGiftBottomSheet = hidden ?: false
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
                        setState(currentState.copy(showFirstOpenBottomSheet = it.data))
                    }
                }
            }
        }
    }

    private fun requestUpdateDecoration(callback: () -> Unit) {
        viewModelScope.launch {
            when (
                updateDecorationUseCase(
                    backgroundId = currentState.savedItems[GiftCategory.BACKGROUND]?.id ?: "",
                    effectId = currentState.savedItems[GiftCategory.EFFECT]?.id ?: "",
                    decorationId = currentState.savedItems[GiftCategory.DECORATION]?.id ?: "",
                    caseId = currentState.savedItems[GiftCategory.CASE]?.id ?: "",
//                    bgmId = currentState.savedItems[GiftCategory.BGM]?.id ?: ""
                )
            ) {
                is Result.Error -> {
                    // TODO error handling
                }

                is Result.Success -> {
                    callback()
                }
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

    private fun hideHiddenItemBottomSheet() {
        viewModelScope.launch {
            readHiddenItemUseCase().also {
                setState(currentState.copy(showHiddenGiftBottomSheet = false))
            }
        }
    }

    fun isChangedDecorationState(
        origin: Map<GiftCategory, Gift?>,
        current: Map<GiftCategory, Gift?>
    ): Boolean {
        return !origin.all { current[it.key]?.id == it.value?.id }
    }
}

data class DecorationState(
    val inventoryList: Map<GiftCategory, List<Gift>> = mapOf(),
    val currentSelectedCategory: GiftCategory = GiftCategory.BACKGROUND,
    val bbsState: BBSState = BBSState(),
    val savedItems: Map<GiftCategory, Gift?> = mapOf(),
    val showDialog: Boolean = false,
    val showFirstOpenBottomSheet: Boolean = false,
    val showHiddenGiftBottomSheet: Boolean = false
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
    data class SaveDecoration(val callback: () -> Unit) : DecorationEvent
    data object HideFirstBottomSheet : DecorationEvent
    data object HideHiddenBottomSheet : DecorationEvent
}

sealed interface DecorationEffect : UiEffect