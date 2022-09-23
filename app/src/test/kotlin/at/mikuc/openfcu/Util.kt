package at.mikuc.openfcu

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic

fun mockLogger() {
    mockkStatic(Log::class)
    every { Log.v(any(), any()) } returns 0
    every { Log.d(any(), any()) } returns 0
    every { Log.i(any(), any()) } returns 0
    every { Log.w(any(), any<String>()) } returns 0
    every { Log.e(any(), any()) } returns 0
    every { Log.wtf(any(), any<String>()) } returns 0
}
