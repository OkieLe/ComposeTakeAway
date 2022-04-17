package com.example.takeaway.starred.model

import com.example.takeaway.common.UiEvent
import com.example.takeaway.data.model.ErrorType

sealed interface StarredEvent: UiEvent {
    data class ShowError(val error: ErrorType): StarredEvent
}
