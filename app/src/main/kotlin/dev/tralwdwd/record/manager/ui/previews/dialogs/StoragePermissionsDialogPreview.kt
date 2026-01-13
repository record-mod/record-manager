package dev.tralwdwd.record.manager.ui.previews.dialogs

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.tralwdwd.record.manager.ui.components.dialogs.ExternalStorageDialog
import dev.tralwdwd.record.manager.ui.components.dialogs.ManageStorageDialog
import dev.tralwdwd.record.manager.ui.theme.ManagerTheme

@Composable
@RequiresApi(Build.VERSION_CODES.R)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun ManageStorageDialogPreview() {
    ManagerTheme {
        ManageStorageDialog(onGranted = {})
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun ExternalStorageDialogPreview() {
    ManagerTheme {
        ExternalStorageDialog(onRequestPermission = {})
    }
}
