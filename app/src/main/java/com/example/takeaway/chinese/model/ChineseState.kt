package com.example.takeaway.chinese.model

import com.example.takeaway.common.UiState
import com.example.takeaway.common.model.HanziItem

data class ChineseState(
    val status: SearchStatus = SearchStatus.Result(),
    val searchMode: SearchMode = SearchMode.ZiMode
): UiState

sealed interface SearchStatus {
    object Loading: SearchStatus
    data class Result(val items: List<HanziItem> = emptyList()): SearchStatus
}

sealed interface SearchMode {
    object ZiMode: SearchMode
    object CiMode: SearchMode
}
