package com.example.aitravelplanner.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class OpenAIManager {

    private val apiKey = ""
    suspend fun preProcessTravel(travel: String): String {
        val systemPrompt = "You are a travel planner. The user provides a map with:\n" +
                "1. Source position\n" +
                "2. Destination\n" +
                "3. Number of days for the trip\n" +
                "4. Whether they want to stay in a hotel\n" +
                "5. Budget\n" +
                "6. Interests of the user\n" +
                "\n" +
                "Generate a travel itinerary in JSON format including the stages with a little description."

        val url = URL("https://api.openai.com/v1/chat/completions")
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer $apiKey"
        )
        val body = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", listOf(
                JSONObject().put("role", "system").put("content", systemPrompt),
                JSONObject().put("role", "user").put("content", travel)
            ))
        }.toString()

        return try {
            val response = postRequest(url, headers, body)
            val jsonResponse = JSONObject(response)
            val choices = jsonResponse.getJSONArray("choices")
            val message = choices.getJSONObject(0).getJSONObject("message")
            val text = message.getString("content")
            val regex = Regex("```([^\n]*\n)?([^`]*)```")
            val matches = regex.findAll(text)
            val codeBlocks = matches.map { it.groupValues[2] }.toList()
            try {
                sanitizeCode(codeBlocks.joinToString("\n"))
            } catch (e: Exception) {
                travel
            }
        } catch (e: Exception) {
            travel
        }
    }

    private fun sanitizeCode(code: String): String {
        var res = code.replace("\\n", "\n")
        res = res.replace("\\'", "'")
        res = res.replace("\\\"", "\"")
        return res
    }

    private suspend fun postRequest(url: URL, headers: Map<String, String>, body: String): String {
        return withContext(Dispatchers.IO) {
            val connection = url.openConnection() as HttpURLConnection
            headers.forEach { (key, value) -> connection.setRequestProperty(key, value) }
            connection.requestMethod = "POST"
            connection.doOutput = true

            connection.outputStream.use { it.write(body.toByteArray()) }

            connection.inputStream.use { it.reader().readText() }
        }
    }
}
