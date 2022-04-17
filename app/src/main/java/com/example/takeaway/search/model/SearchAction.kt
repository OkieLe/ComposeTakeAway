package com.example.takeaway.search.model

sealed interface SearchAction {
    data class Search(val word: String): SearchAction
    object Star: SearchAction
    object UnStar: SearchAction
}
