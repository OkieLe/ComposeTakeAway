package com.example.takeaway.word

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeaway.R
import com.example.takeaway.common.ui.MainUiState
import com.example.takeaway.common.ui.WordBoard
import com.example.takeaway.design.IconMenu
import com.example.takeaway.word.model.WordAction
import com.example.takeaway.word.model.WordEvent
import com.example.takeaway.word.model.WordState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WordScreen(uiState: MainUiState, word: String) {
    val viewModel: WordViewModel = hiltViewModel()
    val actor = viewModel::submit
    val state by viewModel.state.collectAsState()
    UiEffects(viewModel, uiState)
    WordScreenContent(word, state, actor, uiState)
}

@Composable
private fun WordScreenContent(
    word: String,
    state: WordState,
    actor: (action: WordAction) -> Unit,
    uiState: MainUiState
) {
    LaunchedEffect(Unit) {
        actor(WordAction.LoadInfo(word))
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(word, state.starred, actor, uiState)
        WordBoard(state.wordItems, showMore = true)
    }
}

@Composable
private fun UiEffects(viewModel: WordViewModel, uiState: MainUiState) {
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is WordEvent.ShowError -> uiState.showSnackbar(event.error.message)
            }
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    isStarred: Boolean,
    actor: (WordAction) -> Unit,
    uiState: MainUiState
) {
    val unStarClicker = {
        actor(WordAction.UnStar)
        uiState.navigateUp()
    }
    TopAppBar(
        modifier = Modifier.padding(vertical = 4.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 2.dp
    ) {
        IconMenu(
            imageVector = Icons.Filled.ArrowBack,
            description = stringResource(id = R.string.search_label),
            onClick = { uiState.navigateUp() }
        )
        Text(
            text = title,
            style = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.primary
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        IconMenu(
            imageVector = if (isStarred) {
                Icons.Outlined.Favorite
            } else Icons.Outlined.FavoriteBorder,
            description = stringResource(id = R.string.star_label),
            enabled = isStarred,
            onClick = unStarClicker
        )
    }
}
