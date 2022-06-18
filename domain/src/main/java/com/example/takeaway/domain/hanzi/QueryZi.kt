package com.example.takeaway.domain.hanzi

import com.example.takeaway.data.HanziRepository
import com.example.takeaway.data.model.ZiInfo
import com.example.takeaway.domain.base.UseCase
import com.example.takeaway.domain.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class QueryZi @Inject constructor(
    private val hanziRepository: HanziRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
): UseCase<String, List<ZiInfo>>(dispatcher) {

    override suspend fun execute(parameters: String): List<ZiInfo> {
        return hanziRepository.searchZi(parameters)
    }
}
