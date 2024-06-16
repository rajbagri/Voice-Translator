package com.zstudio.voicetranslator

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import java.util.Locale


class SpeechRecognition(private val activity: Activity, private val requestCode: Int) {

    fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        val languageLocale = Locale("en")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageLocale)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageLocale)
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, languageLocale)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...")
        try {
            activity.startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Sorry, your device doesn't support speech language", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?, callback: (String?) -> Unit) {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            callback(result?.get(0))
        }
    }
}
