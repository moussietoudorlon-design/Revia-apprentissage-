package com.example.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AudioTtsManager(context: Context) {
    private val TAG = "AudioTtsManager"
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentProgress = MutableStateFlow(0f)
    val currentProgress: StateFlow<Float> = _currentProgress.asStateFlow()

    private var fullText: String = ""
    private var textChunks: List<String> = emptyList()
    private var currentChunkIndex = 0

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.FRENCH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.US)
                }
                isInitialized = true
                setupProgressListener()
            } else {
                Log.e(TAG, "TTS Init failed with status: $status")
            }
        }
    }

    private fun setupProgressListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isPlaying.value = true
            }

            override fun onDone(utteranceId: String?) {
                if (currentChunkIndex < textChunks.size - 1) {
                    currentChunkIndex++
                    val progress = currentChunkIndex.toFloat() / textChunks.size.toFloat()
                    _currentProgress.value = progress
                    speakChunk(currentChunkIndex)
                } else {
                    _isPlaying.value = false
                    _currentProgress.value = 1f
                }
            }

            override fun onError(utteranceId: String?) {
                _isPlaying.value = false
            }
        })
    }

    fun prepareText(text: String) {
        fullText = text
        textChunks = text.split(Regex("(?<=[.!?])\\s+")).filter { it.isNotBlank() }
        currentChunkIndex = 0
        _currentProgress.value = 0f
    }

    fun play() {
        if (!isInitialized || textChunks.isEmpty()) return
        _isPlaying.value = true
        speakChunk(currentChunkIndex)
    }

    private fun speakChunk(index: Int) {
        if (index in textChunks.indices) {
            val chunk = textChunks[index]
            val params = android.os.Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "revia_chunk_$index")
            }
            tts?.speak(chunk, TextToSpeech.QUEUE_FLUSH, params, "revia_chunk_$index")
        }
    }

    fun pause() {
        tts?.stop()
        _isPlaying.value = false
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }

    fun skip15SecondsForward() {
        val step = 3 // advance ~3 sentences
        currentChunkIndex = (currentChunkIndex + step).coerceAtMost(textChunks.size - 1)
        val progress = if (textChunks.isNotEmpty()) currentChunkIndex.toFloat() / textChunks.size.toFloat() else 0f
        _currentProgress.value = progress
        if (_isPlaying.value) {
            speakChunk(currentChunkIndex)
        }
    }

    fun seekTo(fraction: Float) {
        if (textChunks.isEmpty()) return
        currentChunkIndex = ((textChunks.size - 1) * fraction).toInt().coerceIn(0, textChunks.size - 1)
        _currentProgress.value = fraction
        if (_isPlaying.value) {
            speakChunk(currentChunkIndex)
        }
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
