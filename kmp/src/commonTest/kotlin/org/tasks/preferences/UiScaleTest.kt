package org.tasks.preferences

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UiScaleTest {

    @Test
    fun unsetScaleUsesTheEnvironmentThenOneHundredPercent() {
        assertEquals(1.25f, UiScale.resolve(UiScale.UNSET, 1.25f))
        assertEquals(1f, UiScale.resolve(UiScale.UNSET, null))
        assertEquals(1.25f, UiScale.resolve(999, 1.25f))
    }

    @Test
    fun aSavedPresetWinsOverTheEnvironment() {
        assertEquals(1.5f, UiScale.resolve(150, 1.25f))
        assertEquals(1f, UiScale.resolve(100, 1.5f))
    }

    @Test
    fun environmentFactorAcceptsTheDesktopRange() {
        assertEquals(1.25f, UiScale.parseFactor(" 1.25 "))
        assertEquals(0.5f, UiScale.parseFactor("0.5"))
        assertEquals(3f, UiScale.parseFactor("3"))
        assertNull(UiScale.parseFactor(null))
        assertNull(UiScale.parseFactor(""))
        assertNull(UiScale.parseFactor(" "))
        assertNull(UiScale.parseFactor("125%"))
        assertNull(UiScale.parseFactor("0"))
        assertNull(UiScale.parseFactor("-1"))
        assertNull(UiScale.parseFactor("4"))
        assertNull(UiScale.parseFactor("nope"))
    }

    @Test
    fun labelsAndPresetMatching() {
        assertEquals(125, UiScale.matchingPreset(1.25f))
        assertEquals(110, UiScale.matchingPreset(1.1f))
        assertNull(UiScale.matchingPreset(1.33f))
        assertEquals("125%", UiScale.percentLabel(1.25f))
        assertEquals("110%", UiScale.percentLabel(1.1f))
        assertEquals("133.3%", UiScale.percentLabel(1.333f))
    }
}
