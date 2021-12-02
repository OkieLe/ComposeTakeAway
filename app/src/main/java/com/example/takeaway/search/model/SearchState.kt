package com.example.takeaway.search.model

data class SearchState(
    val status: SearchStatus = SearchStatus.Result()
)

sealed interface SearchStatus {
    object Loading: SearchStatus
    data class Result(val wordItems: List<WordItem> = emptyList()): SearchStatus
}

data class WordItem(
    val text: String,
    val phonetics: List<PhoneticItem>,
    val meanings: List<MeaningItem>
)

data class PhoneticItem(
    val audio: String,
    val text: String
)

data class MeaningItem(
    val definitions: List<DefinitionItem>,
    val partOfSpeech: String
)

data class DefinitionItem(
    val text: String,
    val example: String,
    val synonyms: String,
    val antonyms: String
)
