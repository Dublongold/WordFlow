package com.word.flow.ui.home.lettercalculator.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.word.flow.R

data class LetterCalculatorHistoryItem(
    val id: Long,
    val content: String,
    val count: Int,
) {
    @Composable
    fun Content(onClick: () -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) {
        Card(modifier = modifier.width(IntrinsicSize.Max), onClick = onClick) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .padding(start = 16.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        content,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        "Letters: $count",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(painterResource(id = R.drawable.ic_delete), contentDescription = "Delete")
                }
            }
        }
    }

    @Composable
    operator fun invoke(onClick: () -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) =
        Content(onClick, onDelete, modifier)
}

@Preview
@Composable
private fun LetterCalculatorHistoryItemPreview() {

    val item = remember {
        val content = "example"
        LetterCalculatorHistoryItem(0, content, content.length)
    }
    item(onClick = {}, onDelete = {})
}
