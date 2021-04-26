package com.hms.demo.voicesearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.huawei.hms.mlsdk.asr.MLAsrConstants
import com.huawei.hms.mlsdk.asr.MLAsrListener
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer
import java.util.*

class SpeechRecognizer(private val context: Context) : MLAsrListener {

    private var mSpeechRecognizer: MLAsrRecognizer? = null
    var listener: OnSpeechEventListener? = null

    fun startRecording() {
        val mSpeechRecognizerIntent = Intent(MLAsrConstants.ACTION_HMS_ASR_SPEECH).apply {
            putExtra(MLAsrConstants.FEATURE, MLAsrConstants.FEATURE_WORDFLUX)
            when (Locale.getDefault().language) {
                "es" -> {
                    putExtra(MLAsrConstants.LANGUAGE, MLAsrConstants.LAN_ES_ES)
                }
                "fr" -> {
                    putExtra(MLAsrConstants.LANGUAGE, MLAsrConstants.LAN_FR_FR)
                }
                "de" -> {
                    putExtra(MLAsrConstants.LANGUAGE, MLAsrConstants.LAN_DE_DE)
                }
                else -> {
                    putExtra(MLAsrConstants.LANGUAGE, MLAsrConstants.LAN_EN_US)
                }
            }
        }

        try {
            mSpeechRecognizer = MLAsrRecognizer.createAsrRecognizer(context).apply {
                setAsrListener(this@SpeechRecognizer)
                startRecognizing(mSpeechRecognizerIntent)
            }

        } catch (e: Exception) {
            onError(1, e.message)
        }
    }

    override fun onRecognizingResults(results: Bundle) {


        val message = StringBuilder()
        for (key in results.keySet()) {
            message.append(results[key])
        }
        listener?.onPartialResults(message.toString())
    }

    override fun onResults(results: Bundle) {
        //Release resources
        mSpeechRecognizer?.setAsrListener(null)
        mSpeechRecognizer?.destroy()
        //Notify the recognizing results
        val query = StringBuilder()
        for (key in results.keySet()) {
            query.append(results[key])
        }
        listener?.onResults(query.toString())
    }


    override fun onError(p0: Int, error: String?) {
        error?.let { Log.e("ASR", it) }
    }

    override fun onStartListening() {
        listener?.onStartListening()

    }

    override fun onStartingOfSpeech() {
        listener?.onStartCapturing()
    }

    override fun onVoiceDataReceived(p0: ByteArray?, p1: Float, p2: Bundle?) {
        //unused
    }

    override fun onState(p0: Int, p1: Bundle?) {
        //unused
    }


    interface OnSpeechEventListener {
        fun onStartListening()
        fun onStartCapturing()
        fun onPartialResults(partialResults: String)
        fun onResults(finalResults: String)
    }
}