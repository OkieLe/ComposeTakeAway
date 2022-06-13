package com.example.takeaway.domain.words

import com.example.takeaway.data.WordsRepository
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.domain.base.FlowUseCase
import com.example.takeaway.domain.base.Result
import com.example.takeaway.domain.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchWord @Inject constructor(
    private val wordsRepository: WordsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
): FlowUseCase<String, List<WordInfo>>(dispatcher) {

    override fun execute(parameters: String): Flow<Result<List<WordInfo>>> {
        return flow {
            emit(Result.Success(wordsRepository.searchWord(parameters)))
        }
    }
}
