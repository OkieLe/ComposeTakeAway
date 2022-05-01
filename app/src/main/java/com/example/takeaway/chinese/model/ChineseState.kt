package com.example.takeaway.chinese.model

import com.example.takeaway.common.UiState
import com.example.takeaway.common.model.HanziBrief
import com.example.takeaway.common.model.HanziItem

data class ChineseState(
    val searchStatus: SearchStatus = SearchStatus.Result(),
    val moreResultState: MoreResultState = MoreResultState(),
    val searchMode: SearchMode = SearchMode.ZiMode
) : UiState

sealed interface SearchStatus {
    object Loading : SearchStatus
    data class Result(val items: List<HanziItem> = emptyList()) : SearchStatus
}

data class MoreResultState(
    val showPartial: Boolean = false,
    val allItems: List<HanziBrief> = emptyList()
)

sealed interface SearchMode {
    object ZiMode : SearchMode
    object CiMode : SearchMode
}
