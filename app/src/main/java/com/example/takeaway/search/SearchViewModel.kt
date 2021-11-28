package com.example.takeaway.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takeaway.data.DictionaryRepository
import com.example.takeaway.data.model.onError
import com.example.takeaway.data.model.onSuccess
import com.example.takeaway.search.model.SearchAction
import com.example.takeaway.search.model.SearchState
import com.example.takeaway.search.model.SearchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
): ViewModel() {
    private val _state = Channel<SearchState>()
    val state = _state.receiveAsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SearchState())

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
                .onSuccess { updateState(SearchState(status = SearchStatus.Result(it))) }
                .onError { type, _ -> Timber.e("Search <$word> error $type") }
        }
    }

    private fun updateState(state: SearchState) {
        viewModelScope.launch {
            _state.send(state)
        }
    }
}
