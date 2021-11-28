package com.example.takeaway.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeaway.R
import com.example.takeaway.design.SearchTextField
import com.example.takeaway.model.MainUiState
import com.example.takeaway.search.model.SearchAction

@Composable
fun SearchScreen(uiState: MainUiState) {
    val viewModel: SearchViewModel = hiltViewModel()
    val keyword = remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current
    Column(modifier = Modifier
        .fillMaxSize()
        .clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { focusManager.clearFocus() })) {
        TopAppBar(
            modifier = Modifier.padding(vertical = 4.dp),
            backgroundColor = MaterialTheme.colors.background
        ) {
            SearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = keyword.value,
                hint = stringResource(id = R.string.search_box_hint),
                onValueChange = { keyword.value = it },
                onSearchSubmit = { viewModel.submit(SearchAction.Search(keyword.value.text)) }
            )
        }
    }
}
