package com.gowoon.fortune

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.fortune.GetFortuneListUseCase
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.model.fortune.Fortune
import com.gowoon.model.record.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FortuneViewModel @Inject constructor(
    private val getFortuneListUseCase: GetFortuneListUseCase,
    private val getRecordListUseCase: GetRecordListUseCase
) : BaseViewModel<FortuneState, FortuneEvent, FortuneEffect>() {
    override fun createInitialState(): FortuneState = FortuneState()

    init {
        load()
    }

    override fun handleEvent(event: FortuneEvent) {
        when (event) {
            FortuneEvent.Retry -> load()
        }
    }

    private fun load() {
        setState(currentState.copy(isLoading = true, errorMessage = null))
        loadFortunes()
        loadRecordState()
    }

    private fun loadFortunes() {
        viewModelScope.launch {
            when (val result = getFortuneListUseCase()) {
                is Result.Success -> {
                    setState(
                        currentState.copy(
                            isLoading = false,
                            fortunes = result.data.sortedBy { it.date },
                            errorMessage = null
                        )
                    )
                }

                is Result.Error -> {
                    setState(
                        currentState.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "unknown error"
                        )
                    )
                }
            }
        }
    }

    private fun loadRecordState() {
        val today = LocalDate.now()
        viewModelScope.launch {
            // getRecordList 는 값을 하나만 emit 하고 끝나는 flow 다.
            // first()/take() 로 중간에 끊으면 AbortFlowException 이 repository 의 catch 블록에 잡혀
            // 재emit 되면서 Flow exception transparency 위반으로 크래시가 난다.
            getRecordListUseCase(today.year, today.monthValue).collect { result ->
                if (result is Result.Success) {
                    setRecordState(result.data.records)
                }
            }
            if (today.dayOfMonth == 1) {
                val yesterday = today.minusDays(1)
                getRecordListUseCase(yesterday.year, yesterday.monthValue).collect { result ->
                    if (result is Result.Success) {
                        setRecordState(result.data.records)
                    }
                }
            }
        }
    }

    private fun setRecordState(records: List<Record>) {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        setState(
            currentState.copy(
                hasTodayRecord = currentState.hasTodayRecord || records.any { it.date == today },
                hasYesterdayRecord = currentState.hasYesterdayRecord || records.any { it.date == yesterday }
            )
        )
    }
}

data class FortuneState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val fortunes: List<Fortune> = emptyList(),
    val hasTodayRecord: Boolean = false,
    val hasYesterdayRecord: Boolean = false
) : UiState

sealed interface FortuneEvent : UiEvent {
    data object Retry : FortuneEvent
}

sealed interface FortuneEffect : UiEffect
