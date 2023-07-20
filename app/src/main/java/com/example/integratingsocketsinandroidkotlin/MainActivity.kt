package com.example.integratingsocketsinandroidkotlin

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    //Video can run in app
    private lateinit var videoView: VideoView
    private lateinit var mediaControls: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ///////////////////////////
/*        videoView = findViewById(R.id.videoPlayInApp)

        mediaControls = MediaController(this)
        mediaControls!!.setAnchorView(this.videoView)

        videoView!!.setMediaController(mediaControls)
        videoView!!.setVideoURI(
            Uri.parse(
                "android.resource://" + packageName + "/" + R.raw.screenrecorder
            )
        )
        videoView!!.requestFocus()
        videoView!!.start()
        videoView!!.setOnCompletionListener {
            Toast.makeText(
                applicationContext, "Video completed", Toast.LENGTH_LONG
            ).show()
            true
        }
        videoView!!.setOnErrorListener { mp, what, extra ->
            Toast.makeText(
                applicationContext,
                "An Error Occurred " + "While Playing Video !!!",
                Toast.LENGTH_LONG
            ).show()
            false
        }*/
////////////////////////////////////

        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 10
            )
        }

        val editText = findViewById<EditText>(R.id.editText)
        val enterBtn = findViewById<Button>(R.id.enterBtn)

        enterBtn.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("name", editText.text.toString())
            startActivity(intent)
        }
    }
}