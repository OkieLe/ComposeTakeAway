package com.example.takeaway.search.model

import com.example.takeaway.data.model.WordInfo

data class SearchState(
    val status: SearchStatus = SearchStatus.Result()
)

sealed interface SearchStatus {
    object Loading: SearchStatus
    data class Result(val wordInfo: List<WordInfo> = emptyList()): SearchStatus
}
