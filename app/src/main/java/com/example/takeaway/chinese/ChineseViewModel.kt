package com.example.takeaway.chinese

import androidx.lifecycle.viewModelScope
import com.example.takeaway.chinese.model.ChineseAction
import com.example.takeaway.chinese.model.ChineseEvent
import com.example.takeaway.chinese.model.ChineseState
import com.example.takeaway.chinese.model.SearchError
import com.example.takeaway.chinese.model.SearchMode
import com.example.takeaway.chinese.model.SearchState
import com.example.takeaway.common.BaseViewModel
import com.example.takeaway.common.mapper.HanziItemMapper
import com.example.takeaway.common.model.HanziItem
import com.example.takeaway.data.HanziRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class ChineseViewModel @Inject constructor(
    private val hanziRepository: HanziRepository,
    private val hanziItemMapper: HanziItemMapper
): BaseViewModel<ChineseAction, ChineseState, ChineseEvent>() {

    companion object {
        private val validKeywordRegex = "^[a-zA-Z]+\$|^[\\u4e00-\\u9fa5]+\$".toRegex()
        private val spaceRegex = "\\s".toRegex()
        private const val PAGE_SIZE = 15
    }

    override val initialState: ChineseState
        get() = ChineseState()

    override fun submit(action: ChineseAction) {
        when (action) {
            is ChineseAction.Search -> search(action.word)
            is ChineseAction.ChangeMode -> changeMode()
            ChineseAction.ShowNext -> showNextPage()
            ChineseAction.ShowPrevious -> showPreviousPage()
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
            updateState(state.value.copy(searchState = SearchState.Loading, allItems = emptyList()))
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
            updateState(state.value.copy(searchState = SearchState.Loading, allItems = emptyList()))
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

    private fun showNextPage() {
        state.value.searchState.takeIf { it is SearchState.Result }?.let {
            showPage((it as SearchState.Result).page + 1)
        }
    }

    private fun showPreviousPage() {
        state.value.searchState.takeIf { it is SearchState.Result }?.let {
            showPage((it as SearchState.Result).page - 1)
        }
    }

    private fun showPage(page: Int) {
        val hanziItems = state.value.allItems
        val pageStart = max(page * PAGE_SIZE, 0)
        val pageCount = min(PAGE_SIZE, hanziItems.size - pageStart)
        val hasPrevious = pageStart > 0
        val hasNext = (page + 1) * PAGE_SIZE < hanziItems.size
        updateState(
            state.value.copy(
                searchState = SearchState.Result(
                    hanziItems.subList(pageStart, pageStart + pageCount),
                    page = page, hasPrevious = hasPrevious, hasNext = hasNext
                )
            )
        )
    }

    private fun updateSearchResult(hanziItems: List<HanziItem>) {
        updateState(state.value.copy(allItems = hanziItems))
        if (hanziItems.size > PAGE_SIZE) {
            sendEvent(ChineseEvent.ShowPartial(hanziItems.size))
        }
        showPage(0)
    }

    private fun changeMode() {
        when (state.value.searchMode) {
            SearchMode.CiMode -> updateState(state.value.copy(
                searchMode = SearchMode.ZiMode,
                searchState = SearchState.Result(emptyList()),
                allItems = emptyList()
            ))
            SearchMode.ZiMode -> updateState(state.value.copy(
                searchMode = SearchMode.CiMode,
                searchState = SearchState.Result(emptyList()),
                allItems = emptyList()
            ))
        }
    }
}
