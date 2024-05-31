package com.example.aitravelplanner.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class OpenAIManager {

    private val apiKey = "sk-proj-TN1VtdJdvoAktmWubUDPT3BlbkFJz4cu4afqhqZXo7hdg7VJ"

    suspend fun preProcessTravel(travel: String): JSONObject {
        val systemPrompt = """
            You are a travel planner. The user provides details for a trip:
            1. Source position
            2. Destination
            3. Number of days
            4. Budget
            5. Interests
            6. Cities already visited
            Generate a JSON (in according to the given user's information and preferences) with:
            1. City to visit (if destination is set to 'generate automatic destination')
            2. A brief description of the city and the itinerary
            3. List of places to visit in that city
            4. A brief description of each place 
            Avoid destination in cities already visited and the key of the JSON must be:
            1. "City to visit" for the city destination 
            2. "Description" for the city description
            3. "Itinerary" for the description of the itinerary
            4. "Places to visit" for the array of places to visit
            5. "Name" for the name of the place
            6. "Description" for the place description
        """.trimIndent()

        val url = URL("https://api.openai.com/v1/chat/completions")
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer $apiKey"
        )

        val messages = JSONArray().apply {
            put(JSONObject().put("role", "system").put("content", systemPrompt))
            put(JSONObject().put("role", "user").put("content", travel))
        }

        val body = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", messages)
        }.toString()

        return try {
            val response = postRequest(url, headers, body)
            response?.let {
                val jsonResponse = JSONObject(it)
                if (jsonResponse.has("error")) {
                    Log.e("OpenAIManager", "Error: ${jsonResponse.getJSONObject("error").getString("message")}")
                    JSONObject().put("error", jsonResponse.getJSONObject("error").getString("message"))
                } else {
                    val choices = jsonResponse.getJSONArray("choices")
                    val message = choices.getJSONObject(0).getJSONObject("message")
                    Log.e("message", message.getString("content"))
                    try {
                        JSONObject(message.getString("content"))
                    }
                    catch(e: Exception){
                        JSONObject().put("error", e.message)
                    }
                }
            } ?: JSONObject().put("error", "No response from server")
        } catch (e: Exception) {
            Log.e("OpenAIManager", "Error processing travel: ${e.message}", e)
            JSONObject().put("error", e.message)
        }
    }

    private suspend fun postRequest(url: URL, headers: Map<String, String>, body: String): String? {
        return withContext(Dispatchers.IO) {
            val connection = url.openConnection() as HttpURLConnection
            try {
                headers.forEach { (key, value) -> connection.setRequestProperty(key, value) }
                connection.requestMethod = "POST"
                connection.doOutput = true

                connection.outputStream.use { it.write(body.toByteArray()) }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.use { it.reader().readText() }
                } else {
                    connection.errorStream?.use { it.reader().readText() }
                }
            } catch (e: Exception) {
                Log.e("OpenAIManager", "Error in postRequest: ${e.message}", e)
                null
            } finally {
                connection.disconnect()
            }
        }
    }
}