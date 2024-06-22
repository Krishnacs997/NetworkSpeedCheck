package com.example.networkspeedmeter

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.TrafficStats
import android.net.wifi.WifiInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.networkspeedmeter.databinding.MainActivityBinding
import io.branch.referral.Branch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.nextUp

data class NetworkSpeed(val downloadSpeed: Double, val uploadSpeed: Double)


class MainActivity : ComponentActivity() {
    private lateinit var binding: MainActivityBinding
    private var networkStatus = false
    private var speer = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)



        Thread(Runnable {
            while (true) {
               // sdhf()
            }

           /* while (true) {

                val wifiManger = applicationContext.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager
                val wifiInfo: WifiInfo = wifiManger.connectionInfo

                val speedMbps = wifiInfo.subscriptionId
                // This string is displayed when device is not connected
                // to either of the aforementioned states
                var conStant: String = "Not Connected"

                // Invoking the Connectivity Manager
                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                // Fetching the Network Information
                val netInfo = cm.allNetworkInfo

                // Finding if Network Info typeName is WIFI or MOBILE (Constants)
                // If found, the conStant string is supplied WIFI or MOBILE DATA
                // respectively. The supplied data is a Variable
                for (ni in netInfo) {
                    if (ni.typeName.equals("WIFI", ignoreCase = true))
                        if (ni.isConnected) conStant = speedMbps.toString()
                    if (ni.typeName.equals("MOBILE", ignoreCase = true))
                        if (ni.isConnected) conStant = "MOBILE DATA"
                }

                // To update the layout elements in real-time, use runOnUiThread method
                // We are setting the text in the TextView as the string conState
                runOnUiThread {
                    binding.tvSpeed.text = conStant
                }
            }*/
        }).start() // Starting the thread


        binding.btnClick.setOnClickListener {

            CoroutineScope(Dispatchers.Default).launch {
                val speed = measureCurrentInternetSpeed()
                speer =
                    "Download speed: ${speed.downloadSpeed.toInt()} Kbps\nUpload speed: ${speed.uploadSpeed.toInt()} Kbps"
                Log.d("Speed measure", speer)

                binding.tvSpeed.text = speer
            }

           /*networkStatus = checkNetworkConnection()
            if (networkStatus){
                val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = cm.activeNetworkInfo

                //should check null because in airplane mode it will be null
                val nc = cm.getNetworkCapabilities(cm.activeNetwork)
                val downSpeed = nc!!.linkDownstreamBandwidthKbps
                val upSpeed = nc!!.linkUpstreamBandwidthKbps

                binding.tvSpeed.text = "Download: $downSpeed Upload: $upSpeed"

            }else{
                binding.tvSpeed.text = "Disconnected"
            }*/
        }
    }

    override fun onStart() {
        super.onStart()
        Branch.sessionBuilder(this).withCallback { referringParams, error ->
            if (error == null) {
                // Branch init succeeded, params are the deep linked params
                Log.i("Branch", referringParams.toString())
            } else {
                Log.e("Branch", error.message)
            }
        }.withData(this.intent?.data).init()
    }

        /* private fun checkNetworkConnection(): Boolean{

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->    true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->   true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->   true
                else ->     false
            }
        }
        // For below 29 api
        else {
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false

    }*/


    }


}

suspend fun measureCurrentInternetSpeed(): NetworkSpeed = withContext(Dispatchers.IO) {
    val startRxBytes = TrafficStats.getTotalRxBytes()
    val startTxBytes = TrafficStats.getTotalTxBytes()
    val startTime = System.currentTimeMillis()

    delay(100) // Measure over 1 secon

    val endRxBytes = TrafficStats.getTotalRxBytes()
    val endTxBytes = TrafficStats.getTotalTxBytes()
    val endTime = System.currentTimeMillis()

    val timeDiff = (endTime - startTime) / 100.0 // in seconds
    val downloadSpeed = (endRxBytes - startRxBytes) * 8 / (timeDiff * 1024) // in Mbps
    val uploadSpeed = (endTxBytes - startTxBytes) * 8 / (timeDiff * 1024) // in Mbps

    NetworkSpeed(downloadSpeed, uploadSpeed)
}

