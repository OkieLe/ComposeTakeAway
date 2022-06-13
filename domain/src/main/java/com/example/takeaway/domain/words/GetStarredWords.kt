package com.example.takeaway.domain.words

import com.example.takeaway.data.WordsRepository
import com.example.takeaway.domain.base.FlowUseCase
import com.example.takeaway.domain.base.Result
import com.example.takeaway.domain.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetStarredWords @Inject constructor(
    private val wordsRepository: WordsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
): FlowUseCase<Unit, List<String>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<List<String>>> {
        return wordsRepository.getStarredWords().mapLatest {
            Result.Success(it)
        }
    }
}
