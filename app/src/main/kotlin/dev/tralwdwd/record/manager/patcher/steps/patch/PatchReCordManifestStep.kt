package dev.tralwdwd.record.manager.patcher.steps.patch

import dev.tralwdwd.record.manager.patcher.util.ManifestPatcher
import dev.tralwdwd.record.manager.ui.screens.patchopts.PatchOptions

class PatchReCordManifestStep(val options: PatchOptions) : PatchManifestStep(options) {
    override fun patchManifest(manifestBytes: ByteArray, isBase: Boolean) = ManifestPatcher.patchManifest(
        manifestBytes = manifestBytes,
        packageName = options.packageName,
        compileSdkVersion = null,
        compileSdkVersionCodename = null,
        targetSdkVersion = null,
        useEmbeddedDex = false,
        requestLegacyExternalStorage = false,
        appName = if (isBase) options.appName else null,
        debuggable = options.debuggable,
    )
}
