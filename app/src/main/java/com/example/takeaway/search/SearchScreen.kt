package com.example.takeaway.search

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.takeaway.R
import com.example.takeaway.model.MainUiState

@Composable
fun SearchScreen(uiState: MainUiState) {
    Text(text = stringResource(id = R.string.search_label))
}
