package com.example.takeaway.data.local.mapper

import com.example.takeaway.data.local.entity.Chengyu
import com.example.takeaway.data.local.entity.Hanzi
import com.example.takeaway.data.model.CiInfo
import com.example.takeaway.data.model.ZiInfo

class HanziInfoMapper {

    fun toZiInfo(hanzi: Hanzi): ZiInfo {
        return ZiInfo(
            id = hanzi.id,
            name = hanzi.character,
            pinyin = hanzi.pinyin,
            strokes = hanzi.strokes,
            radicals = hanzi.radicals,
            oldName = hanzi.oldCharacter,
            explanation = hanzi.explanation.orEmpty(),
            more = hanzi.more.orEmpty()
        )
    }

    fun toCiInfo(chengyu: Chengyu): CiInfo {
        return CiInfo(
            id = chengyu.id,
            word = chengyu.word,
            pinyin = chengyu.pinyin,
            derivation = chengyu.derivation.orEmpty(),
            explanation = chengyu.explanation.orEmpty(),
            example = chengyu.example.orEmpty()
        )
    }
}
