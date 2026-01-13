package dev.tralwdwd.record.manager.di

import android.content.Context
import dev.tralwdwd.record.manager.manager.PreferencesManager
import org.koin.core.scope.Scope

fun Scope.providePreferences(): PreferencesManager {
    val ctx: Context = get()
    return PreferencesManager(ctx.getSharedPreferences("preferences", Context.MODE_PRIVATE))
}
