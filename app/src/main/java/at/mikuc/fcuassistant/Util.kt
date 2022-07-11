package at.mikuc.fcuassistant

import android.net.Uri

fun Uri.replaceUriParameter(key: String, newValue: String): Uri {
    val params = queryParameterNames
    return with(buildUpon()) {
        clearQuery()
        params.filterNot { it == key }
            .forEach { appendQueryParameter(it, getQueryParameter(it)) }
        appendQueryParameter(key, newValue)
        build()
    }
}