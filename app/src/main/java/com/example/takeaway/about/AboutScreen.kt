package com.example.takeaway.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.takeaway.R

@Composable
fun AboutScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(id = R.string.about_label))
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}
