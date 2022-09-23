package at.mikuc.openfcu.util

import android.util.Log
import at.mikuc.openfcu.TAG

fun Throwable.logStackTrace() {
    Log.e(TAG, this.stackTraceToString())
}
