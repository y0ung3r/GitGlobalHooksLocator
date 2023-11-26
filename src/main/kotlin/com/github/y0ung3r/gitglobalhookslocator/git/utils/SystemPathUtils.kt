package com.github.y0ung3r.gitglobalhookslocator.git.utils

object SystemPathUtils {
	private const val ROOT_KEY = "SystemDrive"
	private const val USER_HOME_KEY = "user.home"
	private const val USER_DIR_KEY = "user.dir"

	@JvmStatic
	private fun wrapWithSlash(path: String): String
		= "$path/"

	@JvmStatic
	private fun getSystemPath(key: String): String {
		val systemPath = System
			.getProperty(key)
			.replace("\\", "/") // The replacement eliminates the need to escape the string

		return wrapWithSlash(systemPath)
	}

	@JvmStatic
	fun getUserHomePath(): String
		= getSystemPath(USER_HOME_KEY)

	@JvmStatic
	fun getCurrentDirectoryPath(): String
		= getSystemPath(USER_DIR_KEY)

	@JvmStatic
	fun getRootPath(): String
		= wrapWithSlash(System.getenv(ROOT_KEY))
}
