package com.github.y0ung3r.gitglobalhookslocator.git.exceptions

import com.github.y0ung3r.gitglobalhookslocator.git.Git

class GitVersionIsNotSupportedException
    : Exception("The plugin requires Git version above ${Git.minRequiredVersion}")