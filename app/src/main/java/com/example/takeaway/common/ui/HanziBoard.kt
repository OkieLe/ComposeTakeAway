package com.example.takeaway.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.takeaway.R
import com.example.takeaway.common.model.ExtraInfo
import com.example.takeaway.common.model.HanziItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HanziBoard(
    hanziItems: List<HanziItem>,
    showPrevious: Boolean = false,
    onPreviousClick: () -> Unit = {},
    showNext: Boolean = false,
    onNextClick: () -> Unit = {}
) {
    val initialPage = hanziItems.size / 2
    val pagerState = rememberPagerState(initialPage)
    LaunchedEffect(hanziItems) {
        pagerState.scrollToPage(initialPage)
    }
    Column {
        if (hanziItems.size > 1) {
            PagedIndicator(
                pagerState = pagerState,
                showPrevious, onPreviousClick,
                showNext, onNextClick
            )
        }
        HorizontalPager(count = hanziItems.size, state = pagerState) { page ->
            HanziInfoItem(hanziItems[page])
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PagedIndicator(
    pagerState: PagerState,
    hasPrevious: Boolean, onPreviousClick: () -> Unit,
    hasNext: Boolean, onNextClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        if (hasPrevious) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { onPreviousClick() },
                painter = painterResource(id = R.drawable.ic_show_previous),
                contentDescription = stringResource(id = R.string.label_previous)
            )
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.Center),
            activeColor = MaterialTheme.colors.primary,
            indicatorWidth = 6.dp
        )
        if (hasNext) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { onNextClick() },
                painter = painterResource(id = R.drawable.ic_show_next),
                contentDescription = stringResource(id = R.string.label_next)
            )
        }
    }
}

@Composable
private fun HanziInfoItem(hanziItem: HanziItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        BasicFields(hanziItem)
        when (val extra = hanziItem.extraInfo) {
            is ExtraInfo.Zi -> ZiExtraFields(extra)
            is ExtraInfo.Ci -> CiExtraFields(extra)
        }
        hanziItem.explanation.takeIf { it.isNotBlank() }?.let {
            CommonField(stringResource(id = R.string.label_hanzi_explanation), it)
        }
    }
}

@Composable
private fun BasicFields(hanziItem: HanziItem) {
    Card(
        modifier = Modifier.padding(horizontal = 4.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            HanziText(modifier = Modifier.align(Alignment.Bottom), hanziItem.name)
            PinyinText(modifier = Modifier.align(Alignment.Bottom), pinyin = hanziItem.pinyin)
        }
    }
}

@Composable
private fun CommonField(label: String, content: String) {
    Spacer(modifier = Modifier.height(12.dp))
    Card(modifier = Modifier.padding(horizontal = 4.dp), elevation = 4.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun CiExtraFields(extra: ExtraInfo.Ci) {
    CommonField(label = stringResource(id = R.string.label_ci_derivation), content = extra.derivation)
}

@Composable
fun ZiExtraFields(extra: ExtraInfo.Zi) {
    Spacer(modifier = Modifier.height(12.dp))
    Card(modifier = Modifier.padding(horizontal = 4.dp), elevation = 4.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            LabeledText(label = stringResource(id = R.string.label_zi_strokes), content = extra.strokes.toString())
            LabeledText(label = stringResource(id = R.string.label_zi_radicals), content = extra.radicals)
            LabeledText(label = stringResource(id = R.string.label_zi_old_word), content = extra.oldName)
        }
    }
}

@Composable
private fun LabeledText(label: String, content: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Row {
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.6f)
        )
        Text(
            text = content, style = MaterialTheme.typography.body2,
            color = Color.Black.copy(alpha = 0.75f)
        )
    }
}

@Composable
private fun PinyinText(modifier: Modifier, pinyin: String) {
    Text(
        modifier = modifier.padding(all = 4.dp),
        text = pinyin,
        style = MaterialTheme.typography.h6
    )
}

@Composable
private fun HanziText(modifier: Modifier, word: String) {
    Text(
        modifier = modifier.padding(horizontal = 8.dp),
        text = word,
        style = MaterialTheme.typography.h4
    )
}
