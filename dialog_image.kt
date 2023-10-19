package com.example.cryptoo

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.cryptoo.databinding.ActivityDialogImageBinding

class dialog_image : AppCompatActivity() {
    lateinit var b : ActivityDialogImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDialogImageBinding.inflate(layoutInflater)
        setContentView(b.root)

        setSupportActionBar(b.toolbar)
        supportActionBar?.setHomeAsUpIndicator(com.example.cryptoo.R.drawable.baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val image =  intent.getStringExtra("imag")
        val image2 =  intent.getStringExtra("yes")
        //val a = Uri.parse(image2)




        val nam =  intent.getStringExtra("n")
        if(nam != null) {
            title = nam.toString()
            Glide.with(this).load(Uri.parse(image)).into(b.dialogImage)
        }else {
            var a = Uri.parse(image2)
            title = "My Image"
           b.dialogImage.setImageURI(a)

        }




    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()

    }
}