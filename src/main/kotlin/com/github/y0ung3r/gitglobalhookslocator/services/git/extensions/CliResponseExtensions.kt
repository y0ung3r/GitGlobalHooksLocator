package com.github.y0ung3r.gitglobalhookslocator.services.git.extensions

import com.github.y0ung3r.gitglobalhookslocator.services.git.GitResponse
import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.CliResponse

fun CliResponse.toGitResponse(): GitResponse
    = GitResponse(this)