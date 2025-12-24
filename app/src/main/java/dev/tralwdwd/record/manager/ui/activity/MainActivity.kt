package dev.tralwdwd.record.manager.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.tralwdwd.record.manager.ui.screen.home.HomeScreen
import dev.tralwdwd.record.manager.ui.screen.installer.InstallerScreen
import dev.tralwdwd.record.manager.ui.theme.ReCordManagerTheme
import dev.tralwdwd.record.manager.utils.DiscordVersion
import dev.tralwdwd.record.manager.utils.Intents
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val version = intent.getStringExtra(Intents.Extras.VERSION)

        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf("android.permission.POST_NOTIFICATIONS"),
                0
            )
        }

        val screen = if (intent.action == Intents.Actions.INSTALL && version != null) {
            InstallerScreen(DiscordVersion.fromVersionCode(version)!!)
        } else {
            HomeScreen()
        }

        setContent {
            ReCordManagerTheme {
                Navigator(screen) {
                    SlideTransition(it)
                }
            }
        }
    }
}
