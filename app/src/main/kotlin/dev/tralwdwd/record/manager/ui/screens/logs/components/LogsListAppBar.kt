package dev.tralwdwd.record.manager.ui.screens.logs.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.tralwdwd.record.manager.ui.components.BackButton
import dev.tralwdwd.record.manager.ui.screens.settings.SettingsScreen
import dev.tralwdwd.record.manager.R

@Composable
fun LogsListAppBar(
    onDeleteLogs: () -> Unit,
) {
    TopAppBar(
        navigationIcon = { BackButton() },
        title = { Text(stringResource(R.string.logs_title)) },
        actions = {
            val navigator = LocalNavigator.current

            IconButton(onClick = onDeleteLogs) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete_forever),
                    contentDescription = stringResource(R.string.logs_action_delete_all)
                )
            }

            IconButton(onClick = { navigator?.push(SettingsScreen()) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.navigation_settings)
                )
            }
        }
    )
}
