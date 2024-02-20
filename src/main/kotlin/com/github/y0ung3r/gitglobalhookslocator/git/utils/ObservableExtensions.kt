package com.github.y0ung3r.gitglobalhookslocator.git.utils

import com.intellij.ui.layout.ComponentPredicate
import io.reactivex.rxjava3.core.Observable

fun Observable<Boolean>.toComponentPredicate(): ComponentPredicate
	= object : ComponentPredicate() {
		override fun addListener(listener: (Boolean) -> Unit) {
			subscribe(listener)
		}

		override fun invoke(): Boolean
			= false
	}

fun Observable<Boolean>.negate()
	= map { !it }