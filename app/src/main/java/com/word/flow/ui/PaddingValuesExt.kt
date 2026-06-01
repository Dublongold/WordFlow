@file:Suppress("unused")

package com.word.flow.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun PaddingValues.plus(value: PaddingValues) = PaddingValues(
    start = this.calculateStartPadding(LocalLayoutDirection.current) + value.calculateStartPadding(
        LocalLayoutDirection.current
    ),
    top = this.calculateTopPadding() + value.calculateTopPadding(),
    end = this.calculateEndPadding(LocalLayoutDirection.current) + value.calculateEndPadding(
        LocalLayoutDirection.current
    ),
    bottom = this.calculateBottomPadding() + value.calculateBottomPadding(),
)

fun PaddingValues.plus(value: PaddingValues, layoutDirection: LayoutDirection) = PaddingValues(
    start = this.calculateStartPadding(layoutDirection) + value.calculateStartPadding(
        layoutDirection
    ),
    top = this.calculateTopPadding() + value.calculateTopPadding(),
    end = this.calculateEndPadding(layoutDirection) + value.calculateEndPadding(layoutDirection),
    bottom = this.calculateBottomPadding() + value.calculateBottomPadding(),
)

private val ZeroDp = 0.dp

@Composable
fun PaddingValues.add(top: Dp = ZeroDp, bottom: Dp = ZeroDp, start: Dp = ZeroDp, end: Dp = ZeroDp) =
    PaddingValues(
        start = this.calculateStartPadding(LocalLayoutDirection.current) + start,
        top = this.calculateTopPadding() + top,
        end = this.calculateEndPadding(LocalLayoutDirection.current) + end,
        bottom = this.calculateBottomPadding() + bottom,
    )

fun PaddingValues.add(
    top: Dp = ZeroDp,
    bottom: Dp = ZeroDp,
    start: Dp = ZeroDp,
    end: Dp = ZeroDp,
    layoutDirection: LayoutDirection
) = PaddingValues(
    start = this.calculateStartPadding(layoutDirection) + start,
    top = this.calculateTopPadding() + top,
    end = this.calculateEndPadding(layoutDirection) + end,
    bottom = this.calculateBottomPadding() + bottom,
)

@Composable
fun PaddingValues.except(
    top: Boolean = false,
    bottom: Boolean = false,
    start: Boolean = false,
    end: Boolean = false,
) = PaddingValues(
    start = if (start) ZeroDp else this.calculateStartPadding(LocalLayoutDirection.current),
    top = if (top) ZeroDp else this.calculateTopPadding(),
    end = if (end) ZeroDp else this.calculateEndPadding(LocalLayoutDirection.current),
    bottom = if (bottom) ZeroDp else this.calculateBottomPadding(),
)

fun PaddingValues.except(
    top: Boolean = false,
    bottom: Boolean = false,
    start: Boolean = false,
    end: Boolean = false,
    layoutDirection: LayoutDirection
) = PaddingValues(
    start = if (start) ZeroDp else this.calculateStartPadding(layoutDirection),
    top = if (top) ZeroDp else this.calculateTopPadding(),
    end = if (end) ZeroDp else this.calculateEndPadding(layoutDirection),
    bottom = if (bottom) ZeroDp else this.calculateBottomPadding(),
)

@Composable
fun PaddingValues.only(
    top: Boolean = false,
    bottom: Boolean = false,
    start: Boolean = false,
    end: Boolean = false,
) = PaddingValues(
    start = if (start) this.calculateStartPadding(LocalLayoutDirection.current) else ZeroDp,
    top = if (top) this.calculateTopPadding() else ZeroDp,
    end = if (end) this.calculateEndPadding(LocalLayoutDirection.current) else ZeroDp,
    bottom = if (bottom) this.calculateBottomPadding() else ZeroDp,
)

fun PaddingValues.only(
    top: Boolean = false,
    bottom: Boolean = false,
    start: Boolean = false,
    end: Boolean = false,
    layoutDirection: LayoutDirection
) = PaddingValues(
    start = if (start) this.calculateStartPadding(layoutDirection) else ZeroDp,
    top = if (top) this.calculateTopPadding() else ZeroDp,
    end = if (end) this.calculateEndPadding(layoutDirection) else ZeroDp,
    bottom = if (bottom) this.calculateBottomPadding() else ZeroDp,
)

@Composable
fun PaddingValues.calculateStartPadding() = this.calculateStartPadding(LocalLayoutDirection.current)

@Composable
fun PaddingValues.calculateEndPadding() = this.calculateEndPadding(LocalLayoutDirection.current)


val PaddingValues.horizontal
    @Composable
    get() = PaddingValues(
        start = this.calculateStartPadding(LocalLayoutDirection.current),
        top = 0.dp,
        end = this.calculateEndPadding(LocalLayoutDirection.current),
        bottom = 0.dp,
    )

fun PaddingValues.horizontal(layoutDirection: LayoutDirection) = PaddingValues(
    start = this.calculateStartPadding(layoutDirection),
    top = 0.dp,
    end = this.calculateEndPadding(layoutDirection),
    bottom = 0.dp,
)

val PaddingValues.vertical
    get() = PaddingValues(
        start = 0.dp,
        top = this.calculateTopPadding(),
        end = 0.dp,
        bottom = this.calculateBottomPadding(),
    )