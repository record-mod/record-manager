package dev.tralwdwd.record.manager.patcher

import dev.tralwdwd.record.manager.network.utils.SemVer
import dev.tralwdwd.record.manager.ui.screens.patchopts.PatchOptions
import kotlinx.serialization.Serializable

/**
 * Data stored inside patched APKs as "record.json" in order to preserve install-time information about Record and the Manager.
 */
@Serializable
data class InstallMetadata(
    /**
     * The user-selected options for this installation.
     */
    val options: PatchOptions,

    /**
     * Whether the manager is a real release or built from source.
     */
    val customManager: Boolean,

    /**
     * The semver version of this manager that performed the installation.
     */
    val managerVersion: SemVer,

    /**
     * Version of LSPatch used to inject Xposed module into the APK.
     */
    val lspatchVersion: Int?,

    /**
     * Version of ReCordXposed embedded into the APK.
     */
    val recordXposedVersion: SemVer?,
)
