package com.hms.demo.voicesearch

import android.app.Application
import android.util.Log
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.searchkit.SearchKitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class VoiceSearchApp: Application() {
    override fun onCreate() {
        super.onCreate()

        initMlKit()
        initSearchKit()
    }

    private fun initMlKit() {
        val apiKey = AGConnectServicesConfig
            .fromContext(this)
            .getString("client/api_key")
        MLApplication.getInstance().apiKey = apiKey
    }

    private fun initSearchKit() {
        val appID = AGConnectServicesConfig
            .fromContext(this)
            .getString("client/app_id")
        SearchKitInstance.init(this, appID)
        CoroutineScope(Dispatchers.IO).launch {
            SearchKitInstance.instance.refreshToken(this@VoiceSearchApp)
        }
    }
}