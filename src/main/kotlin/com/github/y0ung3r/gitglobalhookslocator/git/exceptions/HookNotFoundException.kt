package com.github.y0ung3r.gitglobalhookslocator.git.exceptions

class HookNotFoundException(fileName: String)
    : Exception("Global hook \"$fileName\" not found")