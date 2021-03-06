package com.example.takeaway.starred

import androidx.lifecycle.viewModelScope
import com.example.takeaway.common.BaseViewModel
import com.example.takeaway.domain.base.onSuccess
import com.example.takeaway.domain.words.GetStarredWords
import com.example.takeaway.starred.model.StarredAction
import com.example.takeaway.starred.model.StarredEvent
import com.example.takeaway.starred.model.StarredState
import com.example.takeaway.starred.model.StarredStatus
import com.example.takeaway.starred.model.StarredWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarredViewModel @Inject constructor(
    private val getStarredWords: GetStarredWords
): BaseViewModel<StarredAction, StarredState, StarredEvent>() {

    override val initialState: StarredState
        get() = StarredState()

    override fun submit(action: StarredAction) {
        when(action) {
            StarredAction.LoadStarredWords -> loadWords()
        }
    }

    private fun loadWords() {
        viewModelScope.launch {
            getStarredWords(Unit).collectLatest { result ->
                result.onSuccess { words ->
                    updateState(state.value.copy(status = StarredStatus.Result(
                        words.map { StarredWord(it) }
                    )))
                }
            }
        }
    }
}
