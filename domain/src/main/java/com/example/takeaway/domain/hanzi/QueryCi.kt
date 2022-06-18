package com.example.takeaway.domain.hanzi

import com.example.takeaway.data.HanziRepository
import com.example.takeaway.data.model.CiInfo
import com.example.takeaway.domain.base.UseCase
import com.example.takeaway.domain.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class QueryCi @Inject constructor(
    private val hanziRepository: HanziRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
): UseCase<String, List<CiInfo>>(dispatcher) {

    override suspend fun execute(parameters: String): List<CiInfo> {
        return hanziRepository.searchCi(parameters)
    }
}
