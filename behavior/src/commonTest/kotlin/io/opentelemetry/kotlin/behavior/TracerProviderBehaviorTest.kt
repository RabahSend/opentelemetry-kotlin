package io.opentelemetry.kotlin.behavior

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class TracerProviderBehaviorTest {

    @Test
    fun spanLimitsStartUnset() {
        assertNull(TracerProviderBehavior().spanLimits)
    }

    @Test
    fun consoleStartUnset() {
        assertNull(TracerProviderBehavior().console)
    }

    @Test
    fun staysUnsetWhenNeitherLayerConfiguredSpanLimits() {
        assertNull(TracerProviderBehavior().mergeWith(TracerProviderBehavior()).spanLimits)
    }

    @Test
    fun staysUnsetWhenNeitherLayerConfiguredConsole() {
        assertNull(TracerProviderBehavior().mergeWith(TracerProviderBehavior()).console)
    }

    @Test
    fun adoptsSpanLimitsFromWhicheverLayerSuppliedThem() {
        val limits = SpanLimitsBehavior(linkCountLimit = 3)

        assertEquals(
            limits,
            TracerProviderBehavior().mergeWith(TracerProviderBehavior(spanLimits = limits)).spanLimits,
        )
        assertEquals(
            limits,
            TracerProviderBehavior(spanLimits = limits).mergeWith(TracerProviderBehavior()).spanLimits,
        )
    }

    @Test
    fun adoptsConsoleFromWhicheverLayerSuppliedThem() {
        val console = ConsoleExporterBehavior()

        assertEquals(
            console,
            TracerProviderBehavior().mergeWith(TracerProviderBehavior(console = console)).console,
        )
        assertEquals(
            console,
            TracerProviderBehavior(console = console).mergeWith(TracerProviderBehavior()).console,
        )
    }

    @Test
    fun mergesSpanLimitsWhenBothLayersSuppliedThem() {
        val merged = TracerProviderBehavior(
            spanLimits = SpanLimitsBehavior(attributeCountLimit = 1, linkCountLimit = 3),
        ).mergeWith(
            TracerProviderBehavior(spanLimits = SpanLimitsBehavior(linkCountLimit = 99)),
        )

        assertEquals(1, merged.spanLimits?.attributeCountLimit)
        assertEquals(99, merged.spanLimits?.linkCountLimit)
    }
}
