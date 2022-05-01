package com.example.takeaway.data.local

import com.example.takeaway.data.local.dao.ChengyuDao
import com.example.takeaway.data.local.dao.HanziDao
import com.example.takeaway.data.local.mapper.HanziInfoMapper

class HanziDataSource(
    private val hanziDao: HanziDao,
    private val chengyuDao: ChengyuDao,
    private val hanziInfoMapper: HanziInfoMapper
) {
    suspend fun getHanzi(name: String) = hanziDao.findByName(name).map(hanziInfoMapper::toZiInfo)

    suspend fun searchHanziByPinyin(keyword: String) = hanziDao.findByPinyin(keyword).map(hanziInfoMapper::toZiInfo)

    suspend fun searchHanziByHanzi(keyword: String) = hanziDao.findByHanzi(keyword).map(hanziInfoMapper::toZiInfo)

    suspend fun getChengyu(word: String) = chengyuDao.findByWord(word).map(hanziInfoMapper::toCiInfo)

    suspend fun searchChengyuByPinyin(keyword: String) = chengyuDao.findByPinyin(keyword).map(hanziInfoMapper::toCiInfo)

    suspend fun searchChengyuByHanzi(keyword: String) = chengyuDao.findByHanzi(keyword).map(hanziInfoMapper::toCiInfo)
}
