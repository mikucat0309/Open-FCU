package at.mikuc.openfcu.util

import android.util.Log
import at.mikuc.openfcu.TAG
import io.ktor.util.network.*
import io.ktor.utils.io.errors.*

suspend fun <T> catchNetworkException(block: suspend () -> T): T? {
    try {
        return block()
    } catch (e: UnresolvedAddressException) {
        Log.e(TAG, "UnresolvedAddressException")
    } catch (e: IOException) {
        Log.e(TAG, "IOException")
    }
    return null
}
