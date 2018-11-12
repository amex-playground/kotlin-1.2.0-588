package com.danger.dangerkt.lib

import com.google.gson.Gson
import java.io.File

private class DangerRunner(jsonInputFilePath: FilePath, jsonOutputPath: FilePath) {
    val jsonOutputFile: File = File(jsonOutputPath)
    val danger: DangerDSL
    val dangerResults: DangerResults = DangerResults(arrayOf(), arrayOf(),arrayOf(), arrayOf())

    private val gson = Gson()

    init {
        this.danger = gson.fromJson(jsonInputFilePath.readText(), DSL::class.java).danger
        saveDangerResults()
    }

    fun saveDangerResults() {
        val resultsJSON = gson.toJson(dangerResults)
        jsonOutputFile.writeText(resultsJSON)
    }
}

private lateinit var dangerRunner: DangerRunner

fun Danger(args: Array<String>): DangerDSL {
    val argsCount = args.count()

    val jsonInputFilePath = args[argsCount - 2]
    val jsonOutputPath = args[argsCount - 1]

    dangerRunner = DangerRunner(jsonInputFilePath, jsonOutputPath)
    return dangerRunner.danger
}

fun fail(message: String) {
    fail(Violation(message))
}

fun fail(message: String, file: FilePath, line: Int) {
    fail(Violation(message, file, line))
}

private fun fail(violation: Violation) {
    dangerRunner.dangerResults.fails+=violation
    dangerRunner.saveDangerResults()
}

fun warning(message: String) {
    warning(Violation(message))
}

fun warning(message: String, file: FilePath, line: Int) {
    warning(Violation(message, file, line))
}

private fun warning(violation: Violation) {
    dangerRunner.dangerResults.warnings+=(violation)
    dangerRunner.saveDangerResults()
}

fun message(message: String) {
    message(Violation(message))
}

fun message(message: String, file: FilePath, line: Int) {
    message(Violation(message, file, line))
}

private fun message(violation: Violation) {
    dangerRunner.dangerResults.messages+=violation
    dangerRunner.saveDangerResults()
}

fun markdown(message: String) {
    markdown(Violation(message))
}

fun markdown(message: String, file: FilePath, line: Int) {
    markdown(Violation(message, file, line))
}

private fun markdown(violation: Violation) {
    dangerRunner.dangerResults.markdowns+=violation
    dangerRunner.saveDangerResults()
}

private fun FilePath.readText() = File(this).readText()
