package com.example.takeaway.word

import androidx.lifecycle.viewModelScope
import com.example.takeaway.common.BaseViewModel
import com.example.takeaway.common.mapper.WordItemMapper
import com.example.takeaway.data.WordsRepository
import com.example.takeaway.data.model.WordInfo
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
    private val wordsRepository: WordsRepository
): BaseViewModel<WordAction, WordState, WordEvent>() {

    private val currentWordInfo: MutableList<WordInfo> = mutableListOf()

    override val initialState: WordState
        get() = WordState()

    override fun submit(action: WordAction) {
        when (action) {
            is WordAction.LoadInfo -> loadWord(action.word)
            WordAction.UnStar -> unStarWord()
        }
    }

    private fun loadWord(word: String) {
        if (word.isBlank()) {
            sendEvent(WordEvent.ShowError(WordError.BLANK_WORD))
            return
        }
        val trimmedWord = word.trim()
        viewModelScope.launch {
            wordsRepository.getWord(word = trimmedWord).let {
                if (it.isEmpty()) {
                    sendEvent(WordEvent.ShowError(WordError.NO_INFO))
                } else {
                    currentWordInfo.clear()
                    currentWordInfo.addAll(it)
                    val wordItems = it.map(wordItemMapper::fromInfo)
                    updateState(WordState(wordItems, starred = true))
                }
            }
        }
    }

    private fun unStarWord() {
        viewModelScope.launch {
            wordsRepository.unStarWord(currentWordInfo[0].word)
            updateState(state.value.copy(starred = false))
        }
    }
}
