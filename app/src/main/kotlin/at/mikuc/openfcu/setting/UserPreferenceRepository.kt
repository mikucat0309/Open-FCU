package at.mikuc.openfcu.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent

class UserPreferenceRepository(private val dataStore: DataStore<Preferences>) : KoinComponent {

    private suspend fun <T> get(key: Preferences.Key<T>): T? =
        dataStore.data.map { it[key] }.firstOrNull()

    private suspend fun <T> set(key: Preferences.Key<T>, value: T) =
        dataStore.edit { it[key] = value }

    suspend fun getCredential(): Credential {
        val id = get(KEY_ID) ?: ""
        val password = get(KEY_PASSWORD) ?: ""
        return Credential(id, password)
    }

    suspend fun setCredential(credential: Credential) {
        set(KEY_ID, credential.id)
        set(KEY_PASSWORD, credential.password)
    }

    companion object {
        val KEY_ID = stringPreferencesKey("ID")
        val KEY_PASSWORD = stringPreferencesKey("Password")
    }
}
