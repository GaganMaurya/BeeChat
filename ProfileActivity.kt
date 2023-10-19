package com.example.cryptoo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.example.cryptoo.databinding.ActivityProfileBinding
import com.example.cryptoo.fragments.bottomsheetdisplay
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {
    lateinit var b : ActivityProfileBinding
     var dap : Uri? = null
     var naam : String? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        b = ActivityProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(b.root)





        setSupportActionBar(b.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.cryptoo.R.drawable.baseline_arrow_back_24)

       var dd =  FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().uid.toString())
       dd.get().addOnSuccessListener {
           naam = it.child("name").value.toString()
           b.username.text = naam.toString()
       }



        val shared = getSharedPreferences("newimage", Context.MODE_PRIVATE)
        val encode = shared.getString("newimage1", "DEFAULT")
        if (encode != "DEFAULT") {
            val imagebytes = Base64.decode(encode, Base64.DEFAULT)
             val di = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.size)
            b.accountimage.setImageBitmap(di)

            val aap = MediaStore.Images.Media.insertImage(
                contentResolver ,
                di,
                "dd",
            "ds"
            )
            dap = Uri.parse(aap)
        }


        val sss = getSharedPreferences("helo1", Context.MODE_PRIVATE)
//        Toast.makeText(this, imageuri.toString(), Toast.LENGTH_SHORT).show()
       var eddi = sss.edit()
        eddi.apply {
            putString("naam" , naam)

        }.apply()


      var name = sss.getString("naam" , null)
        b.username.text = name.toString()



        var sp : SharedPreferences =  getSharedPreferences("numm1" , Context.MODE_PRIVATE)
        val no = sp.getString("numm" , null)
        b.usserphonenu.text = no.toString()

//        var sp1 : SharedPreferences =  getSharedPreferences("newimage" , Context.MODE_PRIVATE)
//        val encode1 = sp1.getString("newimage1", "DEFAULT")
//        if (encode1 != "DEFAULT") {
//            val imagebytes = Base64.decode(encode1, Base64.DEFAULT)
//            val decodeImage = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.size)
//            b.accountimage.setImageBitmap(decodeImage)
//        }


        b.addaccountimage.setOnClickListener {
         val ite = Intent()
            ite.action = Intent.ACTION_GET_CONTENT
            ite.type = "image/*"
            startActivityForResult(ite , 1)
        }
       b.accountimage.setOnClickListener {


           val intent =  Intent(this , dialog_image::class.java)
           intent.putExtra("yes" , dap.toString())
           startActivity(intent)
       }
     b.constraintLayout1.setOnClickListener {
         var farg = bottomsheetdisplay()
          farg.show(supportFragmentManager , farg.tag)

     }


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!= null){
            val image =  data.data!!
            b.accountimage.setImageURI(image)
            b.pr.visibility = View.VISIBLE
            val ref = FirebaseStorage.getInstance().reference.child("Profile").child(FirebaseAuth.getInstance().uid.toString()+"userimage")
            ref.putFile(image)

            var sp : SharedPreferences =  getSharedPreferences("newimage" , Context.MODE_PRIVATE)
            val baos = ByteArrayOutputStream()
            val bitmap = b.accountimage.drawable.toBitmap()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val encode = android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT)
            with(sp.edit()) {
                putString("newimage1", encode)
                apply()
            }
            b.pr.visibility = View.GONE

        }
    }

}