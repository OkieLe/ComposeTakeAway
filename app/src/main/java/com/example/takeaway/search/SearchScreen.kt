package com.example.takeaway.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.takeaway.design.IconMenu
import com.example.takeaway.design.SearchTextField
import com.example.takeaway.model.MainUiState
import com.example.takeaway.model.Screen
import com.example.takeaway.model.rememberMainUiState
import com.example.takeaway.search.model.DefinitionItem
import com.example.takeaway.search.model.MeaningItem
import com.example.takeaway.search.model.PhoneticItem
import com.example.takeaway.search.model.SearchAction
import com.example.takeaway.search.model.SearchEvent
import com.example.takeaway.search.model.SearchState
import com.example.takeaway.search.model.SearchStatus
import com.example.takeaway.search.model.StarState
import com.example.takeaway.search.model.WordItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(uiState: MainUiState) {
    val viewModel: SearchViewModel = hiltViewModel()
    val actor = viewModel::submit
    val state by viewModel.state.collectAsState()
    UiEffects(viewModel, uiState)
    SearchScreenContent(state, actor, uiState)
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreenContent(
        state = SearchState(status = SearchStatus.Loading),
        actor = {},
        uiState = rememberMainUiState()
    )
}

@Composable
private fun SearchScreenContent(
    state: SearchState,
    actor: (action: SearchAction) -> Unit,
    uiState: MainUiState
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { focusManager.clearFocus() })
    ) {
        TopBar(state.starState, actor, uiState)
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
            when (event) {
                is SearchEvent.ShowError -> uiState.showSnackbar(R.string.error_message_unknown_error)
            }
        }
    }
}

@Composable
private fun TopBar(starState: StarState, actor: (SearchAction) -> Unit, uiState: MainUiState) {
    val keyword = remember { mutableStateOf(TextFieldValue()) }
    val starClicker = { actor(SearchAction.Star) }
    val unStarClicker = { actor(SearchAction.UnStar) }
    TopAppBar(
        modifier = Modifier.padding(vertical = 4.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        SearchTextField(
            modifier = Modifier
                .padding(vertical = 1.dp)
                .padding(start = 8.dp)
                .weight(1f),
            value = keyword.value,
            hint = stringResource(id = R.string.search_box_hint),
            onValueChange = { keyword.value = it },
            onSearchSubmit = { actor(SearchAction.Search(keyword.value.text)) })
        IconMenu(
            imageVector = if (starState.enabled && starState.starred) {
                Icons.Outlined.Favorite
            } else Icons.Outlined.FavoriteBorder,
            description = stringResource(id = R.string.star_label),
            enabled = starState.enabled,
            onClick = if (starState.starred) unStarClicker else starClicker
        )
        IconMenu(imageVector = Icons.Outlined.List, description = "") {
            uiState.openScreen(Screen.Starred.route)
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(40.dp))
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ColumnScope.WordInfoList(wordItems: List<WordItem>) {
    val pagerState = rememberPagerState()
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
        WordInfoItem(wordItems[page])
    }
}

@Composable
private fun WordInfoItem(wordInfo: WordItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        BasicFields(wordInfo)
        MeaningsField(wordInfo.meanings)
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
private fun MeaningsField(meanings: List<MeaningItem>) {
    meanings.forEach {
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.padding(horizontal = 4.dp), elevation = 4.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                PartOfSpeech(it.partOfSpeech.capitalize(LocaleList()))
                it.definitions.forEachIndexed { index, definition ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${index + 1}. ${definition.text}",
                        style = MaterialTheme.typography.body2
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
        Text(
            text = "i.e. $it",
            style = MaterialTheme.typography.body2,
            color = Color.Gray,
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
        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.6f),
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
        color = MaterialTheme.colors.secondaryVariant,
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
