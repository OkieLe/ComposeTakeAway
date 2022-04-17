package com.example.takeaway.starred.model

import com.example.takeaway.common.UiState

data class StarredState(
    val status: StarredStatus = StarredStatus.Result()
): UiState

sealed interface StarredStatus {
    object Loading: StarredStatus
    data class Result(val wordItems: List<StarredWord> = emptyList()): StarredStatus
}

data class StarredWord(
    val text: String
)
