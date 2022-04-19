package com.example.takeaway.data.local

import com.example.takeaway.data.local.dao.WordDao
import com.example.takeaway.data.local.mapper.WordInfoMapper
import com.example.takeaway.data.model.WordInfo

class LocalWordsDataSource(
    private val wordInfoMapper: WordInfoMapper,
    private val wordDao: WordDao
) {
    val starredWords = wordDao.getAllWords()

    suspend fun starWord(wordInfos: List<WordInfo>) {
        wordDao.insert(*(wordInfos.map {
            wordInfoMapper.fromWordInfo(it)
        }.toTypedArray()))
    }

    suspend fun unStarWord(word: String) {
        wordDao.findWord(word).toTypedArray().let {
            wordDao.delete(*it)
        }
    }

    suspend fun isWordStarred(word: String): Boolean {
        return wordDao.findWord(word).isNotEmpty()
    }

    suspend fun getWord(word: String): List<WordInfo> {
        return wordDao.findWord(word = word).map {
            wordInfoMapper.toWordInfo(it)
        }
    }
}
