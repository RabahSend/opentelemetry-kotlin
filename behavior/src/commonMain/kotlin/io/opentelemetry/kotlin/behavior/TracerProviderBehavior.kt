package io.opentelemetry.kotlin.behavior

import io.opentelemetry.kotlin.ExperimentalApi

/**
 * Behavior of tracing.
 *
 * https://opentelemetry.io/docs/specs/otel/trace/sdk/#tracer-provider
 */
@ExperimentalApi
data class TracerProviderBehavior(

    /**
     * Limits on span data capture.
     */
    val spanLimits: SpanLimitsBehavior? = null,

    /**
     * Console span exporter. Selecting it is the whole configuration.
     */
    val console: ConsoleExporterBehavior? = null,
) : Behavior<TracerProviderBehavior> {

    override fun mergeWith(higher: TracerProviderBehavior): TracerProviderBehavior = copy(
        spanLimits = mergeNode(spanLimits, higher.spanLimits),
        console = mergeNode(console, higher.console),
    )
}
