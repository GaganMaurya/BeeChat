package com.example.cryptoo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cryptoo.databinding.ActivityOtPactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class OtPactivity : AppCompatActivity() {
    lateinit var  b : ActivityOtPactivityBinding
    lateinit var auth : FirebaseAuth
    lateinit var  variId  : String
    lateinit var dialog  : Dialog
    lateinit var d  : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b =  ActivityOtPactivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        auth =  FirebaseAuth.getInstance()
        val num = "+91" + intent.getStringExtra("number")
        b.textView2.text = "Enter Otp to verify your number :- " + num

        var sp : SharedPreferences =  getSharedPreferences("numm1" , Context.MODE_PRIVATE)
        val edit  =  sp.edit()
        edit.apply{
            putString("numm" , num)
        }.apply()


        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please Wait...")
        builder.setTitle("We are sending otp!")
        builder.setCancelable(false)
         dialog =  builder.create()
        dialog.show()


        val options =  PhoneAuthOptions.newBuilder(auth).setPhoneNumber(num).setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@OtPactivity, "Please try again", Toast.LENGTH_SHORT).show()

                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    variId = p0

                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        b.btn.setOnClickListener {

            val bi = AlertDialog.Builder(this)
            bi.setMessage("Please Wait...")
            bi.setTitle("We are verifying the otp!")
            bi.setCancelable(false)
             d = bi.create()
            d.show()

            if(b.phonum.text!!.isEmpty()){
                Toast.makeText(this, "Please enter Otp", Toast.LENGTH_SHORT).show()
            }else {
                val credential =  PhoneAuthProvider.getCredential(variId, b.phonum.text.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            d.dismiss()
                            val intent =  Intent(this , Profileacticity::class.java)
                            intent.putExtra("snum" , num)
                            startActivity(intent)
                            finish()
                        }else {
                            dialog.dismiss()
                            Toast.makeText(this@OtPactivity, "You have entered wrong otp!!!!", Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }
    }
}