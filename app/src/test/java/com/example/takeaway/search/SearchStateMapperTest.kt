package com.example.takeaway.search

import com.example.takeaway.data.model.Definition
import com.example.takeaway.data.model.Meaning
import com.example.takeaway.data.model.Phonetic
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.search.model.DefinitionItem
import com.example.takeaway.search.model.MeaningItem
import com.example.takeaway.search.model.PhoneticItem
import com.example.takeaway.search.model.WordItem
import io.kotest.assertions.eq.eq
import org.junit.Test

class SearchStateMapperTest {

    private val helloInfo = WordInfo(
        word = "hello",
        meanings = listOf(
            Meaning(partOfSpeech = "exclamation", definitions = listOf(
                Definition(
                    definition = "used as a greeting or to begin a phone conversation.",
                    example = "hello there, Katie!",
                    synonyms = emptyList(),
                    antonyms = emptyList()
                )
            ))
        ),
        phonetics = listOf(
            Phonetic(text = "həˈləʊ", audio = "")
        )
    )

    private val helloItem = WordItem(
        text ="hello",
        meanings = listOf(
            MeaningItem(partOfSpeech = "exclamation", definitions = listOf(
                DefinitionItem(
                    text = "used as a greeting or to begin a phone conversation.",
                    example = "hello there, Katie!",
                    synonyms = "",
                    antonyms = ""
                )
            ))
        ),
        phonetics = listOf(
            PhoneticItem(text = "həˈləʊ", audio = "")
        )
    )

    private val searchStateMapper = SearchStateMapper()

    @Test
    fun `given a 'WordInfo' when 'toWordItem' then return 'WordItem'`() {
        val wordInfo = helloInfo

        val wordItem = searchStateMapper.toWordItem(wordInfo)

        eq(wordItem, helloItem)
    }
}
