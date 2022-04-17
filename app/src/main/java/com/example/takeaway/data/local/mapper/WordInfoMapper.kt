package com.example.takeaway.data.local.mapper

import com.example.takeaway.data.local.entity.Word
import com.example.takeaway.data.model.WordInfo
import com.google.gson.Gson
import javax.inject.Inject

class WordInfoMapper @Inject constructor(
    private val gson: Gson
) {

    fun toWordInfo(word: Word): WordInfo {
        return gson.fromJson(word.info, WordInfo::class.java)
    }

    fun fromWordInfo(wordInfo: WordInfo): Word {
        return Word(word = wordInfo.word, info = gson.toJson(wordInfo))
    }
}
