package com.github.y0ung3r.gitglobalhookslocator.git.exceptions

class ProvidedGitVersionIsInvalidException(version: String)
    : Exception("The provided version does not conform to git versioning: $version")