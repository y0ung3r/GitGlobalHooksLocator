package com.github.y0ung3r.gitglobalhookslocator.services.git.exceptions

class ProvidedSemanticVersionIsInvalidException(version: String)
    : Exception("The provided version does not conform to semantic versioning: $version")