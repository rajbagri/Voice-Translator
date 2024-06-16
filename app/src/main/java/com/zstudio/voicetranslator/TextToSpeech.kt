package com.zstudio.voicetranslator

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class TextToSpeech(private val activity: Activity) {
    fun textToVoice(text: String, language: String, callback: (String) -> Unit) {
        val client = OkHttpClient()


        val jobTitlesMap = mapOf(
            "English" to arrayOf("en-US", "en-US_Joey"),
            "Italian" to arrayOf("it-IT", "it-IT_Carla"),
            "German" to arrayOf("de-DE", "de-DE_Hans"),
            "Hindi" to arrayOf("hi-IN", "hi-IN_Aditi"),
            "Spanish" to arrayOf("es-ES", "es-ES_Enrique"),
            "French" to arrayOf("fr-FR", "fr-FR_Ma02thieu"),
            "Japanese" to arrayOf("ja-JP", "ja-JP_Mizuki")
        )

        val languageCode = jobTitlesMap[language]?.get(0)
        val voiceCode = jobTitlesMap[language]?.get(1)

        Log.d("LanguageCode", languageCode!!)
        Log.d("VoiceCode", voiceCode!!)

        val mediaType = "application/json".toMediaType()

        val body = RequestBody.create(mediaType, "{\"ttsService\":\"polly\"," +
                "\"audioKey\":\"ff63037e-6994-4c50-9861-3e162ee56468\"," +
                "\"storageService\":\"s3\"," +
                "\"text\":\"<speak><p>${text}</p></speak>\"," +
                "\"voice\":{\"value\":\"${voiceCode}\"," +
                "\"lang\":\"${languageCode}\"}," +
                "\"audioOutput\":{\"fileFormat\":\"mp3\",\"sampleRate\":24000}}")

        val request = Request.Builder()
            .url("https://natural-text-to-speech-converter-at-lowest-price.p.rapidapi.com/backend/ttsNewDemo")
            .post(body)
            .addHeader("x-rapidapi-key", "dab961b84emshb7172fe3e6ad60fp1baf5djsnd4f25a34d5d4")
            .addHeader("x-rapidapi-host", "natural-text-to-speech-converter-at-lowest-price.p.rapidapi.com")
            .addHeader("Content-Type", "application/json")
            .build()


        CoroutineScope(Dispatchers.IO).launch{
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body!!.string()

                if (response.isSuccessful) {

                    // Play the audio using MediaPlayer
                    val jsonObject = JSONObject(responseBody)
                    val audioUrl = jsonObject.getString("url")
                    Log.d("AudioUrl", audioUrl)

                    withContext(Dispatchers.Main) {
                        callback(audioUrl)
                    }

                }
            }
            catch (e: IOException) {
                Log.e("NetworkError", "Error: ${e.message}", e)
            }
        }
    }
}