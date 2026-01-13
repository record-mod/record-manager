package dev.tralwdwd.record.manager.ui.previews.screens.logs

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import dev.tralwdwd.record.manager.ui.screens.logs.LogsScreenContent
import dev.tralwdwd.record.manager.ui.theme.ManagerTheme

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun LogsListScreenNonePreview() {
    ManagerTheme {
        LogsScreenContent(
            logs = remember { mutableStateListOf() },
            onOpenLog = {},
            onDeleteLogs = {},
        )
    }
}
