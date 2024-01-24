package com.github.y0ung3r.gitglobalhookslocator.git

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

class HooksFolderVisitor(private val watchService: WatchService) : SimpleFileVisitor<Path>() {
	override fun preVisitDirectory(directory: Path, attributes: BasicFileAttributes): FileVisitResult {
		directory.register(
			watchService,
			StandardWatchEventKinds.ENTRY_CREATE,
			StandardWatchEventKinds.ENTRY_DELETE)

		return FileVisitResult.CONTINUE
	}
}