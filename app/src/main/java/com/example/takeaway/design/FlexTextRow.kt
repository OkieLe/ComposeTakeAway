package com.example.takeaway.design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow

private val colorList = listOf(
    Color(0xFF939391),
    Color(0xFF8696a7),
    Color(0xFF7b8b6f),
    Color(0xFF965454),
    Color(0xFF7a7281),
    Color(0xFF6b5152),
    Color(0xFF656565)
)

@Composable
fun FlexTextRow(
    modifier: Modifier,
    textList: List<String>,
    style: TextStyle,
    itemClickListener: (String) -> Unit
) {
    FlowRow(modifier = modifier) {
        textList.forEach {
            ColorfulText(
                text = it,
                textStyle = style,
                itemClickListener
            )
        }
    }
}

@Composable
private fun ColorfulText(text: String, textStyle: TextStyle, itemClickListener: (String) -> Unit) {
    val color = colorList.random()
    Text(
        modifier = Modifier
            .padding(vertical = 2.dp, horizontal = 3.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Color.Black.copy(alpha = 0.2f))
            .clickable { itemClickListener(text) }
            .padding(vertical = 6.dp, horizontal = 10.dp),
        text = text,
        style = textStyle.copy(color = color)
    )
}
