package com.example.cryptoo

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cryptoo.Adapter.chatMessageAdapter
import com.example.cryptoo.databinding.ActivityChatactivityBinding
import com.example.cryptoo.dataclass.NotificationData
import com.example.cryptoo.dataclass.pushNotification
import com.google.android.play.integrity.internal.j
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.util.Date

class chatactivity : AppCompatActivity() {
    lateinit var b :  ActivityChatactivityBinding
    lateinit var database: FirebaseDatabase
    lateinit var senderuid : String
    lateinit var recevieruid : String
     var fcm : String? = null
    var cu : String? = null
    var ownername : String? = null

    lateinit var senderrrom : String
    lateinit var recevierrom : String

       val view : View? = null
     var list : ArrayList<messagemodel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b =  ActivityChatactivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        database =  FirebaseDatabase.getInstance()

        setSupportActionBar(b.topbar)

        supportActionBar?.setHomeAsUpIndicator(com.example.cryptoo.R.drawable.baseline_arrow_back_24)
        b.topbar.setTitleTextColor(Color.WHITE)


        val userimage =  intent.getStringExtra("userimage").toString()
       Glide.with(this).load(Uri.parse(userimage)).into(b.userprofileimage)

        val name =  intent.getStringExtra("username").toString()




        cu =  intent.getStringExtra("cu").toString()
        b.profilename.text = name

        title = null

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sp =   getSharedPreferences("helo" , Context.MODE_PRIVATE)
        ownername =  sp.getString("nnn","").toString()




        senderuid =  FirebaseAuth.getInstance().uid.toString()

         recevieruid = intent.getStringExtra("uid").toString()
         fcm = intent.getStringExtra("fcm").toString()

        senderrrom =  senderuid + recevieruid
        recevierrom = recevieruid + senderuid

       b.profilename.setOnClickListener {
           Toast.makeText(this, "profile page", Toast.LENGTH_SHORT).show()
       }



        b.sendbutton.setOnClickListener {

            if(b.message.text.isEmpty()){
                Toast.makeText(this, "ENter the text to send", Toast.LENGTH_SHORT).show()
            } else {

                val message = messagemodel(name , b.message.text.toString() , senderuid , System.currentTimeMillis())
                b.message.text= null

                val randomkey = database.reference.push().key

                database.reference.child("chats")
                    .child(senderrrom).child("message").child(randomkey!!).setValue(message)
                    .addOnSuccessListener {
                        database.reference.child("chats").child(recevierrom).child("message").child(randomkey).setValue(message)
                            .addOnSuccessListener {
                                var s = b.message.text.toString()

                              pushNotification(
                                  NotificationData("BeeChat","You have received one message from $ownername ") ,
                                  fcm!!
                              ).also {
                                  sendnotification(it)
                              }

                            }
                    }
            }
        }

        database.reference.child("chats").child(senderrrom).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                     if(snapshot.exists()) {

                         for(i in  snapshot.children){
                             val data = i.getValue(messagemodel::class.java)
                             list.add(data!!)

                         }
                         b.chatmessagerv.layoutManager =  LinearLayoutManager(this@chatactivity)
                         val adapter = chatMessageAdapter(this@chatactivity , list)

                         b.chatmessagerv.adapter = adapter
                         adapter.notifyDataSetChanged()


                     } else {
                         list.clear()
                         val nulllist  : ArrayList<messagemodel> =  arrayListOf()
                         nulllist.add(messagemodel("Send Some Text" , null , null))
                         val adapter = chatMessageAdapter(this@chatactivity , nulllist)
                         b.chatmessagerv.adapter = adapter


                     }
                    b.chatmessagerv.post {
                        val last = b.chatmessagerv.adapter?.itemCount?.minus(1)
                        b.chatmessagerv.scrollToPosition(last!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun sendnotification(notifications : pushNotification) = CoroutineScope(Dispatchers.IO).launch {
     try {
         val response  = RetrofitInstance.api.postnotification(notifications)
         if(response.isSuccessful){
             Toast.makeText(this@chatactivity, "message sent", Toast.LENGTH_SHORT).show()
         }

     }catch (e:Exception){
         Log.e("hh" , e.toString())
     }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}