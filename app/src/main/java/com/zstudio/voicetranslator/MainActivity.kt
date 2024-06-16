package com.zstudio.voicetranslator

import android.R
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.zstudio.voicetranslator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 102
    private lateinit var binding: ActivityMainBinding
    private lateinit var speechRecognition: SpeechRecognition
    private lateinit var textTranslation: TextTranslation
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var fromLanguage: String
    private lateinit var toLanguage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        speechRecognition = SpeechRecognition(this@MainActivity, REQUEST_CODE)

        textTranslation = TextTranslation(this@MainActivity)

        textToSpeech = TextToSpeech(this@MainActivity)
        mediaPlayer = MediaPlayer()

        val categories = arrayOf(
            "Japanese",
            "English",
            "Italian",
            "German",
            "Hindi",
            "Spanish",
            "French"
        )


        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, categories)
        binding.fromSpineer.adapter = spinnerAdapter

        binding.toSpinner.adapter = spinnerAdapter


        binding.button.setOnClickListener {
            fromLanguage = binding.fromSpineer.selectedItem.toString()
            toLanguage = binding.toSpinner.selectedItem.toString()
            speechRecognition.startSpeechToText()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        speechRecognition.handleActivityResult(requestCode, resultCode, data) { result ->
            textTranslation.textToText(result!!, toLanguage){ textTranslation ->
                binding.textView.text = textTranslation
                textToSpeech.textToVoice(textTranslation, toLanguage){textToSpeech ->
                    Log.d("AudioUrl", textToSpeech)
                    textToSpeech.let {
                        mediaPlayer.reset()
                        mediaPlayer.setDataSource(textToSpeech)
                        mediaPlayer.prepare()
                        mediaPlayer.start()

                    }


                }

            }
        }
    }

}