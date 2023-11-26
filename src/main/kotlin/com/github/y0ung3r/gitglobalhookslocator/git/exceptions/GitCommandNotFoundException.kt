package com.github.y0ung3r.gitglobalhookslocator.git.exceptions

class GitCommandNotFoundException(details: String, vararg params: String)
    : Exception("Native Git command \"git ${params.joinToString(" ")}\" not found ${mapDetails(details)}")
{
    private companion object {
        fun mapDetails(details: String): String
            = when (details) {
                "" -> details
                else -> "($details)"
            }
    }
}