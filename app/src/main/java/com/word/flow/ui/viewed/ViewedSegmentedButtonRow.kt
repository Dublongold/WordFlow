package com.word.flow.ui.viewed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ViewedSegmentedButtonRow(
    isKnown: Boolean,
    updateKnown: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        val colors = SegmentedButtonDefaults.colors().let {
            it.copy(
                inactiveContainerColor = it.activeContainerColor.copy(.66f),
            )
        }
        SegmentedButton(
            selected = isKnown,
            onClick = { updateKnown(true) },
            colors = colors,
            shape = SegmentedButtonDefaults.itemShape(0, 2),
        ) { Text("Known") }
        SegmentedButton(
            selected = !isKnown,
            onClick = { updateKnown(false) },
            colors = colors,
            shape = SegmentedButtonDefaults.itemShape(1, 2),
        ) { Text("Unknown") }
    }
}

@Preview
@Composable
private fun ViewedSegmentedButtonRowPreview() {
    var isKnown by remember {
        mutableStateOf(true)
    }
    ViewedSegmentedButtonRow(
        isKnown = isKnown,
        updateKnown = { isKnown = it },
    )
}