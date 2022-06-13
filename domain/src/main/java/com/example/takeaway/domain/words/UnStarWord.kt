package com.example.takeaway.domain.words

import com.example.takeaway.data.WordsRepository
import com.example.takeaway.domain.base.UseCase
import com.example.takeaway.domain.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UnStarWord @Inject constructor(
    private val wordsRepository: WordsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
): UseCase<String, Unit>(dispatcher) {

    override suspend fun execute(parameters: String) {
        wordsRepository.unStarWord(parameters)
    }
}
