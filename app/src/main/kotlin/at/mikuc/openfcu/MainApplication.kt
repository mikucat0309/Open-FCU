package at.mikuc.openfcu

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import at.mikuc.openfcu.course.search.CourseSearchViewModel
import at.mikuc.openfcu.qrcode.QrcodeViewModel
import at.mikuc.openfcu.redirect.RedirectViewModel
import at.mikuc.openfcu.setting.SettingViewModel
import at.mikuc.openfcu.setting.UserPreferenceRepository
import at.mikuc.openfcu.timetable.TimetableViewModel
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
    singleOf(::UserPreferenceRepository)
    singleOf(::FcuRepository)

    viewModelOf(::CourseSearchViewModel)
    viewModelOf(::QrcodeViewModel)
    viewModelOf(::RedirectViewModel)
    viewModelOf(::SettingViewModel)
    viewModelOf(::TimetableViewModel)
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
