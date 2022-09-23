package at.mikuc.openfcu.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.time.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.toJavaDuration

fun <T> runBlocking(duration: Duration, block: suspend CoroutineScope.() -> T): T? = runBlocking {
    withTimeoutOrNull(duration.toJavaDuration(), block)
}
