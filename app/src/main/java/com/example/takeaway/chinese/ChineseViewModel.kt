package com.example.takeaway.chinese

import androidx.lifecycle.viewModelScope
import com.example.takeaway.chinese.model.ChineseAction
import com.example.takeaway.chinese.model.ChineseEvent
import com.example.takeaway.chinese.model.ChineseState
import com.example.takeaway.chinese.model.MoreResultState
import com.example.takeaway.chinese.model.SearchError
import com.example.takeaway.chinese.model.SearchMode
import com.example.takeaway.chinese.model.SearchStatus
import com.example.takeaway.common.BaseViewModel
import com.example.takeaway.common.mapper.HanziItemMapper
import com.example.takeaway.common.model.HanziItem
import com.example.takeaway.data.HanziRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class ChineseViewModel @Inject constructor(
    private val hanziRepository: HanziRepository,
    private val hanziItemMapper: HanziItemMapper
): BaseViewModel<ChineseAction, ChineseState, ChineseEvent>() {

    companion object {
        private val validKeywordRegex = "^[a-zA-Z]+\$|^[\\u4e00-\\u9fa5]+\$".toRegex()
        private val spaceRegex = "\\s".toRegex()
        private const val MAX_DISPLAY_COUNT = 12
    }

    override val initialState: ChineseState
        get() = ChineseState()

    override fun submit(action: ChineseAction) {
        when (action) {
            is ChineseAction.Search -> search(action.word)
            is ChineseAction.ChangeMode -> changeMode()
        }
    }

    private fun search(word: String) {
        val trimmedWord = word.replace(spaceRegex, "")
        if (trimmedWord.isBlank() || validKeywordRegex.matches(trimmedWord).not()) {
            sendEvent(ChineseEvent.ShowError(SearchError.InvalidInput))
        } else {
            when (state.value.searchMode) {
                SearchMode.CiMode -> searchCi(trimmedWord)
                SearchMode.ZiMode -> searchZi(trimmedWord)
            }
        }
    }

    private fun searchZi(word: String) {
        viewModelScope.launch {
            updateState(state.value.copy(searchStatus = SearchStatus.Loading, moreResultState = MoreResultState()))
            hanziRepository.searchZi(word).let {
                when {
                    it.isNotEmpty() -> updateSearchResult(it.map(hanziItemMapper::fromZiInfo))
                    else -> {
                        sendEvent(ChineseEvent.ShowError(SearchError.NotFound))
                        updateSearchResult(emptyList())
                    }
                }
            }
        }
    }

    private fun searchCi(word: String) {
        viewModelScope.launch {
            updateState(state.value.copy(searchStatus = SearchStatus.Loading, moreResultState = MoreResultState()))
            hanziRepository.searchCi(word).let {
                when {
                    it.isNotEmpty() -> updateSearchResult(it.map(hanziItemMapper::fromCiInfo))
                    else -> {
                        sendEvent(ChineseEvent.ShowError(SearchError.NotFound))
                        updateSearchResult(emptyList())
                    }
                }
            }
        }
    }

    private fun updateSearchResult(hanziItems: List<HanziItem>) {
        val showPartial = MAX_DISPLAY_COUNT < hanziItems.size
        updateState(
            state.value.copy(
                searchStatus = SearchStatus.Result(
                    hanziItems.subList(0, min(MAX_DISPLAY_COUNT, hanziItems.size))
                ),
                moreResultState = MoreResultState(
                    showPartial = showPartial,
                    allItems = hanziItems.map(hanziItemMapper::toHanziBrief)
                )
            )
        )
        if (showPartial) sendEvent(ChineseEvent.ShowPartial(hanziItems.size))
    }

    private fun changeMode() {
        when (state.value.searchMode) {
            SearchMode.CiMode -> updateState(state.value.copy(
                searchMode = SearchMode.ZiMode,
                moreResultState = MoreResultState(),
                searchStatus = SearchStatus.Result(emptyList())
            ))
            SearchMode.ZiMode -> updateState(state.value.copy(
                searchMode = SearchMode.CiMode,
                moreResultState = MoreResultState(),
                searchStatus = SearchStatus.Result(emptyList())
            ))
        }
    }
}
