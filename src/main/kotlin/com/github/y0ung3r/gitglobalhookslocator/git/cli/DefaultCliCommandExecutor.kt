package com.github.y0ung3r.gitglobalhookslocator.git.cli

import com.github.y0ung3r.gitglobalhookslocator.git.cli.interfaces.CliCommandExecutor
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class DefaultCliCommandExecutor : CliCommandExecutor {
    private companion object {
        const val TERMINATION_EXIT_CODE = 0
        const val NOT_FOUND_EXIT_CODE = 1
    }

    override fun execute(processBuilder: ProcessBuilder): CliResponse
        = try {
            handle(processBuilder)
        } catch (exception: Exception) {
            when (exception) {
                is IOException, is InterruptedException, is RuntimeException
                    -> NotFoundCliResponse(exception.message)

                else -> throw exception
            }
        }

    private fun handle(processBuilder: ProcessBuilder) : CliResponse {
        val process = processBuilder.start()
        val streamReader = InputStreamReader(process.inputStream, StandardCharsets.UTF_8)
        val bufferedReader = BufferedReader(streamReader)

        val responseBuilder = StringBuilder()
        var line: String? = bufferedReader.readLine()

        while (line != null) {
            responseBuilder.append(line)
            responseBuilder.append(System.lineSeparator())
            line = bufferedReader.readLine()
        }

        val response = responseBuilder.toString()

        return when (process.waitFor()) {
            TERMINATION_EXIT_CODE -> CliResponse(response)
            NOT_FOUND_EXIT_CODE -> NotFoundCliResponse(response)
            else -> EmptyCliResponse()
        }
    }
}