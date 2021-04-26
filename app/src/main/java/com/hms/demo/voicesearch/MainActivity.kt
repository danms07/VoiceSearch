package com.hms.demo.voicesearch

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hms.demo.voicesearch.databinding.MainBinding
import com.huawei.hms.searchkit.SearchKitInstance
import com.huawei.hms.searchkit.bean.WebItem
import com.huawei.hms.searchkit.bean.WebSearchRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity(),SpeechRecognizer.OnSpeechEventListener {

    lateinit var binding: MainBinding
    val adapter: ResultsAdapter = ResultsAdapter()

    companion object {
        const val MIC_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener { setupRec() }
        binding.recycler.adapter = adapter

    }

    private fun setupRec() {
        if (checkMicPermission()) {
            startRecording()
        } else requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), MIC_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkMicPermission()) startRecording()
    }

    private fun startRecording() {
       SpeechRecognizer(this).apply {
            listener=this@MainActivity
            startRecording()
        }
    }

    private fun checkMicPermission(): Boolean {
        val mic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return mic == PackageManager.PERMISSION_GRANTED
    }

    private var pickupDialog: AlertDialog? = null

    private fun createDialog(): AlertDialog {
        return AlertDialog.Builder(this)
            .create()
    }

    override fun onPartialResults(partialResults: String){
        pickupDialog?.setMessage(partialResults)
    }

    override fun onResults(finalResults: String) {

        pickupDialog?.setTitle(R.string.searching)
        performSearch(finalResults)
    }

    override fun onStartListening() {
        if (pickupDialog == null) pickupDialog = createDialog()
        pickupDialog?.let {
            it.setTitle(R.string.speak_now)
            it.setMessage("")
            if (!it.isShowing) it.show()
        }

    }

    override fun onStartCapturing(){
        pickupDialog?.setTitle(R.string.capturing_voice)
    }

    private fun performSearch(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val searchKitInstance=SearchKitInstance.instance
            val webSearchRequest = WebSearchRequest().apply {
                setQ(query)
                setLang(searchKitInstance.loadLang())
                setSregion(searchKitInstance.loadRegion())
                setPs(5)
                setPn(1)
            }
            val response = searchKitInstance.webSearcher.search(webSearchRequest)

            displayResults(response.data)
            playResults(response.data)

        }
    }

    private fun playResults(data: List<WebItem>) {
        val speaker=Speaker()
        val intro=getString(R.string.found)
        speaker.speak(intro)
        for(item in data){
            speaker.speak(item.title)
        }


    }

    private fun displayResults(data: List<WebItem>) {
        runOnUiThread {
            pickupDialog?.apply {
                dismiss()
                cancel()
            }
            adapter.items.apply {
                clear()
                addAll(data)
            }
            adapter.notifyDataSetChanged()
        }
    }


}