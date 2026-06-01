package com.word.flow.ui.viewed.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.word.flow.R
import com.word.flow.ui.add
import com.word.flow.ui.navigation.LocalAppPadding
import com.word.flow.ui.only
import com.word.flow.ui.viewed.ViewedSegmentedButtonRow

@Composable
fun FavoritesScreen(
    onSelectWord: (String) -> Unit,
    vm: FavoritesViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsState()
    val haveAnyWords by vm.haveAnyWords.collectAsState()
    val words by vm.words.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                LocalAppPadding.current.only(start = true, top = true, end = true).add(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp
                )
            ),
    ) {
        if (!haveAnyWords) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp + LocalAppPadding.current.calculateBottomPadding())
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "No favorite words yet.",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            ViewedSegmentedButtonRow(state.isKnown, vm::setKnown)

            if (!words.isEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = LocalAppPadding.current.only(bottom = true),
                    modifier = Modifier
                        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    0f to Color.Transparent,
                                    (16.dp.toPx() / size.height / 2) to Color.Red,
                                    1f to Color.Red
                                ),
                                blendMode = BlendMode.DstIn,
                            )
                        }
                ) {
                    item { Spacer(Modifier.height(4.dp)) }
                    item {
                        Card {
                            OutlinedTextField(
                                value = state.query,
                                onValueChange = vm::setQuery,
                                singleLine = true,
                                label = { Text("Filter (* wildcard supported)") },
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                            )
                        }
                    }
                    items(words) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectWord(item.word) },
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(item.word)
                                IconButton(onClick = {
                                    vm.setFavorite(
                                        item.word,
                                        !item.isFavorite
                                    )
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_favorite),
                                        contentDescription = "Remove from favorites",
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }
            } else {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp + LocalAppPadding.current.calculateBottomPadding())
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No favorite ${if (state.isKnown) "known" else "unknown"} words yet.",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
