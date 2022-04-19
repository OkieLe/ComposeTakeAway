package com.example.takeaway.word.model

import com.example.takeaway.common.UiAction

sealed interface WordAction: UiAction {
    data class LoadInfo(val word: String): WordAction
    object UnStar: WordAction
}
