package com.example.takeaway.common.mapper

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import com.example.takeaway.common.model.DefinitionItem
import com.example.takeaway.common.model.MeaningItem
import com.example.takeaway.common.model.PhoneticItem
import com.example.takeaway.common.model.WordItem
import com.example.takeaway.data.model.Definition
import com.example.takeaway.data.model.Meaning
import com.example.takeaway.data.model.Phonetic
import com.example.takeaway.data.model.WordInfo
import javax.inject.Inject

class WordItemMapper @Inject constructor() {
    fun fromInfo(wordInfo: WordInfo): WordItem {
        return with(wordInfo) {
            WordItem(
                text = word,
                phonetics = phonetics.filterNot { it.text.isNullOrBlank() }.map(this@WordItemMapper::toPhoneticItem),
                meanings = meanings.map(this@WordItemMapper::toMeaningItem)
            )
        }
    }

    private fun toMeaningItem(meaning: Meaning): MeaningItem {
        return with(meaning) {
            MeaningItem(
                partOfSpeech = partOfSpeech,
                definitions = definitions.map(this@WordItemMapper::toDefinitionItem)
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
