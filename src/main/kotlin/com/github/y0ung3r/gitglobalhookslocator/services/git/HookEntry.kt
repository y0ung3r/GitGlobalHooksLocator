package com.github.y0ung3r.gitglobalhookslocator.services.git

import com.github.y0ung3r.gitglobalhookslocator.services.git.exceptions.HookNotFoundException
import java.io.File
import java.nio.file.Path

class HookEntry(private var file: File) {
    companion object {
        @JvmStatic
        fun load(filePath: Path): HookEntry {
            val file = filePath.toFile()

            if (!file.exists()) {
                throw HookNotFoundException(file.name.toString())
            }

            return HookEntry(file)
        }
    }

    fun isDisabled()
        = HooksFolder
            .availableHooks
            .all { it != file.nameWithoutExtension }

    fun enable() {
        if (!isDisabled()) {
            return
        }

        renameFile(HooksFolder.availableHooks.first { file.nameWithoutExtension.contains(it) })
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
            .let {
                file.renameTo(it)
                it
            }
    }
}