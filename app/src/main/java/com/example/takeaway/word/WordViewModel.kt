package com.example.takeaway.word

import androidx.lifecycle.viewModelScope
import com.example.takeaway.common.BaseViewModel
import com.example.takeaway.common.mapper.WordItemMapper
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.domain.base.onError
import com.example.takeaway.domain.base.onSuccess
import com.example.takeaway.domain.words.GetWord
import com.example.takeaway.domain.words.UnStarWord
import com.example.takeaway.word.model.WordAction
import com.example.takeaway.word.model.WordError
import com.example.takeaway.word.model.WordEvent
import com.example.takeaway.word.model.WordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    private val wordItemMapper: WordItemMapper,
    private val getWord: GetWord,
    private val unStarWord: UnStarWord,
): BaseViewModel<WordAction, WordState, WordEvent>() {

    private val currentWordInfo: MutableList<WordInfo> = mutableListOf()

    override val initialState: WordState
        get() = WordState()

    override fun submit(action: WordAction) {
        when (action) {
            is WordAction.LoadInfo -> loadWord(action.word)
            WordAction.UnStar -> unStarWord()
            is WordAction.Play -> {}
        }
    }

    private fun loadWord(word: String) {
        if (word.isBlank()) {
            sendEvent(WordEvent.ShowError(WordError.BLANK_WORD))
            return
        }
        val trimmedWord = word.trim()
        viewModelScope.launch {
            getWord(trimmedWord)
                .onSuccess {
                    if (it.isEmpty()) {
                        sendEvent(WordEvent.ShowError(WordError.NO_INFO))
                    } else {
                        currentWordInfo.clear()
                        currentWordInfo.addAll(it)
                        val wordItems = it.map(wordItemMapper::fromInfo)
                        updateState(WordState(wordItems, starred = true))
                    }
                }
                .onError { _, _ ->
                    sendEvent(WordEvent.ShowError(WordError.NO_INFO))
                }
        }
    }

    private fun unStarWord() {
        viewModelScope.launch {
            unStarWord(currentWordInfo[0].word)
            updateState(state.value.copy(starred = false))
        }
    }
}
