package dev.tralwdwd.record.manager.di

import dev.tralwdwd.record.manager.domain.repository.RestRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::RestRepository)
}