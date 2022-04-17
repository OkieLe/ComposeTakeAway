package com.example.takeaway.search.mapper

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import com.example.takeaway.data.model.Definition
import com.example.takeaway.data.model.Meaning
import com.example.takeaway.data.model.Phonetic
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.search.model.DefinitionItem
import com.example.takeaway.search.model.MeaningItem
import com.example.takeaway.search.model.PhoneticItem
import com.example.takeaway.search.model.WordItem
import javax.inject.Inject

class SearchStateMapper @Inject constructor() {
    fun toWordItem(wordInfo: WordInfo): WordItem {
        return with(wordInfo) {
            WordItem(
                text = word,
                phonetics = phonetics.filterNot { it.text.isNullOrBlank() }.map(this@SearchStateMapper::toPhoneticItem),
                meanings = meanings.map(this@SearchStateMapper::toMeaningItem)
            )
        }
    }

    private fun toMeaningItem(meaning: Meaning): MeaningItem {
        return with(meaning) {
            MeaningItem(
                partOfSpeech = partOfSpeech,
                definitions = definitions.map(this@SearchStateMapper::toDefinitionItem)
            )
        }
    }

    private fun toPhoneticItem(phonetic: Phonetic): PhoneticItem {
        return PhoneticItem(
            audio = phonetic.audio.orEmpty(),
            text = phonetic.text.orEmpty()
        )
    }

    private fun toDefinitionItem(definition: Definition): DefinitionItem {
        return with(definition) {
            DefinitionItem(
                text = this.definition,
                example = example.orEmpty().capitalize(LocaleList()),
                synonyms = synonyms.joinToString(),
                antonyms = antonyms.joinToString()
            )
        }
    }
}
