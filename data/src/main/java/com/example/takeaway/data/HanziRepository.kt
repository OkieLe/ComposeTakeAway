package com.example.takeaway.data

import com.example.takeaway.data.local.HanziDataSource
import com.example.takeaway.data.model.CiInfo
import com.example.takeaway.data.model.ZiInfo

class HanziRepository(
    private val hanziDataSource: HanziDataSource
) {

    companion object {
        private val pinyinRegex = "^[a-zA-Z]+\$".toRegex()
        private val hanziRegex = "^[\\u4e00-\\u9fa5]+\$".toRegex()
    }

    suspend fun searchZi(keyword: String): List<ZiInfo> {
        return when {
            pinyinRegex.matches(keyword) -> hanziDataSource.searchHanziByPinyin(keyword)
            hanziRegex.matches(keyword) && keyword.length == 1 -> hanziDataSource.searchHanziByHanzi(keyword)
            else -> emptyList()
        }
    }

    suspend fun getZi(name: String) = hanziDataSource.getHanzi(name)

    suspend fun searchCi(keyword: String): List<CiInfo> {
        return when {
            pinyinRegex.matches(keyword) -> hanziDataSource.searchChengyuByPinyin(keyword)
            hanziRegex.matches(keyword) -> hanziDataSource.searchChengyuByHanzi(keyword)
            else -> emptyList()
        }
    }

    suspend fun getCi(name: String) = hanziDataSource.getChengyu(name)
}
