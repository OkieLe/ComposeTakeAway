package com.example.takeaway.data.local.mapper

import com.example.takeaway.data.local.entity.Chengyu
import com.example.takeaway.data.local.entity.Hanzi
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import org.junit.Test

class HanziInfoMapperTest {
    private val infoMapper: HanziInfoMapper = HanziInfoMapper()

    @Test
    fun `given a 'Hanzi' when 'toZiInfo' then return 'ZiInfo'`() {
        val hanzi = Hanzi(
            id = 0, character = "字", pinyin = "zi", abbrev = "zi", tone = 4,
            strokes = 6, radicals = "子", oldCharacter = "字", explanation = null, more = null)

        val ziInfo = infoMapper.toZiInfo(hanzi)

        ziInfo.id shouldBe 0
        ziInfo.name shouldBe "字"
        ziInfo.pinyin shouldBe "zi"
        ziInfo.strokes shouldBe 6
        ziInfo.radicals shouldBe "子"
        ziInfo.oldName shouldBe "字"
        ziInfo.explanation shouldHaveLength 0
        ziInfo.more shouldHaveLength 0
    }

    @Test
    fun `given a 'Chengyu' when 'toCiInfo' then return 'CiInfo'`() {
        val chengyu = Chengyu(
            id = 1, word = "一心一意", pinyin = "yi xin yi yi", abbrev = "yxyy", asciiPinyin = "yixinyiyi",
            derivation = "无", explanation = null, example = null)

        val ciInfo = infoMapper.toCiInfo(chengyu)

        ciInfo.id shouldBe 1
        ciInfo.word shouldBe "一心一意"
        ciInfo.pinyin shouldBe "yi xin yi yi"
        ciInfo.derivation shouldBe "无"
        ciInfo.explanation shouldHaveLength 0
        ciInfo.example shouldHaveLength 0
    }
}
