package com.example.cryptoo

import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import com.example.cryptoo.databinding.ActivityProfileacticityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

import java.io.ByteArrayOutputStream
import java.util.Base64


class Profileacticity : AppCompatActivity() {
    lateinit var b  : ActivityProfileacticityBinding
    lateinit var auth : FirebaseAuth
    lateinit var databaseReference: FirebaseDatabase
    lateinit var storage : FirebaseStorage
     var selectedimage : Uri? = null
    lateinit var dialog: AlertDialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileacticityBinding.inflate(layoutInflater)
        setContentView(b.root)

        val a = AlertDialog.Builder(this@Profileacticity)
            a.setMessage("Updating Profile")
            a.setCancelable(false)
            a.setTitle("Please Wait")
            dialog = a.create()


        auth =  FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()




        b.userimage.setOnClickListener {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.activity_dialog_image) // Create a new layout file "dialog_image.xml" for the dialog's content
                 dialog.setCancelable(true)

                val dialogImage = dialog.findViewById<ImageView>(R.id.dialogImage)
                dialogImage.setImageDrawable(b.userimage.drawable)

//                val window = dialog.window
//                window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
//                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                 dialog.setCanceledOnTouchOutside(true)


                dialog.show()
            }

        b.edit.setOnClickListener {
            val int =  Intent()
            int.action = Intent.ACTION_GET_CONTENT
            int.type = "image/*"
            startActivityForResult(int , 1)
        }

        b.cbtn.setOnClickListener {
            if(b.name.text!!.isEmpty()){
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()

            }else if(b.userimage.drawable == null){
                Toast.makeText(this, "PLease insert image", Toast.LENGTH_SHORT).show()
            }
            else {


                dialog.show()
                var sp : SharedPreferences =  getSharedPreferences("helo" , Context.MODE_PRIVATE)
                val edit  =  sp.edit()
                edit.apply{
                    putString("nnn" , b.name.text.toString())
                }.apply()


                val baos = ByteArrayOutputStream()
                val bitmap = b.userimage.drawable.toBitmap()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val encode = android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT)
                with(sp.edit()) {
                    putString("encodeimage", encode)
                    apply()
                }



                uploaddata()

            }
        }

    }

    private fun uploaddata() {


            val ref =
                storage.reference.child("Profile").child(FirebaseAuth.getInstance().uid.toString()+"userimage")
            ref.putFile(selectedimage!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnSuccessListener { task ->
                        uploadinfo(task.toString())

                    }
                }
            }

    }

    private fun uploadinfo(hello: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.toString()
                val user = modelclass(
                    auth.uid.toString(),
                    b.name.text.toString(),
                    auth.currentUser!!.phoneNumber.toString(),
                    hello,
                    token
                )
                databaseReference.reference.child("User").child(auth.uid.toString()).setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show()
                        val inn = Intent(this, MainActivity::class.java)
                        inn.putExtra("cu", b.name.text.toString())
                        startActivity(inn)
                        dialog.dismiss()
                        finish()
                    }

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null){
            if(data.data != null){
                selectedimage = data.data!!
                b.userimage.setImageURI(selectedimage)


            }
        }
    }
}