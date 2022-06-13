package com.example.takeaway.domain.words

import com.example.takeaway.data.WordsRepository
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.domain.base.UseCase
import com.example.takeaway.domain.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetWord @Inject constructor(
    private val wordsRepository: WordsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
): UseCase<String, List<WordInfo>>(dispatcher) {

    override suspend fun execute(parameters: String): List<WordInfo> {
        return wordsRepository.getWord(parameters)
    }
}
