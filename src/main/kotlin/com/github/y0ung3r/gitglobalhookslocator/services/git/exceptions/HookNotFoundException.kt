package com.github.y0ung3r.gitglobalhookslocator.services.git.exceptions

class HookNotFoundException(fileName: String)
    : Exception("Global hook \"$fileName\" not found")