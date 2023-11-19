package com.github.y0ung3r.gitglobalhookslocator.git.cli

import com.github.y0ung3r.gitglobalhookslocator.git.cli.interfaces.CliCommandExecutor
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class DefaultCliCommandExecutor : CliCommandExecutor {
    private companion object {
        const val TERMINATION_EXIT_CODE = 0
    }

    override fun execute(processBuilder: ProcessBuilder) : CliResponse {
        val process = processBuilder.start()
        val streamReader = InputStreamReader(process.inputStream, StandardCharsets.UTF_8)
        val bufferedReader = BufferedReader(streamReader)

        val response = StringBuilder()
        var line: String? = bufferedReader.readLine()

        while (line != null) {
            response.append(line)
            response.append(System.lineSeparator())
            line = bufferedReader.readLine()
        }

        return when (process.waitFor()) {
            TERMINATION_EXIT_CODE -> CliResponse(response.toString())
            else -> NotFoundCliResponse(response.toString())
        }
    }
}