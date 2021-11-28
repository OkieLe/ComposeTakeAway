package com.example.takeaway.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.takeaway.R
import com.example.takeaway.model.MainUiState

@Composable
fun HomeScreen(uiState: MainUiState) {
    Text(text = stringResource(id = R.string.home_label))
}
