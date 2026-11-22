package org.tasks.preferences

import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Desktop UI scale. An explicit choice in settings wins. Otherwise
 * [ENV_VAR] is used when it is a factor between [MIN_FACTOR] and [MAX_FACTOR],
 * and the app stays at [DEFAULT_PERCENT] when neither is set.
 */
object UiScale {
    const val ENV_VAR = "TASKS_UI_SCALE"
    const val UNSET = 0
    const val DEFAULT_PERCENT = 100
    const val MIN_FACTOR = 0.5f
    const val MAX_FACTOR = 3f

    val presets: List<Int> = listOf(100, 110, 125, 150, 175, 200)

    fun isPreset(percent: Int): Boolean = percent in presets

    fun factorForPercent(percent: Int): Float = percent / 100f

    fun parseFactor(raw: String?): Float? {
        val value = raw?.trim()?.toFloatOrNull() ?: return null
        if (!value.isFinite() || value < MIN_FACTOR || value > MAX_FACTOR) return null
        return value
    }

    fun resolve(savedPercent: Int, environmentFactor: Float?): Float =
        if (isPreset(savedPercent)) {
            factorForPercent(savedPercent)
        } else {
            environmentFactor ?: factorForPercent(DEFAULT_PERCENT)
        }

    fun matchingPreset(factor: Float): Int? =
        presets.firstOrNull { abs(it / 100f - factor) < 0.001f }

    fun percentLabel(factor: Float): String {
        val percent = factor * 100f
        val rounded = percent.roundToInt()
        if (abs(percent - rounded) < 0.05f) return "$rounded%"
        val tenths = (percent * 10f).roundToInt()
        val whole = tenths / 10
        val fraction = abs(tenths % 10)
        return "$whole.$fraction%"
    }
}
