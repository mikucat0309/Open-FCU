package at.mikuc.fcuassistant.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {
    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile("settings") }
        )
    }
}

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun <T> get(key: Preferences.Key<T>): T? = dataStore.data.map { it[key] }.firstOrNull()


    suspend fun <T> set(key: Preferences.Key<T>, value: T) = dataStore.edit { it[key] = value }

    companion object {
        val KEY_ID = stringPreferencesKey("ID")
        val KEY_PASSWORD = stringPreferencesKey("Password")
    }
}


