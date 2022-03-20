package com.example.takeaway.design

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.example.takeaway.R

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    value: TextFieldValue,
    hint: String,
    onValueChange: (TextFieldValue) -> Unit,
    onSearchSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val submitSearch = {
        focusManager.clearFocus()
        onSearchSubmit(value.text)
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = android.R.string.search_go)
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.text.isNotEmpty(), enter = fadeIn(), exit = fadeOut()
            ) {
                IconButton(
                    onClick = { onValueChange(TextFieldValue()) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear, contentDescription = stringResource(R.string.clear_label)
                    )
                }
            }
        },
        placeholder = { Text(text = hint) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            submitSearch()
        }),
        maxLines = 1,
        singleLine = true,
        modifier = modifier.onKeyEvent { event ->
            if (event.key == Key.Enter && event.type == KeyEventType.KeyUp) {
                submitSearch()
            }
            true
        }
    )
}
