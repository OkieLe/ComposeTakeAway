package com.example.takeaway.search

import com.example.takeaway.common.mapper.WordItemMapper
import com.example.takeaway.common.model.WordItem
import com.example.takeaway.data.model.Definition
import com.example.takeaway.data.model.Meaning
import com.example.takeaway.data.model.Phonetic
import com.example.takeaway.data.model.WordInfo
import io.cucumber.java8.En
import io.kotest.matchers.shouldBe

class WordMapperStepDefinitions : En {
    private val searchStateMapper = WordItemMapper()

    init {
        var wordInfo: WordInfo? = null
        var wordItem: WordItem? = null
        DataTableType { entry: Map<String, String> ->
            WordInfo(
                word = entry["word"].orEmpty(),
                meanings = listOf(Meaning(partOfSpeech = "", definitions = listOf(
                    Definition(
                        definition = entry["definition"].orEmpty(),
                        example = "",
                        synonyms = emptyList(),
                        antonyms = emptyList()
                    )
                ))),
                phonetics = listOf(Phonetic(text = entry["phonetic"].orEmpty(), audio = ""))
            )
        }
        Before { _ ->
            wordInfo = null
            wordItem = null
        }
        Given("A WordInfo with below data:") { wordInfoData: WordInfo ->
            wordInfo = wordInfoData
        }
        When("Convert a WordInfo to WordItem") {
            wordItem = wordInfo?.let { searchStateMapper.fromInfo(it) }
        }
        Then("The WordItem content is correct") {
            wordItem?.text shouldBe wordInfo?.word
            wordItem?.meanings?.size shouldBe 1
            wordItem?.meanings?.firstOrNull()?.definitions?.firstOrNull()?.text shouldBe
                wordInfo?.meanings?.firstOrNull()?.definitions?.firstOrNull()?.definition
            wordItem?.phonetics?.firstOrNull()?.text shouldBe
                wordInfo?.phonetics?.firstOrNull()?.text
        }
    }
}
