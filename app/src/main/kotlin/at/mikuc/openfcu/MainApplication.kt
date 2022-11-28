package at.mikuc.openfcu

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import at.mikuc.openfcu.course.detail.CourseDetailRepository
import at.mikuc.openfcu.course.detail.CourseDetailViewModel
import at.mikuc.openfcu.course.search.CourseSearchViewModel
import at.mikuc.openfcu.qrcode.QrcodeViewModel
import at.mikuc.openfcu.redirect.RedirectViewModel
import at.mikuc.openfcu.setting.SettingViewModel
import at.mikuc.openfcu.setting.UserPreferenceRepository
import at.mikuc.openfcu.timetable.TimetableViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val myModule = module {
    single {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("settings") }
        )
    }
    single {
        Json {
            prettyPrint = true
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) { json(get()) }
            install(HttpCookies)
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.BODY
            }
        }
    }
    singleOf(::UserPreferenceRepository)
    singleOf(::FcuRepository)
    singleOf(::CourseDetailRepository)

    viewModelOf(::CourseSearchViewModel)
    viewModelOf(::QrcodeViewModel)
    viewModelOf(::RedirectViewModel)
    viewModelOf(::SettingViewModel)
    viewModelOf(::TimetableViewModel)
    viewModelOf(::CourseDetailViewModel)
}

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(myModule)
        }
    }
}
