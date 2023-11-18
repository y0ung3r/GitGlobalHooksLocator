package com.github.y0ung3r.gitglobalhookslocator.services.git.exceptions

import com.github.y0ung3r.gitglobalhookslocator.services.git.Git

class GitVersionIsNotSupportedException
    : Exception("The plugin requires Git version above ${Git.minRequiredVersion}")