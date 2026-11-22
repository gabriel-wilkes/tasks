package org.tasks.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize

/**
 * Scales layout and text by [scale] on top of the platform density.
 *
 * The factor is a [Density], so text and vectors are rasterized at the larger
 * pixel size instead of the window bitmap being scaled up. [WindowInfo.containerDpSize]
 * is divided by the same factor: breakpoints read that size, and leaving it alone
 * would keep laying the window out as if the content had not grown.
 */
@Composable
fun ProvideUiScale(scale: Float, content: @Composable () -> Unit) {
    if (scale == 1f) {
        content()
        return
    }
    val base = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val density = remember(base.density, base.fontScale, scale) {
        Density(density = base.density * scale, fontScale = base.fontScale)
    }
    val scaledWindowInfo = remember(windowInfo, scale) {
        ScaledDpWindowInfo(windowInfo, scale)
    }
    CompositionLocalProvider(
        LocalDensity provides density,
        LocalWindowInfo provides scaledWindowInfo,
        content = content,
    )
}

internal fun scaledContainerDp(size: DpSize, scale: Float): DpSize {
    if (scale == 1f || size.width.value <= 0f || size.height.value <= 0f) return size
    return DpSize(size.width / scale, size.height / scale)
}

private class ScaledDpWindowInfo(
    private val delegate: WindowInfo,
    private val scale: Float,
) : WindowInfo by delegate {
    override val containerDpSize: DpSize
        get() = scaledContainerDp(delegate.containerDpSize, scale)
}
