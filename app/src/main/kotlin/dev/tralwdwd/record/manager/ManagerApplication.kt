package dev.tralwdwd.record.manager

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import dev.tralwdwd.record.manager.di.*
import dev.tralwdwd.record.manager.installers.pm.PMInstaller
import dev.tralwdwd.record.manager.manager.*
import dev.tralwdwd.record.manager.manager.download.AndroidDownloadManager
import dev.tralwdwd.record.manager.manager.download.KtorDownloadManager
import dev.tralwdwd.record.manager.network.services.*
import dev.tralwdwd.record.manager.ui.screens.about.AboutModel
import dev.tralwdwd.record.manager.ui.screens.home.HomeModel
import dev.tralwdwd.record.manager.ui.screens.iconopts.IconOptionsModel
import dev.tralwdwd.record.manager.ui.screens.log.LogScreenModel
import dev.tralwdwd.record.manager.ui.screens.logs.LogsListScreenModel
import dev.tralwdwd.record.manager.ui.screens.patching.PatchingScreenModel
import dev.tralwdwd.record.manager.ui.screens.patchopts.PatchOptionsModel

import dev.tralwdwd.record.manager.ui.screens.settings.SettingsModel
import dev.tralwdwd.record.manager.ui.widgets.updater.UpdaterViewModel
import dev.tralwdwd.record.manager.installers.root.RootInstaller
import dev.tralwdwd.record.manager.installers.shizuku.ShizukuInstaller
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.*
import org.koin.dsl.module

class ManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android activities & context
            androidContext(this@ManagerApplication)
            modules(module(createdAtStart = true) {
                singleOf(::ActivityProvider)
            })

            // HTTP
            modules(module {
                single { provideJson() }
                single { provideHttpClient() }
            })

            // Services
            modules(module {
                singleOf(::HttpService)
                singleOf(::AliucordMavenService)
                singleOf(::RNATrackerService)
                singleOf(::ReCordGithubService)
            })

            // UI Models
            modules(module {
                factory { HomeModel(get<Application>(), get<AliucordMavenService>(), get<ReCordGithubService>(), get<RNATrackerService>(), get<kotlinx.serialization.json.Json>(), get<dev.tralwdwd.record.manager.manager.PreferencesManager>()) }

                factoryOf(::AboutModel)
                factoryOf(::PatchingScreenModel)
                factory { SettingsModel(get<Application>(), get<PathManager>(), get<PreferencesManager>(), get<InstallerManager>()) }
                factoryOf(::PatchOptionsModel)
                factoryOf(::IconOptionsModel)
                factoryOf(::LogScreenModel)
                factoryOf(::LogsListScreenModel)
                viewModelOf(::UpdaterViewModel)
            })

            // Managers
            modules(module {
                single { providePreferences() }
                singleOf(::PathManager)
                singleOf(::InstallerManager)
                singleOf(::OverlayManager)
                singleOf(::InstallLogManager)

                singleOf(::AndroidDownloadManager)
                singleOf(::KtorDownloadManager)
            })

            // Installers
            modules(module {
                single { PMInstaller(get()) }
                single { RootInstaller() }
                singleOf(::ShizukuInstaller)
            })
        }

        // Limit parallel fetching of images using Coil
        @OptIn(DelicateCoilApi::class)
        SingletonImageLoader.setUnsafe { context ->
            ImageLoader.Builder(context)
                .fetcherCoroutineContext(Dispatchers.IO.limitedParallelism(5))
                .build()
        }
    }
}
