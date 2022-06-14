package com.example.lab8

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import com.example.lab8.databinding.ActivityBai2Binding
import java.util.concurrent.TimeUnit

class Bai2Activity : AppCompatActivity() {
    lateinit var binding:ActivityBai2Binding
    lateinit var mediaPlayer: MediaPlayer
    lateinit var seekBar:SeekBar
    var temp:Int = R.raw.diquamuaha
    var handle = Handler()
    var startTime:Double = 0.0
    var finalTime:Double = 0.0
    var oneTimeOnly = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBai2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        seekBar = binding.seekBar
        val playBtn = binding.playBtn
        val pauseBtn = binding.pauseBtn
        val stopBtn = binding.stopBtn

        seekBar.isClickable = false
        pauseBtn.isEnabled = false
        stopBtn.isEnabled = false

        playBtn.setOnClickListener { play() }
        pauseBtn.setOnClickListener { pause() }
        stopBtn.setOnClickListener { stop() }

        mediaPlayer = MediaPlayer.create(this,temp)
        val listSong = arrayOf("Di qua mua ha","Ve phia mua","em co con dung so nay khong")
        val list = ArrayList<String>()
        list.addAll(listSong)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,list)
        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            if(mediaPlayer.isPlaying){
                stop()
            }
            temp = when(position){
                0 -> R.raw.diquamuaha
                1 -> R.raw.vephiamua
                2 -> R.raw.emcocondungsonaykhong
                else -> {R.raw.diquamuaha}
            }
            mediaPlayer = MediaPlayer.create(this,temp)
            play()
        }


    }

    fun play(){
        binding.playBtn.isEnabled = false
        Toast.makeText(this,"Playing",Toast.LENGTH_SHORT).show()
        mediaPlayer.start()
        binding.pauseBtn.isEnabled = true
        binding.stopBtn.isEnabled = true
        finalTime = mediaPlayer.duration.toDouble()
        startTime = mediaPlayer.currentPosition.toDouble()
        if(oneTimeOnly==0){
            seekBar.max = finalTime.toInt()
            oneTimeOnly = 1
        }
        binding.sizeSong.text = String.format("%d min, %d sec",
            TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()))
        )
        binding.currentTimeSong.text = String.format("%d min, %d sec",
            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))
        )

        seekBar.progress = startTime.toInt()
        handle.postDelayed(UpdateSongTime,100)

    }
    private val UpdateSongTime = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            binding.currentTimeSong.text = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))
            )
            seekBar.progress = startTime.toInt()
            handle.postDelayed(this,100)
        }

    }

    fun pause(){
        Toast.makeText(this,"Pause",Toast.LENGTH_SHORT).show()
        mediaPlayer.pause()
        binding.pauseBtn.isEnabled = false
        binding.playBtn.isEnabled = true
    }

    fun stop(){
        mediaPlayer.stop()
        binding.playBtn.isEnabled = true
        startTime = 0.0
        seekBar.progress = startTime.toInt()
        binding.stopBtn.isEnabled = false
        binding.pauseBtn.isEnabled = false
        binding.currentTimeSong.text = String.format("%d min, %d sec",
            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))
        )
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this,temp)
        handle.removeCallbacks(UpdateSongTime)
    }

}