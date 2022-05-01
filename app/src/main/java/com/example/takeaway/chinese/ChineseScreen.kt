package com.example.takeaway.chinese

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeaway.R
import com.example.takeaway.chinese.model.ChineseAction
import com.example.takeaway.chinese.model.ChineseEvent
import com.example.takeaway.chinese.model.ChineseState
import com.example.takeaway.chinese.model.SearchMode
import com.example.takeaway.chinese.model.SearchStatus
import com.example.takeaway.common.ui.HanziBoard
import com.example.takeaway.common.ui.MainUiState
import com.example.takeaway.design.IconMenu
import com.example.takeaway.design.LoadingIndicator
import com.example.takeaway.design.SearchTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChineseScreen(uiState: MainUiState) {
    val viewModel: ChineseViewModel = hiltViewModel()
    val actor = viewModel::submit
    val state by viewModel.state.collectAsState()
    UiEffects(viewModel, uiState)
    ChineseScreenContent(state, actor)
}

@Preview(showBackground = true)
@Composable
fun ChineseScreenPreview() {
    ChineseScreenContent(
        state = ChineseState(status = SearchStatus.Loading),
        actor = {}
    )
}

@Composable
private fun ChineseScreenContent(
    state: ChineseState,
    actor: (action: ChineseAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { focusManager.clearFocus() })
    ) {
        TopBar(state.searchMode, actor)
        when (val status = state.status) {
            SearchStatus.Loading -> LoadingIndicator()
            is SearchStatus.Result -> { HanziBoard(hanziItems = status.items) }
        }
    }
}

@Composable
private fun UiEffects(viewModel: ChineseViewModel, uiState: MainUiState) {
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ChineseEvent.ShowError -> uiState.showSnackbar(event.error.message)
            }
        }
    }
}

@Composable
private fun TopBar(searchMode: SearchMode, actor: (ChineseAction) -> Unit) {
    val keyword = remember { mutableStateOf(TextFieldValue()) }
    val hint = if (searchMode == SearchMode.ZiMode) R.string.search_hanzi_hint_zi
    else R.string.search_hanzi_hint_ci
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
            hint = stringResource(id = hint),
            onValueChange = { keyword.value = it },
            onSearchSubmit = { actor(ChineseAction.Search(keyword.value.text)) })
        IconMenu(
            imagePainter = painterResource(
                id = if (searchMode == SearchMode.ZiMode) R.drawable.ic_state_zi else R.drawable.ic_state_ci),
            description = stringResource(id = hint),
            onClick = { actor(ChineseAction.ChangeMode) }
        )
    }
}
