package com.example.takeaway.search

import androidx.lifecycle.viewModelScope
import com.example.takeaway.common.BaseViewModel
import com.example.takeaway.data.WordsRepository
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
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchStateMapper: SearchStateMapper,
    private val wordsRepository: WordsRepository
): BaseViewModel<SearchAction, SearchState, SearchEvent>() {

    private val currentWordInfo: MutableList<WordInfo> = mutableListOf()

    override val initialState: SearchState
        get() = SearchState()

    override fun submit(action: SearchAction) {
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
                            state.value.copy(
                                starState = StarState(
                                    enabled = true,
                                    wordsRepository.isWordStarred(trimmedWord)
                                )
                            )
                        )
                    }
                }
                .onError { type, _ ->
                    Timber.e("Search <$trimmedWord> error $type")
                    updateState(SearchState(status = SearchStatus.Result(), StarState()))
                    sendEvent(SearchEvent.ShowError(type))
                }
        }
    }

    private fun starWord() {
        viewModelScope.launch {
            wordsRepository.starWord(currentWordInfo)
            updateState(state.value.copy(starState = StarState(enabled = true, starred = true)))
        }
    }

    private fun unStarWord() {
        viewModelScope.launch {
            wordsRepository.unStarWord(currentWordInfo[0].word)
            updateState(state.value.copy(starState = StarState(enabled = true, starred = false)))
        }
    }
}
