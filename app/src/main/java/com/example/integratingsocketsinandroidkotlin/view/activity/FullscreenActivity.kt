package com.example.integratingsocketsinandroidkotlin.view.activity

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.integratingsocketsinandroidkotlin.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class FullscreenActivity : AppCompatActivity() {

    private lateinit var fullscreenPlayerView: PlayerView
    private lateinit var fullscreenExoPlayer: SimpleExoPlayer
    lateinit var videoTitle: TextView
    lateinit var backBtn: ImageButton
    lateinit var playPauseButton: ImageButton
    lateinit var prevBtn: ImageButton
    lateinit var nextBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        fullscreenPlayerView = findViewById(R.id.fullscreen_player_view)
        videoTitle = findViewById(R.id.videoTitle)
        backBtn = findViewById(R.id.backBtn)
        playPauseButton = findViewById(R.id.playPauseButton)
        prevBtn = findViewById(R.id.prevBtn)
        nextBtn = findViewById(R.id.nextBtn)
        fullscreenExoPlayer = SimpleExoPlayer.Builder(this).build()
        fullscreenPlayerView.player = fullscreenExoPlayer

        val videoUri = Uri.parse(intent.getStringExtra("videoUri"))
        val videoName = intent.getStringExtra("videoName")
        val mediaItem = MediaItem.fromUri(videoUri)
        fullscreenExoPlayer.setMediaItem(mediaItem)
        fullscreenExoPlayer.prepare()
        fullscreenExoPlayer.playWhenReady = true
        videoTitle.isSelected = true
        videoTitle.text = videoName

        backBtn.setOnClickListener {
            finish()
        }

        playPauseButton.setOnClickListener {
            if (fullscreenExoPlayer.isPlaying) pauseVideo()
            else playVideo()
        }

        prevBtn.setOnClickListener {
            Toast.makeText(this, "Previous and next button is not working", Toast.LENGTH_SHORT)
                .show()
        }

        nextBtn.setOnClickListener {
            Toast.makeText(this, "Previous and next button is not working", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun playVideo() {
        playPauseButton.setImageResource(R.drawable.pause_icon)
        fullscreenExoPlayer.play()
    }

    private fun pauseVideo() {
        playPauseButton.setImageResource(R.drawable.play_icon)
        fullscreenExoPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        fullscreenExoPlayer.release()
    }

}