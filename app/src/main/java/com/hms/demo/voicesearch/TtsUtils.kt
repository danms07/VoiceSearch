package com.hms.demo.voicesearch

import com.huawei.hms.mlsdk.tts.MLTtsConfig
import java.util.*


fun MLTtsConfig.loadLang() :String{
    return when (Locale.getDefault().language) {
        "es" -> "es-ES"
        "fr" -> "fr-FR"
        "de" -> "de-DE"
        "it" -> "it-IT"
        else -> "en-US"
    }
}

fun MLTtsConfig.getDefaultSpeaker():String{
    return "${loadLang()}-st-1"
}