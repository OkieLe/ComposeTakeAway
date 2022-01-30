package com.example.takeaway.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takeaway.data.DictionaryRepository
import com.example.takeaway.data.model.ErrorType
import com.example.takeaway.data.model.onError
import com.example.takeaway.data.model.onSuccess
import com.example.takeaway.search.model.SearchAction
import com.example.takeaway.search.model.SearchEvent
import com.example.takeaway.search.model.SearchState
import com.example.takeaway.search.model.SearchStatus
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
    private val dictionaryRepository: DictionaryRepository
): ViewModel() {
    private val _events = Channel<SearchEvent>()
    val events = _events.receiveAsFlow().shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    fun submit(action: SearchAction) {
        when (action) {
            is SearchAction.Search -> searchWord(action.word)
        }
    }

    private fun searchWord(word: String) {
        if (word.isBlank()) {
            updateState(SearchState(SearchStatus.Result()))
            return
        }
        updateState(SearchState(status = SearchStatus.Loading))
        viewModelScope.launch {
            dictionaryRepository.searchWord(word = word.trim())
                .onSuccess {
                    val wordItems = it.map(searchStateMapper::toWordItem)
                    updateState(SearchState(status = SearchStatus.Result(wordItems)))
                }
                .onError { type, _ ->
                    Timber.e("Search <$word> error $type")
                    updateState(SearchState(status = SearchStatus.Result()))
                    sendError(type)
                }
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
