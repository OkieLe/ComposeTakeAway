package com.example.takeaway.search.model

import com.example.takeaway.common.UiEvent
import com.example.takeaway.domain.base.Category

sealed interface SearchEvent: UiEvent {
    data class ShowError(val error: Category) : SearchEvent
}
