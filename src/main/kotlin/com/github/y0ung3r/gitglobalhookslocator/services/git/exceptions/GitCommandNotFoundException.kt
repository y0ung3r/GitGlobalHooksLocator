package com.github.y0ung3r.gitglobalhookslocator.services.git.exceptions

class GitCommandNotFoundException(vararg params: String)
    : Exception("Native Git command \"git ${params.joinToString(" ")}\" not found")