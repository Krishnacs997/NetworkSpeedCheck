package com.example.networkspeedmeter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.LinkProperties

class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        val buo = BranchUniversalObject()
            .setCanonicalIdentifier("content/12345")
            .setTitle("My Content Title")
            .setContentDescription("My Content Description")

        val lp = LinkProperties()
            .setChannel("facebook")
            .setFeature("sharing")

        buo.generateShortUrl(this, lp) { url, error ->
            if (error == null) {
                // Share the link
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, url)
                startActivity(Intent.createChooser(intent, "Share Link"))
            } else {
                Log.e("Branch", error.message)
            }
        }
    }
}