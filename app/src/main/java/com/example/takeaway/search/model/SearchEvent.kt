package com.example.takeaway.search.model

import com.example.takeaway.common.UiEvent
import com.example.takeaway.data.model.ErrorType

sealed interface SearchEvent: UiEvent {
    data class ShowError(val error: ErrorType): SearchEvent
}
