package com.example.takeaway.common.mapper

import com.example.takeaway.common.model.ExtraInfo
import com.example.takeaway.common.model.HanziBrief
import com.example.takeaway.common.model.HanziItem
import com.example.takeaway.data.model.CiInfo
import com.example.takeaway.data.model.ZiInfo
import javax.inject.Inject

class HanziItemMapper @Inject constructor() {
    fun fromZiInfo(ziInfo: ZiInfo): HanziItem {
        return with(ziInfo) {
            HanziItem(
                id = id,
                name = name,
                pinyin = pinyin,
                extraInfo = ExtraInfo.Zi(
                    strokes = strokes,
                    radicals = radicals,
                    oldName = oldName
                ),
                explanation = explanation
            )
        }
    }

    fun fromCiInfo(ciInfo: CiInfo): HanziItem {
        return with(ciInfo) {
            HanziItem(
                id = id,
                name = word,
                pinyin = pinyin,
                extraInfo = ExtraInfo.Ci(
                    derivation = derivation
                ),
                explanation = explanation
            )
        }
    }

    fun toHanziBrief(hanziItem: HanziItem): HanziBrief {
        return with(hanziItem) {
            HanziBrief(
                id = id,
                name = name,
                pinyin = pinyin
            )
        }
    }
}
