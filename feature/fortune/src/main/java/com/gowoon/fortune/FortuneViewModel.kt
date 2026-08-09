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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/** 오늘을 포함해 최근 며칠치 운세를 불러올지 */
private const val FORTUNE_LIST_DAYS = 7

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
        val endDate = currentState.today
        val startDate = endDate.minusDays(FORTUNE_LIST_DAYS - 1L)
        viewModelScope.launch {
            // LocalDate.toString() 은 서버가 쓰는 ISO-8601(yyyy-MM-dd) 형식이다.
            val result = getFortuneListUseCase(
                startDate = startDate.toString(),
                endDate = endDate.toString()
            )
            when (result) {
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
        val today = currentState.today
        val yesterday = today.minusDays(1)
        val isFirstDayOfMonth = today.dayOfMonth == 1
        viewModelScope.launch {
            // 판정할 수 있는 필드만 채운다. 조회가 실패하면 null 로 남아 버튼이 노출되지 않는다. > 추후 예외에 대한 처리가 필요
            var hasToday: Boolean? = null
            var hasYesterday: Boolean? = null

            getRecordListUseCase(today.year, today.monthValue).collect { result ->
                if (result is Result.Success) {
                    hasToday = result.data.records.any { it.date == today }
                    // 1일이면 어제는 지난달이라 이 응답으로는 판단할 수 없다. 아래에서 따로 조회한다.
                    if (!isFirstDayOfMonth) {
                        hasYesterday = result.data.records.any { it.date == yesterday }
                    }
                }
            }
            if (isFirstDayOfMonth) {
                getRecordListUseCase(yesterday.year, yesterday.monthValue).collect { result ->
                    if (result is Result.Success) {
                        hasYesterday = result.data.records.any { it.date == yesterday }
                    }
                }
            }

            setState(
                currentState.copy(
                    hasTodayRecord = hasToday,
                    hasYesterdayRecord = hasYesterday
                )
            )
        }
    }
}

data class FortuneState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val fortunes: List<Fortune> = emptyList(),
    // null = 아직 모름 (조회 전이거나 조회 실패), false = 기록 없음, true = 기록 있음
    val hasTodayRecord: Boolean? = null,
    val hasYesterdayRecord: Boolean? = null,
    // 화면 곳곳에서 LocalDate.now() 를 각자 부르면 recomposition 시점에 따라 값이 엇갈린다.
    // 진입 시 한 번 고정해두고 모두 이 값을 본다.
    val today: LocalDate = LocalDate.now()
) : UiState {
    // 히스토리 제목에 쓰는 달. fortunes 는 날짜 오름차순이라 마지막이 가장 최근이다.
    val displayMonth: Int
        get() = fortunes.lastOrNull()?.date?.monthValue ?: today.monthValue

    // 어제·오늘 중 기록이 없는 게 확실한 날에만 기록하기 버튼을 노출한다.
    // 모르는 상태(null)에서 헛걸음시키지 않도록 == false 로 좁혀서 본다.
    fun showRecordButton(date: LocalDate): Boolean = when (date) {
        today -> hasTodayRecord == false
        today.minusDays(1) -> hasYesterdayRecord == false
        else -> false
    }
}

sealed interface FortuneEvent : UiEvent {
    data object Retry : FortuneEvent
}

sealed interface FortuneEffect : UiEffect
