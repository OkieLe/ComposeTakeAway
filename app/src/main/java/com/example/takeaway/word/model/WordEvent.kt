package com.example.takeaway.word.model

import androidx.annotation.StringRes
import com.example.takeaway.R
import com.example.takeaway.common.UiEvent

sealed interface WordEvent: UiEvent {
    data class ShowError(val error: WordError): WordEvent
}

enum class WordError(@StringRes val message: Int) {
    BLANK_WORD(R.string.error_message_blank_word),
    NO_INFO(R.string.error_message_no_info_found)
}
