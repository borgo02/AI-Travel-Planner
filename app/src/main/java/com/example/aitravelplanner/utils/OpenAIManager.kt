package com.example.aitravelplanner.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class OpenAIManager {

    private var systemPrompt: String = ""
    private val apiKey = "sk-proj-TN1VtdJdvoAktmWubUDPT3BlbkFJz4cu4afqhqZXo7hdg7VJ"

    suspend fun preProcessTravel(travel: String, isAutomaticDestination: Boolean): JSONObject {
        if(isAutomaticDestination)
            systemPrompt = """
                You are a travel planner. The user provides details for a trip:
                1. Source position
                2. Destination
                3. Number of days
                4. Budget
                5. Interests
                6. Cities already visited
                Generate a JSON (in according to the given user's information and preferences) with:
                1. City to visit (if destination is set to 'generate automatic destination' you have to generate an automatic destination)
                2. A brief description of the city and the itinerary
                3. List of places to visit in that city (based on to the number of days the user wants to stay)
                4. A brief description of each place 
                You have to avoid destination in cities already visited. The key of the JSON must be:
                1. "City to visit" for the name of the city destination
                2. "Description" for the city description
                3. "Itinerary" for the itinerary description
                4. "Places to visit" for the list of places to visit in that city
                5. "Place" for the place name
                6. "Description" for the place description
            """.trimIndent()
        else
            systemPrompt = """
                You are a travel planner. The user provides details for a trip:
                1. Source position
                2. Destination
                3. Number of days
                4. Budget
                5. Interests
                6. Cities already visited
                Generate a JSON (in according to the given user's information and preferences) with:
                1. City to visit (is equal to destination the user gave you)
                2. A brief description of the city and the itinerary
                3. List of places to visit in that city (based on the number the user wants to stay)
                4. A brief description of each place 
                The key of the JSON must be:
                1. "City to visit" for the name of the city destination (the same the user gave you)
                2. "Description" for the city description
                3. "Itinerary" for the itinerary description
                4. "Places to visit" for the list of places to visit in that city
                5. "Place" for the place name
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
            put("temperature", 0.8)
            put("top_p", 0.8)
            put("frequency_penalty", 0.5)
            put("presence_penalty", 0.5)
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
                    try {
                        Log.e("OpenAIManager", "${JSONObject(message.getString("content"))}")
                        JSONObject(message.getString("content"))
                    } catch (e: Exception) {
                        Log.e("OpenAIManager", "Error converting String to JSONObject: ${e.message}", e)
                        Log.e("OpenAIManager", "Value ```json: ${message.getString("content")}")
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
