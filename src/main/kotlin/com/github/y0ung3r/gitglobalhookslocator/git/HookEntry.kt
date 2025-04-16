package com.github.y0ung3r.gitglobalhookslocator.git

import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.HookNotFoundException
import java.io.File
import java.nio.file.Path

class HookEntry(private var file: File) {
    companion object {
        fun load(filePath: Path): HookEntry {
            val file = filePath.toFile()

            if (!file.exists()) {
                throw HookNotFoundException(file.name.toString())
            }

            return HookEntry(file)
        }
    }

    val name: String
        = HooksFolder
            .supportedHooks
            .first { file.nameWithoutExtension.contains(it) }

    fun isDisabled()
        = HooksFolder
            .supportedHooks
            .all { it != file.nameWithoutExtension }

    fun enable() {
        if (!isDisabled()) {
            return
        }

        renameFile(name)
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