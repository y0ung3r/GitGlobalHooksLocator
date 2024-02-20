package com.github.y0ung3r.gitglobalhookslocator.git.hooks

import com.github.y0ung3r.gitglobalhookslocator.git.hooks.exceptions.HookNotFoundException
import java.io.File
import java.nio.file.Path

class HookEntry private constructor(private var file: File) {
    companion object {
        @JvmStatic
        fun load(filePath: Path): HookEntry {
            val file = filePath.toFile()

            if (!file.exists()) {
                throw HookNotFoundException(file.nameWithoutExtension)
            }

            return HookEntry(file)
        }
    }

    val name: HookName
        = HookName.create(file)

    fun isDisabled()
        = HookName
            .supportedHooks
            .all { it != file.nameWithoutExtension }

    fun enable() {
        if (!isDisabled()) {
            return
        }

        renameFile(name.value)
    }

    fun disable() {
        if (isDisabled()) {
            return
        }

        renameFile("_${file.name}")
    }

    private fun renameFile(newName: String) {
        file = Path
            .of(file.parent, newName)
            .toFile()
            .apply {
                file.renameTo(this)
            }
    }
}