package com.example.takeaway.search.model

import com.example.takeaway.common.UiState
import com.example.takeaway.common.model.WordItem

data class SearchState(
    val status: SearchStatus = SearchStatus.Result(),
    val starState: StarState = StarState()
): UiState

sealed interface SearchStatus {
    object Loading : SearchStatus
    data class Result(val wordItems: List<WordItem> = emptyList()) : SearchStatus
}

data class StarState(
    val enabled: Boolean = false,
    val starred: Boolean = false
)
