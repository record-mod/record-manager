package dev.tralwdwd.record.manager.di

import dev.tralwdwd.record.manager.ui.viewmodel.home.HomeViewModel
import dev.tralwdwd.record.manager.ui.viewmodel.installer.InstallerViewModel
import dev.tralwdwd.record.manager.ui.viewmodel.installer.LogViewerViewModel
import dev.tralwdwd.record.manager.ui.viewmodel.libraries.LibrariesViewModel
import dev.tralwdwd.record.manager.ui.viewmodel.settings.AdvancedSettingsViewModel
import dev.tralwdwd.record.manager.ui.viewmodel.settings.DeveloperSettingsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {
    factoryOf(::InstallerViewModel)
    factoryOf(::AdvancedSettingsViewModel)
    factoryOf(::DeveloperSettingsViewModel)
    factoryOf(::HomeViewModel)
    factoryOf(::LogViewerViewModel)
    factoryOf(::LibrariesViewModel)
}