package com.github.y0ung3r.gitglobalhookslocator.ui

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notifier {
	private const val GROUP_ID = "GitGlobalHooksLocator"

	@JvmStatic
	fun error(project: Project, title: String, content: String)
		= notify(project, title, content, NotificationType.ERROR)

	@JvmStatic
	fun warning(project: Project, title: String, content: String)
		= notify(project, title, content, NotificationType.WARNING)

	@JvmStatic
	fun info(project: Project, title: String, content: String)
		= notify(project, title, content, NotificationType.INFORMATION)

	@JvmStatic
	private fun notify(project: Project, title: String, content: String, type: NotificationType)
		= NotificationGroupManager
			.getInstance()
			.getNotificationGroup(GROUP_ID)
			.createNotification(title, content, type)
			.notify(project)
}