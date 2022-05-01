package com.example.takeaway.chinese

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.example.takeaway.chinese.model.MoreResultState
import com.example.takeaway.chinese.model.SearchMode
import com.example.takeaway.chinese.model.SearchStatus
import com.example.takeaway.common.model.HanziBrief
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
        state = ChineseState(searchStatus = SearchStatus.Loading),
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
        TopBar(state.searchMode, state.moreResultState, actor)
        when (val status = state.searchStatus) {
            SearchStatus.Loading -> LoadingIndicator()
            is SearchStatus.Result -> HanziBoard(hanziItems = status.items)
        }
    }
}

@Composable
private fun UiEffects(viewModel: ChineseViewModel, uiState: MainUiState) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ChineseEvent.ShowError -> uiState.showSnackbar(event.error.message)
                is ChineseEvent.ShowPartial -> uiState.showSnackbar(
                    context.getString(R.string.message_hanzi_shown_partially, event.total)
                )
            }
        }
    }
}

@Composable
private fun AllItems(
    dropdownExpanded: MutableState<Boolean>,
    allItems: List<HanziBrief>,
    onItemSelected: (HanziBrief) -> Unit
) {
    val density = LocalDensity.current
    val itemSize = remember { mutableStateOf(0.dp) }
    DropdownMenu(
        expanded = dropdownExpanded.value,
        onDismissRequest = { dropdownExpanded.value = false },
        modifier = Modifier.requiredSizeIn(maxHeight = minOf(itemSize.value.times(allItems.size), 400.dp))
    ) {
        allItems.forEach { item ->
            DropdownMenuItem(
                onClick = { onItemSelected(item) },
                modifier = Modifier.onSizeChanged {
                    with(density) {
                        itemSize.value = it.height.toDp()
                    }
                }
            ) {
                Text(text = item.name)
            }
        }
    }
}

@Composable
private fun TopBar(
    searchMode: SearchMode, moreState: MoreResultState, actor: (ChineseAction) -> Unit
) {
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
        if (moreState.showPartial) {
            Box {
                val dropdownExpanded = remember { mutableStateOf(false) }
                IconMenu(
                    imagePainter = painterResource(id = R.drawable.ic_show_more),
                    description = stringResource(id = R.string.more_label),
                    onClick = { dropdownExpanded.value = true }
                )
                AllItems(
                    dropdownExpanded = dropdownExpanded,
                    allItems = moreState.allItems,
                    onItemSelected = {
                        keyword.value = TextFieldValue(it.name)
                        actor(ChineseAction.Search(it.name))
                    }
                )
            }
        }
        IconMenu(
            imagePainter = painterResource(
                id = if (searchMode == SearchMode.ZiMode) R.drawable.ic_state_zi else R.drawable.ic_state_ci
            ),
            description = stringResource(id = hint),
            onClick = { actor(ChineseAction.ChangeMode) }
        )
    }
}
