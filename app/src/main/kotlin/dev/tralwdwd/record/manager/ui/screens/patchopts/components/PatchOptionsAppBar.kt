package dev.tralwdwd.record.manager.ui.screens.patchopts.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.tralwdwd.record.manager.ui.components.BackButton
import dev.tralwdwd.record.manager.ui.screens.settings.SettingsScreen
import dev.tralwdwd.record.manager.R

@Composable
fun PatchOptionsAppBar(
    isUpdate: Boolean = false,
) {
    TopAppBar(
        navigationIcon = { BackButton() },
        title = { Text(stringResource(if (!isUpdate) R.string.action_add_install else R.string.action_update_install)) },
        actions = {
            val navigator = LocalNavigator.current

            IconButton(onClick = { navigator?.push(SettingsScreen()) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.navigation_settings)
                )
            }
        }
    )
}
