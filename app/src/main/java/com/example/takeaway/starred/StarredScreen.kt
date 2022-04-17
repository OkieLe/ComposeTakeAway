package com.example.takeaway.starred

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeaway.R
import com.example.takeaway.design.FlexTextRow
import com.example.takeaway.design.IconMenu
import com.example.takeaway.design.LoadingIndicator
import com.example.takeaway.model.MainUiState
import com.example.takeaway.starred.model.StarredAction
import com.example.takeaway.starred.model.StarredState
import com.example.takeaway.starred.model.StarredStatus
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StarredScreen(uiState: MainUiState) {
    val viewModel: StarredViewModel = hiltViewModel()
    val actor = viewModel::submit
    val state by viewModel.state.collectAsState()
    UiEffects(viewModel, uiState)
    StarredScreenContent(state, actor, uiState)
}

@Composable
private fun StarredScreenContent(
    state: StarredState,
    actor: (action: StarredAction) -> Unit,
    uiState: MainUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(uiState)
        when (state.status) {
            StarredStatus.Loading -> LoadingIndicator()
            is StarredStatus.Result -> FlexTextRow(
                modifier = Modifier.fillMaxWidth(),
                textList = state.status.wordItems.map { it.text },
                style = MaterialTheme.typography.body1
            )
        }
    }
    LaunchedEffect(Unit) {
        actor(StarredAction.LoadStarredWords)
    }
}
@Composable
private fun TopBar(uiState: MainUiState) {
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
            text = stringResource(id = R.string.star_label),
            style = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.primary
            )
        )
    }
}
@Composable
private fun UiEffects(viewModel: StarredViewModel, uiState: MainUiState) {
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
            }
        }
    }
}
