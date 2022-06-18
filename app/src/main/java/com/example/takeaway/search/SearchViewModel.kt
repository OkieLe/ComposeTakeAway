package com.example.takeaway.search

import androidx.lifecycle.viewModelScope
import com.example.takeaway.common.BaseViewModel
import com.example.takeaway.common.mapper.WordItemMapper
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.domain.base.onError
import com.example.takeaway.domain.base.onSuccess
import com.example.takeaway.domain.base.successOr
import com.example.takeaway.domain.words.IsWordStarred
import com.example.takeaway.domain.words.SearchWord
import com.example.takeaway.domain.words.StarWord
import com.example.takeaway.domain.words.UnStarWord
import com.example.takeaway.search.model.SearchAction
import com.example.takeaway.search.model.SearchEvent
import com.example.takeaway.search.model.SearchState
import com.example.takeaway.search.model.SearchStatus
import com.example.takeaway.search.model.StarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val wordItemMapper: WordItemMapper,
    private val starWord: StarWord,
    private val unStarWord: UnStarWord,
    private val searchWord: SearchWord,
    private val isWordStarred: IsWordStarred
): BaseViewModel<SearchAction, SearchState, SearchEvent>() {

    private val currentWordInfo: MutableList<WordInfo> = mutableListOf()

    override val initialState: SearchState
        get() = SearchState()

    override fun submit(action: SearchAction) {
        when (action) {
            is SearchAction.Search -> onSearchWord(action.word)
            SearchAction.Star -> starWord()
            SearchAction.UnStar -> unStarWord()
        }
    }

    private fun onSearchWord(word: String) {
        if (word.isBlank()) {
            updateState(SearchState(SearchStatus.Result()))
            return
        }
        val trimmedWord = word.trim()
        updateState(SearchState(status = SearchStatus.Loading))
        viewModelScope.launch {
            searchWord(trimmedWord).collectLatest { result ->
                result.onSuccess {
                    currentWordInfo.clear()
                    currentWordInfo.addAll(it)
                    val wordItems = it.map(wordItemMapper::fromInfo)
                    updateState(SearchState(status = SearchStatus.Result(wordItems)))
                    viewModelScope.launch {
                        val starred = isWordStarred(trimmedWord).successOr(false)
                        updateState(
                            state.value.copy(
                                starState = StarState(
                                    enabled = true,
                                    starred = starred
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
    }

    private fun starWord() {
        viewModelScope.launch {
            starWord(currentWordInfo)
            updateState(state.value.copy(starState = StarState(enabled = true, starred = true)))
        }
    }

    private fun unStarWord() {
        viewModelScope.launch {
            unStarWord(currentWordInfo[0].word)
            updateState(state.value.copy(starState = StarState(enabled = true, starred = false)))
        }
    }
}
