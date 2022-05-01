package com.example.takeaway.chinese.model

import androidx.annotation.StringRes
import com.example.takeaway.R
import com.example.takeaway.common.UiEvent

sealed interface ChineseEvent: UiEvent {
    data class ShowError(val error: SearchError): ChineseEvent
}

enum class SearchError(@StringRes val message: Int) {
    InvalidInput(R.string.error_message_invalid_input),
    NotFound(R.string.error_message_hanzi_not_found)
}