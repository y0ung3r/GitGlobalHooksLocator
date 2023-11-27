package com.github.y0ung3r.gitglobalhookslocator.git

import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.ProvidedGitVersionIsInvalidException

class GitVersion(val major: Int, val minor: Int, val patch: Int) : Comparable<GitVersion> {
    companion object {
        /*
         * From git sources:
         * The format of this string should be kept stable for compatibility
         * with external projects that rely on the output of "git version"
         * https://github.com/git/git/blob/564d0252ca632e0264ed670534a51d18a689ef5d/help.c#L741
         */
        private const val PREFIX = "git version"
        private const val VERSION_PATTERN = "^$PREFIX (0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)?"

        fun parse(version: String) : GitVersion {
            val match = Regex(VERSION_PATTERN).findAll(version).firstOrNull()
                ?: throw ProvidedGitVersionIsInvalidException(version)

            return try {
                match.groupValues.let {
                    GitVersion(it[1].toInt(), it[2].toInt(), it[3].toInt())
                }
            } catch (exception: NumberFormatException) {
                throw ProvidedGitVersionIsInvalidException(version)
            }
        }
    }

    override fun compareTo(other: GitVersion): Int =
        when {
            major > other.major -> 1
            major < other.major -> -1
            minor > other.minor -> 1
            minor < other.minor -> -1
            patch > other.patch -> 1
            patch < other.patch -> -1
            else -> 0
        }

    override fun equals(other: Any?): Boolean {
        val version = other as? GitVersion

        return when {
            version == null -> false
            compareTo(version) == 0 -> true
            else -> false
        }
    }

    override fun hashCode(): Int
        = major.hashCode() * 31 + minor.hashCode() * 31 + patch.hashCode()

    override fun toString(): String
        = "$PREFIX $major.$minor.$patch"
}