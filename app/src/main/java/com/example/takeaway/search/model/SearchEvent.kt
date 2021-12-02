package com.example.takeaway.search.model

import com.example.takeaway.data.model.ErrorType

sealed interface SearchEvent {
    data class ShowError(val error: ErrorType): SearchEvent
}
