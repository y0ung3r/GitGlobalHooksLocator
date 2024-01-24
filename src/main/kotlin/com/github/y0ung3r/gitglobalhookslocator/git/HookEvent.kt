package com.github.y0ung3r.gitglobalhookslocator.git

import java.nio.file.Path
import java.nio.file.WatchEvent

data class HookEvent(
	val kind: WatchEvent.Kind<Path>,
	val entry: HookEntry)