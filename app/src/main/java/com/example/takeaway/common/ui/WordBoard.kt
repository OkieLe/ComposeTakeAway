package com.example.takeaway.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import com.example.takeaway.R
import com.example.takeaway.common.model.DefinitionItem
import com.example.takeaway.common.model.MeaningItem
import com.example.takeaway.common.model.PhoneticItem
import com.example.takeaway.common.model.WordItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.min

private const val MAX_DEFINITION_COUNT = 5

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WordBoard(wordItems: List<WordItem>, showMore: Boolean = false) {
    val pagerState = rememberPagerState()
    Column {
        if (wordItems.size > 1) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                activeColor = MaterialTheme.colors.primary,
                indicatorWidth = 6.dp
            )
        }
        HorizontalPager(count = wordItems.size, state = pagerState) { page ->
            WordInfoItem(wordItems[page], showMore)
        }
    }
}

@Composable
private fun WordInfoItem(wordInfo: WordItem, showMore: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        BasicFields(wordInfo)
        MeaningsField(wordInfo.meanings, showMore)
    }
}

@Composable
private fun BasicFields(wordInfo: WordItem) {
    Card(
        modifier = Modifier.padding(horizontal = 4.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            WordText(modifier = Modifier.align(Alignment.Bottom), wordInfo.text)
            wordInfo.phonetics.forEach {
                PhoneticText(modifier = Modifier.align(Alignment.Bottom), phonetic = it)
            }
        }
    }
}

@Composable
private fun MeaningsField(meanings: List<MeaningItem>, showMore: Boolean) {
    meanings.forEach {
        Spacer(modifier = Modifier.height(12.dp))
        SelectableCard(modifier = Modifier.padding(horizontal = 4.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                PartOfSpeech(it.partOfSpeech.capitalize(LocaleList()))
                val definitionsToShow =
                    if (showMore) it.definitions
                    else it.definitions.subList(0, min(MAX_DEFINITION_COUNT, it.definitions.size))
                definitionsToShow.forEachIndexed { index, definition ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${index + 1}. ${definition.text}",
                        style = MaterialTheme.typography.body2
                    )
                    DefinitionItem(definition, showMore)
                }
            }
        }
    }
}

@Composable
private fun DefinitionItem(definition: DefinitionItem, showMore: Boolean) {
    definition.example.takeIf { it.isNotBlank() }?.let {
        Text(
            text = "i.e. $it",
            style = MaterialTheme.typography.body2,
            color = Color.Gray,
            fontStyle = FontStyle.Italic
        )
    }
    if (showMore) {
        definition.synonyms.takeIf { it.isNotBlank() }?.let {
            SynonymsOrAntonyms(label = stringResource(id = R.string.label_synonyms), words = it)
        }
        definition.antonyms.takeIf { it.isNotBlank() }?.let {
            SynonymsOrAntonyms(label = stringResource(id = R.string.label_antonyms), words = it)
        }
    }
}

@Composable
private fun SynonymsOrAntonyms(label: String, words: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = label,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.primary.copy(alpha = 0.6f),
        fontStyle = FontStyle.Italic
    )
    Text(
        text = words, style = MaterialTheme.typography.caption,
        color = Color.Black.copy(alpha = 0.75f)
    )
}

@Composable
private fun PartOfSpeech(partOfSpeech: String) {
    Text(
        text = partOfSpeech,
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.primary,
        fontStyle = FontStyle.Italic
    )
}

@Composable
private fun PhoneticText(modifier: Modifier, phonetic: PhoneticItem) {
    Text(
        modifier = modifier.padding(all = 4.dp),
        text = phonetic.text,
        style = MaterialTheme.typography.body1
    )
}

@Composable
private fun WordText(modifier: Modifier, word: String) {
    Text(
        modifier = modifier.padding(horizontal = 8.dp),
        text = word,
        style = MaterialTheme.typography.h4
    )
}
