package com.example.networkspeedmeter

import android.app.Application
import io.branch.referral.Branch

class NetworkApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize the Branch object
        Branch.getAutoInstance(this)
    }

}