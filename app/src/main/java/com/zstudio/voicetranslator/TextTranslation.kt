package com.zstudio.voicetranslator

import android.app.Activity
import android.util.Log
import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject


class TextTranslation(private val acitivity: Activity) {
    private var translatedText: String = ""
    fun textToText(text: String, language: String, callback: (String) -> Unit){

        val client = OkHttpClient()

        val jobTitlesMap = mapOf(
            "English" to arrayOf("en", "en-US_Joey"),
            "Italian" to arrayOf("it", "it-IT_Carla"),
            "German" to arrayOf("de", "de-DE_Hans"),
            "Hindi" to arrayOf("hi", "hi-IN_Aditi"),
            "Spanish" to arrayOf("es", "es-ES_Enrique"),
            "French" to arrayOf("fr", "fr-FR_Mathieu"),
            "Japanese" to arrayOf("ja", "ja-JP_Mizuki")
        )

        val languageCode = jobTitlesMap[language]?.get(0)
        val voiceCode = jobTitlesMap[language]?.get(1)

        val mediaType =
            "multipart/form-data; boundary=---011000010111000001101001".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, "-----011000010111000001101001\r\nContent-Disposition: form-data; " +
                                                        "name=\"text\"\r\n\r\n${text}\r\n-----011000010111000001101001\r\nContent-Disposition: form-data;" +
                                                        " name=\"from\"\r\n\r\nauto\r\n-----011000010111000001101001\r\nContent-Disposition: form-data;" +
                                                        " name=\"to\"\r\n\r\n${languageCode}\r\n-----011000010111000001101001--\r\n\r\n")
        val request = Request.Builder()
            .url("https://translate281.p.rapidapi.com/")
            .post(body)
            .addHeader("x-rapidapi-key", "9ddc8e4708mshbb1ee6f383dd713p133201jsne080b9d94006")
            .addHeader("x-rapidapi-host", "translate281.p.rapidapi.com")
            .addHeader("Content-Type", "multipart/form-data; boundary=---011000010111000001101001")
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val responseBody = client.newCall(request).execute()
            val responseString = responseBody

            val response = responseString.body!!.string()

            val jsonObject = JSONObject(response)
            translatedText = jsonObject.getString("response").toString()
            Log.d("Translated Text", translatedText)

            withContext(Dispatchers.Main) {
                callback(translatedText)
            }
        }


    }
}