package com.example.lab8

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lab8.databinding.ActivityBai1Binding

class Bai1Activity : AppCompatActivity() {
    lateinit var binding: ActivityBai1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBai1Binding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.bai1CamBtn.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            getResult.launch(takePictureIntent)
        }
    }
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val value = it.data?.extras?.get("data") as Bitmap
            binding.bai1ImgView.setImageBitmap(value)
        }
    }
}