package com.example.takeaway.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeaway.R
import com.example.takeaway.data.model.Definition
import com.example.takeaway.data.model.Meaning
import com.example.takeaway.data.model.Phonetic
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.design.SearchTextField
import com.example.takeaway.model.MainUiState
import com.example.takeaway.search.model.SearchAction
import com.example.takeaway.search.model.SearchStatus

@Composable
fun SearchScreen(uiState: MainUiState) {
    val viewModel: SearchViewModel = hiltViewModel()
    val actor = viewModel::submit
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { focusManager.clearFocus() })
    ) {
        TopSearchBar(actor)
        when (val status = state.status) {
            SearchStatus.Loading -> LoadingIndicator()
            is SearchStatus.Result -> WordInfoList(status.wordInfoList)
        }
    }
}

@Composable
private fun TopSearchBar(actor: (SearchAction) -> Unit) {
    val keyword = remember { mutableStateOf(TextFieldValue()) }
    TopAppBar(
        modifier = Modifier.padding(vertical = 4.dp), backgroundColor = MaterialTheme.colors.background
    ) {
        SearchTextField(modifier = Modifier.fillMaxWidth(),
            value = keyword.value,
            hint = stringResource(id = R.string.search_box_hint),
            onValueChange = { keyword.value = it },
            onSearchSubmit = { actor(SearchAction.Search(keyword.value.text)) })
    }
}

@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun WordInfoList(wordInfoList: List<WordInfo>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(wordInfoList) { wordInfo ->
            WordInfoItem(wordInfo)
        }
    }
}

@Composable
private fun WordInfoItem(wordInfo: WordInfo) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        WordText(wordInfo.word)
        PhoneticField(wordInfo.phonetics)
        Spacer(modifier = Modifier.height(16.dp))
        MeaningsField(wordInfo.meanings)
    }
}

@Composable
private fun MeaningsField(meanings: List<Meaning>) {
    meanings.forEach {
        Spacer(modifier = Modifier.height(12.dp))
        PartOfSpeech(it.partOfSpeech.capitalize(LocaleList()))
        it.definitions.forEachIndexed { index, definition ->
            Text(
                text = "${index+1}. ${definition.definition}",
                style = MaterialTheme.typography.body2
            )
            DefinitionItem(definition)
        }
    }
}

@Composable
private fun DefinitionItem(definition: Definition) {
    definition.example?.let {
        Text(text = "> ${it.capitalize(LocaleList())}",
            style = MaterialTheme.typography.body2,
            fontStyle = FontStyle.Italic
        )
    }
    definition.synonyms.joinToString().takeIf { it.isNotBlank() }?.let {
        SynonymsOrAntonyms(label = stringResource(id = R.string.label_synonyms), words = it)
    }
    definition.antonyms.joinToString().takeIf { it.isNotBlank() }?.let {
        SynonymsOrAntonyms(label = stringResource(id = R.string.label_antonyms), words = it)
    }
}

@Composable
private fun SynonymsOrAntonyms(label: String, words: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = label,
        style = MaterialTheme.typography.caption,
        fontStyle = FontStyle.Italic
    )
    Text(text = words, style = MaterialTheme.typography.caption)
}

@Composable
private fun PartOfSpeech(partOfSpeech: String) {
    Text(
        text = partOfSpeech,
        style = MaterialTheme.typography.body2,
        fontStyle = FontStyle.Italic
    )
}

@Composable
private fun PhoneticField(phonetics: List<Phonetic>) {
    phonetics.forEach {
        PhoneticItem(phonetic = it)
    }
}

@Composable
private fun PhoneticItem(phonetic: Phonetic) {
    Text(
        text = "[${phonetic.text}]",
        style = MaterialTheme.typography.body1
    )
}

@Composable
private fun WordText(word: String) {
    Text(
        text = word,
        style = MaterialTheme.typography.h6
    )
}