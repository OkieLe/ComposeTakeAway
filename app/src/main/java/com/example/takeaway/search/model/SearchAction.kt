package com.example.takeaway.search.model

import com.example.takeaway.common.UiAction

sealed interface SearchAction: UiAction {
    data class Search(val word: String): SearchAction
    object Star: SearchAction
    object UnStar: SearchAction
    data class Play(val mediaUrl: String) : SearchAction
}
