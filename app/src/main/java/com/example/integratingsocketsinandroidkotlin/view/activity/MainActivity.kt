package com.example.integratingsocketsinandroidkotlin.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.integratingsocketsinandroidkotlin.R
import com.example.integratingsocketsinandroidkotlin.view.fragment.VideoStreamingFragment

class MainActivity : AppCompatActivity() {
    //Video can run in app
    private lateinit var videoView: VideoView
    private lateinit var mediaControls: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editText)
        val enterBtn = findViewById<Button>(R.id.enterBtn)
        val uploadBtn = findViewById<Button>(R.id.uploadFile)
        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = VideoStreamingFragment()

        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 10
            )
        }

        enterBtn.setOnClickListener {
            if (editText.text.toString() == "") {
                Toast.makeText(this@MainActivity, "Please Enter the Name", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("name", editText.text.toString())
                startActivity(intent)
            }
        }
        uploadBtn.setOnClickListener {
            val mBundle = Bundle()
            mFragment.arguments = mBundle
            mFragmentTransaction.add(R.id.frameLayout, mFragment).commit()

        }
    }
}