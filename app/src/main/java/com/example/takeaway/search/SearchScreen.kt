package com.example.takeaway.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeaway.R
import com.example.takeaway.common.ui.MainUiState
import com.example.takeaway.common.ui.Screen
import com.example.takeaway.common.ui.WordBoard
import com.example.takeaway.common.ui.rememberMainUiState
import com.example.takeaway.design.IconMenu
import com.example.takeaway.design.LoadingIndicator
import com.example.takeaway.design.SearchTextField
import com.example.takeaway.search.model.SearchAction
import com.example.takeaway.search.model.SearchEvent
import com.example.takeaway.search.model.SearchState
import com.example.takeaway.search.model.SearchStatus
import com.example.takeaway.search.model.StarState
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
            is SearchStatus.Result -> WordBoard(status.wordItems)
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
