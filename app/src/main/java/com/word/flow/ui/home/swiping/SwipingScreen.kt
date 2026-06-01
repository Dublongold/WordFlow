package com.word.flow.ui.home.swiping

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.word.flow.data.local.datastore.FakeAppPreferences
import com.word.flow.data.local.db.dao.fake.FakeEncounteredWordDao
import com.word.flow.data.local.db.dao.fake.FakeSeedWordDao
import com.word.flow.data.repository.EncounteredWordRepositoryImpl
import com.word.flow.data.repository.SeedRepositoryImpl
import com.word.flow.domain.usecase.GetSwipePoolUseCase
import com.word.flow.domain.usecase.SwipeWordUseCase
import com.word.flow.ui.horizontal
import com.word.flow.ui.navigation.LocalAppPadding
import com.word.flow.ui.only
import com.word.flow.ui.theme.WordFlowTheme
import kotlinx.coroutines.launch

@Composable
fun SwipingScreen(vm: SwipingViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()

    if (state.isFinished) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(LocalAppPadding.current)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "All words reviewed!",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
            )
            Button(
                onClick = vm::reload,
                Modifier
                    .widthIn(min = 150.dp)
                    .fillMaxWidth(.5f),
                shape = MaterialTheme.shapes.medium
            ) { Text("Reload pool") }
        }
    } else {
        Column(Modifier.fillMaxSize()) {
            var useMinWidth by remember { mutableStateOf(false) }
            val minWidth = with(LocalDensity.current) { 300.dp.toPx() }
            BoxWithConstraints(
                Modifier
                    .padding(LocalAppPadding.current.only(top = true, bottom = true))
                    .run {
                        if (useMinWidth) {
                            width(320.dp).fillMaxHeight()
                        } else aspectRatio(1f)
                    }
                    .onPlaced {
                        if (it.size.width < minWidth) {
                            useMinWidth = true
                        }
                    }
                    .align(Alignment.CenterHorizontally)
            ) {
                if (state.nextWord != null) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(LocalAppPadding.current.horizontal)
                            .fillMaxSize()
                    ) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Text(
                                text = state.nextWord.orEmpty(),
                                style = MaterialTheme.typography.displayMedium,
                                modifier = Modifier.padding(24.dp),
                            )
                        }
                    }
                }
                val density = LocalDensity.current

                var currentOffsetX by remember(state.currentWord) { mutableIntStateOf(0) }
                val cardAnimation = remember(state.currentWord) { Animatable(0f) }
                var isAnimating by remember { mutableStateOf(false) }
                var isKnown by remember { mutableStateOf(null as Boolean?) }
                LaunchedEffect(isKnown) {
                    isKnown?.let { isKnown ->
                        isAnimating = true
                        val minDimen = minOf(maxHeight, maxWidth)
                        val expectedOffset =
                            (maxWidth.value * 1.5f) + (16 * density.density + minDimen.value)
                        val value = if (isKnown) {
                            -expectedOffset
                        } else {
                            expectedOffset
                        }
                        cardAnimation.animateTo(value - currentOffsetX)
                        if (isKnown) {
                            vm.swipeKnown()
                        } else {
                            vm.swipeUnknown()
                        }
                        isAnimating = false
                    }
                    isKnown = null
                }
                val interactiveUIOffset = (currentOffsetX + cardAnimation.value).dp
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(LocalAppPadding.current.horizontal)
                        .fillMaxSize()
                        .offset(x = interactiveUIOffset)
                        .pointerInput(state.currentWord) {
                            if (state.currentWord != null) {
                                var x = 0
                                var initX = 0
                                val expectedDiff = size.width / 4
                                detectDragGestures(
                                    onDragStart = {
                                        x = it.x.toInt()
                                        initX = x
                                    },
                                    onDragEnd = {
                                        if (x - initX < -expectedDiff) {
                                            isKnown = true
                                        } else if (x - initX > expectedDiff) {
                                            isKnown = false
                                        } else {
                                            x = 0
                                            initX = 0
                                            currentOffsetX = 0
                                        }
                                    }
                                ) { change, _ ->
                                    val c = change.positionChange().x.toInt()
                                    x += c
                                    currentOffsetX += (c / density.density).toInt()
                                }
                            }
                        }
                ) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(
                            text = state.currentWord.orEmpty(),
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(24.dp),
                        )
                    }
                }
                Button(
                    onClick = {
                        isKnown = true
                    },
                    modifier = Modifier
                        .padding((16 + 12).dp)
                        .padding(LocalAppPadding.current.horizontal)
                        .align(Alignment.BottomStart)
                        .offset(x = interactiveUIOffset),
                    enabled = isAnimating.not(),
                    shape = MaterialTheme.shapes.extraSmall,
                ) {
                    Text("Known")
                }
                Button(
                    onClick = {
                        isKnown = false
                    },
                    modifier = Modifier
                        .padding((16 + 12).dp)
                        .padding(LocalAppPadding.current.horizontal)
                        .align(Alignment.BottomEnd)
                        .offset(x = interactiveUIOffset),
                    enabled = isAnimating.not(),
                    shape = MaterialTheme.shapes.extraSmall,
                ) {
                    Text("Unknown")
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwipingScreenPreview() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    WordFlowTheme {
        Surface {
            SwipingScreen(
                vm = viewModel {
                    val encounteredWordRepository = EncounteredWordRepositoryImpl(
                        dao = FakeEncounteredWordDao(),
                    )
                    val seedRepository = SeedRepositoryImpl(
                        context,
                        seedDao = FakeSeedWordDao(),
                        preferences = FakeAppPreferences(),
                    )
                    SwipingViewModel(
                        getSwipePoolUseCase = GetSwipePoolUseCase(
                            seedRepository = seedRepository,
                        ), swipeWordUseCase = SwipeWordUseCase(
                            encounteredWordRepository = encounteredWordRepository,
                        )
                    ).also {
                        scope.launch {
                            seedRepository.ensureSeeded()
                            it.reload()
                        }
                    }
                })
        }
    }
}