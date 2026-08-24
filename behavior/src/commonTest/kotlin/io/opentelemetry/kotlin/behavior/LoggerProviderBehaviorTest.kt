package io.opentelemetry.kotlin.behavior

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class LoggerProviderBehaviorTest {

    @Test
    fun logLimitsStartUnset() {
        assertNull(LoggerProviderBehavior().logLimits)
    }

    @Test
    fun consoleStartUnset() {
        assertNull(LoggerProviderBehavior().console)
    }

    @Test
    fun mergesLogLimitsWhenBothLayersSuppliedThem() {
        val merged = LoggerProviderBehavior(
            logLimits = LogLimitsBehavior(attributeCountLimit = 1, attributeValueLengthLimit = 3),
        ).mergeWith(
            LoggerProviderBehavior(logLimits = LogLimitsBehavior(attributeValueLengthLimit = 99)),
        )

        assertEquals(1, merged.logLimits?.attributeCountLimit)
        assertEquals(99, merged.logLimits?.attributeValueLengthLimit)
    }

    @Test
    fun staysUnsetWhenNeitherLayerConfiguredConsole() {
        assertNull(LoggerProviderBehavior().mergeWith(LoggerProviderBehavior()).console)
    }

    @Test
    fun adoptsConsoleFromWhicheverLayerSuppliedThem() {
        val console = ConsoleExporterBehavior()

        assertEquals(
            console,
            LoggerProviderBehavior().mergeWith(LoggerProviderBehavior(console = console)).console,
        )
        assertEquals(
            console,
            LoggerProviderBehavior(console = console).mergeWith(LoggerProviderBehavior()).console,
        )
    }
}
