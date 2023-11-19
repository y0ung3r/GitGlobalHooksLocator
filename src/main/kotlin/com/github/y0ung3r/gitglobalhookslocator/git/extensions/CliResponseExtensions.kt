package com.github.y0ung3r.gitglobalhookslocator.git.extensions

import com.github.y0ung3r.gitglobalhookslocator.git.GitResponse
import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse

fun CliResponse.toGitResponse(): GitResponse
    = GitResponse(this)