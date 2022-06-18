package com.example.takeaway.common.model

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
