package com.example.aitravelplanner.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ImagesManager {
    suspend fun getImages(places: ArrayList<String>): ArrayList<String> {
        val urlImages: ArrayList<String> = arrayListOf()
        for (place in places) {
            urlImages.add(searchImage(place.toString().replace(" ",  "").replace("-", "")))
        }
        return urlImages
    }

    private suspend fun searchImage(query: String): String = withContext(Dispatchers.IO) {
        val apiKey = "AIzaSyCyKXYJ-owDTDlkYatWmAjqyDH4H-osRV0"
        val cx = "92ac60da16a974572"
        val encodedQuery = URLEncoder.encode(query, "UTF-8")
        val urlString = "https://www.googleapis.com/customsearch/v1?q=$encodedQuery&cx=$cx&searchType=image&key=$apiKey"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                    val response = reader.readText()
                    try {
                        val jsonObject = JSONObject(response)
                        Log.e("ImagesManager", jsonObject.toString())
                        if (jsonObject.has("items")) {
                            val itemsArray = jsonObject.getJSONArray("items")
                            if (itemsArray.length() > 0) {
                                val item = itemsArray.getJSONObject(0)
                                item.getString("link")
                            } else {
                                ""
                            }
                        } else {
                            Log.e("ImagesManager", "No items found in the response.")
                            ""
                        }
                    } catch (e: Exception) {
                        Log.e("ImagesManager", "Error: ${e.message}", e)
                        ""
                    }
                }
            } else {
                Log.e("ImagesManager", "HTTP error code: $responseCode")
                ""
            }
        } catch (e: Exception) {
            Log.e("ImagesManager", "Error: ${e.message}", e)
            ""
        } finally {
            connection.disconnect()
        }
    }
}
