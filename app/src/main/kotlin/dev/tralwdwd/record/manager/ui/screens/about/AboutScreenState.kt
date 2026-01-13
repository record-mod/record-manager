package dev.tralwdwd.record.manager.ui.screens.about

import dev.tralwdwd.record.manager.network.models.Developer
import kotlinx.collections.immutable.ImmutableList

sealed interface AboutScreenState {
    data object Loading : AboutScreenState
    data object Failure : AboutScreenState
    data class Loaded(val contributors: ImmutableList<Developer>) : AboutScreenState
}
