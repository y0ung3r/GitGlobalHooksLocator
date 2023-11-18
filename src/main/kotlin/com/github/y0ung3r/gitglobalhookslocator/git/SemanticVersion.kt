package com.github.y0ung3r.gitglobalhookslocator.git

import ai.grazie.utils.findAllMatches
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.ProvidedSemanticVersionIsInvalidException

class SemanticVersion(val major: Int, val minor: Int, val patch: Int) : Comparable<SemanticVersion> {
    companion object {
        private const val COMPONENTS_DELIMITER = '.'
        private const val VERSION_PATTERN = "(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)?"

        @JvmStatic
        fun parse(version: String): SemanticVersion {
            val match = Regex(VERSION_PATTERN).findAllMatches(version).firstOrNull()
                ?: throw ProvidedSemanticVersionIsInvalidException(version)

            return match.value.split(COMPONENTS_DELIMITER).let {
                SemanticVersion(it[0].toInt(), it[1].toInt(), it[2].toInt())
            }
        }
    }

    override fun compareTo(other: SemanticVersion): Int =
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
        val version = other as? SemanticVersion

        return when {
            version == null -> false
            compareTo(version) == 0 -> true
            else -> false
        }
    }

    override fun hashCode(): Int
        = major.hashCode() * 31 + minor.hashCode() * 31 + patch.hashCode()

    override fun toString(): String
        = "$major.$minor.$patch"
}