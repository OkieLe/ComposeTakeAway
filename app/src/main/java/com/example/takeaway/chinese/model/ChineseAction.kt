package com.example.takeaway.chinese.model

import com.example.takeaway.common.UiAction

sealed interface ChineseAction: UiAction {
    data class Search(val word: String): ChineseAction
    object ChangeMode: ChineseAction
}
