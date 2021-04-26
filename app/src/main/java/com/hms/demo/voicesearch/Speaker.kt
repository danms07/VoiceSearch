package com.hms.demo.voicesearch

import android.os.Bundle
import android.util.Log
import android.util.Pair
import com.huawei.hms.mlsdk.tts.*


class Speaker: MLTtsCallback {

    private var mlTtsEngine: MLTtsEngine? = null
    private var latestTask:String=""

    init{
        // Use customized parameter settings to create a TTS engine.
        val mlTtsConfig = MLTtsConfig().apply {
            language = loadLang()
            person = getDefaultSpeaker()
            // Set the speech speed. The range is (0,5.0]. 1.0 indicates a normal speed.
            speed = 1.0f
            // Set the volume. The range is (0,2). 1.0 indicates a normal volume.
            volume = 1.0f
        }
        mlTtsEngine = MLTtsEngine(mlTtsConfig).apply {
            setPlayerVolume(20)
            updateConfig(mlTtsConfig)
            setTtsCallback(this@Speaker)
        }

    }

    fun speak(speech: String){
        mlTtsEngine?.let{latestTask=it.speak(speech, MLTtsEngine.QUEUE_APPEND)}
    }

    override fun onError(code: String?, error: MLTtsError?) {
        error?.let{Log.e("TTS", it.errorMsg)}
    }

    override fun onWarn(p0: String?, p1: MLTtsWarn?) {
        //unused
    }

    override fun onRangeStart(p0: String?, p1: Int, p2: Int) {
        //unused
    }

    override fun onAudioAvailable(
        p0: String?,
        p1: MLTtsAudioFragment?,
        p2: Int,
        p3: Pair<Int, Int>?,
        p4: Bundle?
    ) {

    }

    override fun onEvent(taskId: String, eventId: Int, p2: Bundle?) {
        if (eventId==MLTtsConstants.EVENT_SYNTHESIS_COMPLETE && taskId==latestTask) {
                mlTtsEngine?.shutdown()
            Log.i("Speaker","Stop")

        }
    }

}