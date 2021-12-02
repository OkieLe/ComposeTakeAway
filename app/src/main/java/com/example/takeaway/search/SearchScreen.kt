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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeaway.R
import com.example.takeaway.design.SearchTextField
import com.example.takeaway.model.MainUiState
import com.example.takeaway.search.model.DefinitionItem
import com.example.takeaway.search.model.MeaningItem
import com.example.takeaway.search.model.PhoneticItem
import com.example.takeaway.search.model.SearchAction
import com.example.takeaway.search.model.SearchEvent
import com.example.takeaway.search.model.SearchState
import com.example.takeaway.search.model.SearchStatus
import com.example.takeaway.search.model.WordItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(uiState: MainUiState) {
    val viewModel: SearchViewModel = hiltViewModel()
    val actor = viewModel::submit
    val state by viewModel.state.collectAsState()
    UiEffects(viewModel, uiState)
    SearchScreenContent(state, actor)
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreenContent(
        state = SearchState(status = SearchStatus.Loading),
        actor = {}
    )
}

@Composable
private fun SearchScreenContent(
    state: SearchState, actor: (action: SearchAction) -> Unit
) {
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
            is SearchStatus.Result -> WordInfoList(status.wordItems)
        }
    }
}

@Composable
private fun UiEffects(viewModel: SearchViewModel, uiState: MainUiState) {
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when(event) {
                is SearchEvent.ShowError -> uiState.showSnackbar(R.string.error_message_unknown_error)
            }
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
private fun WordInfoList(wordItems: List<WordItem>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(wordItems) { wordItem ->
            WordInfoItem(wordItem)
        }
    }
}

@Composable
private fun WordInfoItem(wordInfo: WordItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        WordText(wordInfo.text)
        PhoneticField(wordInfo.phonetics)
        MeaningsField(wordInfo.meanings)
    }
}

@Composable
private fun MeaningsField(meanings: List<MeaningItem>) {
    meanings.forEach {
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.padding(horizontal = 4.dp), elevation = 4.dp) {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                PartOfSpeech(it.partOfSpeech.capitalize(LocaleList()))
                it.definitions.forEachIndexed { index, definition ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${index + 1}. ${definition.text}", style = MaterialTheme.typography.body2
                    )
                    DefinitionItem(definition)
                }
            }
        }
    }
}

@Composable
private fun DefinitionItem(definition: DefinitionItem) {
    definition.example.takeIf { it.isNotBlank() }?.let {
        Text(text = "i.e. $it",
            style = MaterialTheme.typography.body2,
            fontStyle = FontStyle.Italic
        )
    }
    definition.synonyms.takeIf { it.isNotBlank() }?.let {
        SynonymsOrAntonyms(label = stringResource(id = R.string.label_synonyms), words = it)
    }
    definition.antonyms.takeIf { it.isNotBlank() }?.let {
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
private fun PhoneticField(phonetics: List<PhoneticItem>) {
    phonetics.forEach {
        PhoneticText(phonetic = it)
    }
}

@Composable
private fun PhoneticText(phonetic: PhoneticItem) {
    Text(
        modifier = Modifier.padding(horizontal = 8.dp),
        text = phonetic.text,
        style = MaterialTheme.typography.body1
    )
}

@Composable
private fun WordText(word: String) {
    Text(
        modifier = Modifier.padding(horizontal = 8.dp),
        text = word,
        style = MaterialTheme.typography.h6
    )
}
