package com.hms.demo.voicesearch

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object HTTPHelper {
    fun sendHttpRequest(
        url: URL,
        method: String,
        headers: HashMap<String, String>,
        body: ByteArray
    ): String {
        val conn = url.openConnection() as HttpURLConnection
        conn.apply {
            requestMethod = method
            doOutput = true
            doInput = true
            for (key in headers.keys) {
                setRequestProperty(key, headers[key])
            }
            if(method!="GET"){
                outputStream.write(body)
                outputStream.flush()
            }
        }
        val inputStream = if (conn.responseCode < 400) {
            conn.inputStream
        } else conn.errorStream
        return convertStreamToString(inputStream).also { conn.disconnect() }
    }

    private fun convertStreamToString(input: InputStream): String {
        return BufferedReader(InputStreamReader(input)).use {
            val response = StringBuffer()
            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            it.close()
            response.toString()
        }
    }
}