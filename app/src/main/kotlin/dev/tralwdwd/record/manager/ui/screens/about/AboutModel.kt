package dev.tralwdwd.record.manager.ui.screens.about

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.tralwdwd.record.manager.network.models.Developer
import dev.tralwdwd.record.manager.network.services.HttpService
import dev.tralwdwd.record.manager.ui.util.toUnsafeImmutable
import dev.tralwdwd.record.manager.util.launchIO
import kotlinx.collections.immutable.persistentListOf

class AboutModel(
    private val http: HttpService,
) : StateScreenModel<AboutScreenState>(AboutScreenState.Loading) {
    init {
        fetchDevelopers()
    }

    fun fetchDevelopers() = screenModelScope.launchIO {
        mutableState.value = AboutScreenState.Loading

        val allDevelopers = persistentListOf(
            Developer(
                username = "tralwdwd",
                avatarUrl = "https://github.com/tralwdwd.png",
                commits = 0,
                repositories = emptyList(),
                role = "ReCord - Creator"
            ),
            Developer(
                username = "pylixonly",
                avatarUrl = "https://github.com/pylixonly.png",
                commits = 0,
                repositories = emptyList(),
                role = "Bunny - Creator"
            ),
            Developer(
                username = "maisymoe",
                avatarUrl = "https://github.com/maisymoe.png",
                commits = 0,
                repositories = emptyList(),
                role = "Creator - Vendetta"
            ),
        )

        mutableState.value = AboutScreenState.Loaded(allDevelopers.toUnsafeImmutable())
    }
}
