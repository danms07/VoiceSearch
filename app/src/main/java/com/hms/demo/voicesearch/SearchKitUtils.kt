package com.hms.demo.voicesearch

import android.content.Context
import android.util.Log
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.searchkit.SearchKitInstance
import com.huawei.hms.searchkit.utils.Language
import com.huawei.hms.searchkit.utils.Region
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder
import java.text.MessageFormat
import java.util.*

const val APP_SECRET:String="PUT_YOUR_APP_SECRET_HERE"

 fun SearchKitInstance.loadLang(): Language {
    return when (Locale.getDefault().language) {
        "es" -> Language.SPANISH
        "fr" -> Language.FRENCH
        "de" -> Language.GERMAN
        "it" -> Language.ITALIAN
        "pt" -> Language.PORTUGUESE
        else -> Language.ENGLISH
    }
}


fun SearchKitInstance.loadRegion(): Region {
    return when (Locale.getDefault().country){
        "MX" -> Region.MEXICO
        "CL"-> Region.CHILE
        "CO" -> Region.COLOMBIA
        "ES"-> Region.SPAIN
        "SG" -> Region.SINGAPORE
        "TR" -> Region.TURKEY
        else -> Region.WHOLEWORLD
    }
}

const val TOKEN_URL = "https://oauth-login.cloud.huawei.com/oauth2/v3/token"

fun SearchKitInstance.refreshToken(context: Context) {
    val config = AGConnectServicesConfig.fromContext(context)
    val appId = config.getString("client/app_id")
    val url = URL(TOKEN_URL)
    val headers = HashMap<String, String>().apply {
        put("content-type", "application/x-www-form-urlencoded")
    }
    val msgBody = MessageFormat.format(
        "grant_type={0}&client_secret={1}&client_id={2}",
        "client_credentials", URLEncoder.encode(APP_SECRET, "UTF-8"), appId
    ).toByteArray(Charsets.UTF_8)

    val response = HTTPHelper.sendHttpRequest(url, "POST", headers, msgBody)
    val accessToken = JSONObject(response).let {
        if (it.has("access_token")) {
            it.getString("access_token")
        } else ""
    }
    setInstanceCredential(accessToken)

}