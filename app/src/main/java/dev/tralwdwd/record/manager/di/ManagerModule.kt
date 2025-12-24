package dev.tralwdwd.record.manager.di

import dev.tralwdwd.record.manager.domain.manager.DownloadManager
import dev.tralwdwd.record.manager.domain.manager.InstallManager
import dev.tralwdwd.record.manager.domain.manager.PreferenceManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managerModule = module {
    singleOf(::DownloadManager)
    singleOf(::PreferenceManager)
    singleOf(::InstallManager)
}