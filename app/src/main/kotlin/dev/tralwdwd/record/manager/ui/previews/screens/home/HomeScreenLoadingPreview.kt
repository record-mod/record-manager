package dev.tralwdwd.record.manager.ui.previews.screens.home

import android.content.res.Configuration
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.tralwdwd.record.manager.ui.screens.home.HomeScreenLoadingContent
import dev.tralwdwd.record.manager.ui.screens.home.components.HomeAppBar
import dev.tralwdwd.record.manager.ui.theme.ManagerTheme

// This preview cannot be properly viewed from an IDE preview

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun HomeScreenLoadingPreview() {
    ManagerTheme {
        Scaffold(
            topBar = { HomeAppBar() },
        ) { padding ->
            HomeScreenLoadingContent(padding = padding)
        }
    }
}
