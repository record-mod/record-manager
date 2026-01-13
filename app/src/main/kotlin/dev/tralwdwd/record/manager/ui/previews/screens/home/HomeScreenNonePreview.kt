package dev.tralwdwd.record.manager.ui.previews.screens.home

import android.content.res.Configuration
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.tralwdwd.record.manager.ui.screens.home.HomeScreenNoneContent
import dev.tralwdwd.record.manager.ui.screens.home.components.HomeAppBar
import dev.tralwdwd.record.manager.ui.theme.ManagerTheme

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun HomeScreenNonePreview() {
    ManagerTheme {
        Scaffold(
            topBar = { HomeAppBar() },
        ) { padding ->
            HomeScreenNoneContent(
                padding = padding,
                onClickInstall = {},
            )
        }
    }
}
