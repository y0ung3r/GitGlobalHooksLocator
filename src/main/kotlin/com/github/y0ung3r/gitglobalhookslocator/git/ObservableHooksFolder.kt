package com.github.y0ung3r.gitglobalhookslocator.git

import com.github.y0ung3r.gitglobalhookslocator.git.extensions.getGlobalHooksPath
import com.intellij.openapi.diagnostic.thisLogger
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.*
import kotlin.io.path.nameWithoutExtension

class ObservableHooksFolder(git: Git) {
	companion object {
		@JvmStatic
		val supportedHooks = arrayOf(
			"pre-commit",
			"prepare-commit-msg",
			"commit-msg",
			"post-commit",
			"applypatch-msg",
			"pre-applypatch",
			"post-applypatch",
			"pre-rebase",
			"post-rewrite",
			"post-checkout",
			"post-merge",
			"pre-push",
			"pre-auto-gc"
		)
	}

	private var count: Int = 0
	val hooks: PublishSubject<HookEvent>
	val path: Path

	init {
		path = git.getGlobalHooksPath()

		val files = try {
			Files.list(path)
		}
		catch (exception: NoSuchFileException) {
			thisLogger()
				.info("Provided hooks path is not valid", exception)

			emptyList<Path>()
				.stream()
		}

		hooks = PublishSubject.create()

		files
			.filter {
				supportedHooks.any { hookName -> it.fileName.nameWithoutExtension.contains(hookName) }
			}
			.map { HookEvent(StandardWatchEventKinds.ENTRY_CREATE, HookEntry.load(it)) }
			.forEach { hooks.onNext(it) }

		val watchService = FileSystems.getDefault().newWatchService()
		Files.walkFileTree(path, HooksFolderVisitor(watchService))

		val changesTrackingJob = CoroutineScope(Dispatchers.IO).launch {
			while (true) {
				val folderEntry = watchService.take()

				folderEntry.pollEvents().forEach {
					val hookPath = Path.of(path.toString(), it.context().toString())
					val hookEntry = HookEntry.load(hookPath)
					hooks.onNext(HookEvent(it.kind() as WatchEvent.Kind<Path>, hookEntry))
				}
			}
		}
	}
}
