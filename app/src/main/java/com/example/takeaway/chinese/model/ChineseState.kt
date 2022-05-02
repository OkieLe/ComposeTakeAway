package com.example.takeaway.chinese.model

import com.example.takeaway.common.UiState
import com.example.takeaway.common.model.HanziItem

data class ChineseState(
    val searchState: SearchState = SearchState.Result(),
    val allItems: List<HanziItem> = emptyList(),
    val searchMode: SearchMode = SearchMode.ZiMode
) : UiState

sealed interface SearchState {
    object Loading : SearchState
    data class Result(
        val items: List<HanziItem> = emptyList(),
        val page: Int = 0,
        val hasPrevious: Boolean = false,
        val hasNext: Boolean = false
    ) : SearchState
}

sealed interface SearchMode {
    object ZiMode : SearchMode
    object CiMode : SearchMode
}
