package com.example.takeaway.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takeaway.data.WordsRepository
import com.example.takeaway.data.model.ErrorType
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.data.model.onError
import com.example.takeaway.data.model.onSuccess
import com.example.takeaway.search.mapper.SearchStateMapper
import com.example.takeaway.search.model.SearchAction
import com.example.takeaway.search.model.SearchEvent
import com.example.takeaway.search.model.SearchState
import com.example.takeaway.search.model.SearchStatus
import com.example.takeaway.search.model.StarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchStateMapper: SearchStateMapper,
    private val wordsRepository: WordsRepository
): ViewModel() {
    private val _events = Channel<SearchEvent>()
    val events = _events.receiveAsFlow().shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val currentWordInfo: MutableList<WordInfo> = mutableListOf()

    fun submit(action: SearchAction) {
        when (action) {
            is SearchAction.Search -> searchWord(action.word)
            SearchAction.Star -> starWord()
            SearchAction.UnStar -> unStarWord()
        }
    }

    private fun searchWord(word: String) {
        if (word.isBlank()) {
            updateState(SearchState(SearchStatus.Result()))
            return
        }
        val trimmedWord = word.trim()
        updateState(SearchState(status = SearchStatus.Loading))
        viewModelScope.launch {
            wordsRepository.searchWord(word = trimmedWord)
                .onSuccess {
                    currentWordInfo.clear()
                    currentWordInfo.addAll(it)
                    val wordItems = it.map(searchStateMapper::toWordItem)
                    updateState(SearchState(status = SearchStatus.Result(wordItems)))
                    viewModelScope.launch {
                        updateState(
                            _state.value.copy(
                                starState = StarState(enabled = true,
                                    wordsRepository.isWordStarred(trimmedWord)))
                        )
                    }
                }
                .onError { type, _ ->
                    Timber.e("Search <$trimmedWord> error $type")
                    updateState(SearchState(status = SearchStatus.Result(), StarState()))
                    sendError(type)
                }
        }
    }

    private fun starWord() {
        viewModelScope.launch {
            wordsRepository.starWord(currentWordInfo)
            updateState(_state.value.copy(starState = StarState(enabled = true, starred = true)))
        }
    }

    private fun unStarWord() {
        viewModelScope.launch {
            wordsRepository.unStarWord(currentWordInfo[0].word)
            updateState(_state.value.copy(starState = StarState(enabled = true, starred = false)))
        }
    }

    private fun sendError(type: ErrorType) {
        viewModelScope.launch {
            _events.send(SearchEvent.ShowError(type))
        }
    }

    private fun updateState(state: SearchState) {
        viewModelScope.launch {
            _state.emit(state)
        }
    }
}
