package com.github.y0ung3r.gitglobalhookslocator.git.hooks

import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.extensions.getGlobalHooksPath
import com.intellij.openapi.diagnostic.thisLogger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import java.nio.file.*
import java.util.stream.Collectors

class HooksFolder(git: Git) {
	private data class HooksFolderEvent(val kind: WatchEvent.Kind<Path>, private val sourcePath: Path, private val folderPath: Path) {
		val hookPath: Path = Path.of(folderPath.toString(), sourcePath.toString())
	}

	val path: Path
		= git.getGlobalHooksPath()

	private val cachedHooks: MutableList<HookEntry>
		= getExistsHooks()

	val addedHooks: Observable<HookEntry>
		= observeFolder(StandardWatchEventKinds.ENTRY_CREATE)
			.map { HookEntry.load(it.hookPath) }
			.doOnNext {
				if (cachedHooks.add(it)) {
					thisLogger()
						.info("Hook ${it.name.value} was added")
				}
			}
			.startWithIterable(cachedHooks)
			.subscribeOn(Schedulers.io())

	val deletedHooks: Observable<HookName>
		= observeFolder(StandardWatchEventKinds.ENTRY_DELETE)
			.map { HookName.create(it.hookPath) }
			.doOnNext { hookName ->
				val index = cachedHooks.indexOfFirst { it.name == hookName }

				if (index != -1) {
					cachedHooks.removeAt(index)

					thisLogger()
						.info("Hook ${hookName.value} was deleted")
				}
			}
			.subscribeOn(Schedulers.io())

	val renamedHooks: Observable<HookEntry>
		= observeFolder(StandardWatchEventKinds.ENTRY_MODIFY)
			.map { HookEntry.load(it.hookPath) }
			.subscribeOn(Schedulers.io())

	fun isEmpty(): Boolean
		= cachedHooks.isEmpty()

	fun count(): Int
		= cachedHooks
			.count()

	fun isAllDisabled(): Boolean
		= cachedHooks
			.all { it.isDisabled() }

	fun enableAll(): Unit
		= cachedHooks
			.forEach { it.enable() }

	fun disableAll(): Unit
		= cachedHooks
			.forEach { it.disable() }

	fun canBeUsed(hookName: HookName): Boolean
		= cachedHooks.count { hookName == it.name } <= 1

	private fun observeFolder(kind: WatchEvent.Kind<Path>): Observable<HooksFolderEvent> {
		val watchService = FileSystems.getDefault().newWatchService()
		path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE)

		return Observable.create<HooksFolderEvent> { emitter ->
			while (true) {
				val folderEntry = try {
					watchService.take()
				} catch (exception: ClosedWatchServiceException) {
					return@create emitter.onComplete()
				}

				if (!folderEntry.isValid) {
					return@create emitter.onComplete()
				}

				emitFolderEvent(emitter, folderEntry, kind)

				if (!folderEntry.reset()) {
					return@create emitter.onComplete()
				}
			}
		}
		.doFinally {
			watchService.close()

			thisLogger()
				.info("Watch service was closed")
		}
		.onErrorResumeNext {
			thisLogger()
				.error("An error occurred while watching the hooks folder", it)

			observeFolder(kind)
		}
	}

	private fun emitFolderEvent(
		emitter: ObservableEmitter<HooksFolderEvent>,
		folderEntry: WatchKey,
		observableKind: WatchEvent.Kind<Path>) {
		val pendingEvents = folderEntry
			.pollEvents()
			.map {
				@Suppress("UNCHECKED_CAST")
				it as WatchEvent<Path>
			}

		val targetKind = when {
			pendingEvents.size > 1 -> StandardWatchEventKinds.ENTRY_CREATE
			else -> observableKind
		}

		val originalEvent = pendingEvents
			.firstOrNull { it.kind() == targetKind }
				?: return

		val folderEvent = HooksFolderEvent(
			observableKind,
			originalEvent.context(),
			path)

		if (HookName.isSupportedHook(folderEvent.hookPath)) {
			emitter.onNext(folderEvent)
		}
	}

	private fun getExistsHooks(): MutableList<HookEntry>
		= try {
			Files.list(path)
				.filter { HookName.isSupportedHook(it) }
				.map { HookEntry.load(it) }
				.collect(Collectors.toList())
		}
		catch (exception: NoSuchFileException) {
			thisLogger()
				.info("Provided hooks path is not valid", exception)

			mutableListOf()
		}
}
