package dev.tralwdwd.record.manager.manager

import android.content.SharedPreferences
import androidx.compose.runtime.Stable
import dev.tralwdwd.record.manager.manager.base.BasePreferenceManager
import dev.tralwdwd.record.manager.ui.theme.Theme

@Stable
class PreferencesManager(preferences: SharedPreferences) : BasePreferenceManager(preferences) {
    var theme by enumPreference("theme", Theme.System)
    var dynamicColor by booleanPreference("dynamic_color", true)
    var devMode by booleanPreference("dev_mode", false)
    var installer by enumPreference<InstallerSetting>("installer", InstallerSetting.PM)
    var keepPatchedApks by booleanPreference("keep_patched_apks", false)
    var showNetworkWarning by booleanPreference("show_network_warning", true)
    var showPlayProtectWarning by booleanPreference("show_play_protect_warning", true)
    var rootPopupShown by booleanPreference("root_popup_shown", false)
}
