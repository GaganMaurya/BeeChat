package com.example.cryptoo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.cryptoo.databinding.ActivityNumberBinding
import com.google.android.play.integrity.internal.m
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class NumberActivity : AppCompatActivity() {
    lateinit var  b  : ActivityNumberBinding
    lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b =  ActivityNumberBinding.inflate(layoutInflater)
        setContentView(b.root)

       auth = FirebaseAuth.getInstance()

        b.btn.setOnClickListener {
            if(b.phonum.text!!.isEmpty()){
                Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show()
            } else {
                val i =  Intent(this , OtPactivity::class.java)
                i.putExtra("number" , b.phonum.text.toString())
                startActivity(i)
            }
        }

    }




    override fun onStart() {
        super.onStart()



            val sp =  getSharedPreferences("helo" , Context.MODE_PRIVATE)
            val name  =  sp.getString("nnn" , "").toString()
              if(auth.currentUser  == null){
                 Toast.makeText(this, "Welcome to the App", Toast.LENGTH_SHORT).show()
             }
             else if(name.isEmpty()){
                 startActivity(Intent(this , Profileacticity::class.java))
                 finish()
             }
             else {
                 startActivity(Intent(this , MainActivity::class.java))
                 finish()


         }

    }

}