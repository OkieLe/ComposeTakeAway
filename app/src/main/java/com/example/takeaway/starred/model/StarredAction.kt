package com.example.takeaway.starred.model

import com.example.takeaway.common.UiAction

sealed interface StarredAction: UiAction {
    object LoadStarredWords: StarredAction
}
