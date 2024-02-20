package com.github.y0ung3r.gitglobalhookslocator.git.hooks.exceptions

class UnsupportedHookNameException(fileName: String)
    : Exception("Global hook \"$fileName\" unsupported")