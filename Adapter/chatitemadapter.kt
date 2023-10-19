package com.example.cryptoo.Adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Adapter
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cryptoo.ContactModel
import com.example.cryptoo.R
import com.example.cryptoo.chatactivity
import com.example.cryptoo.databinding.ChatUserImageBinding
import com.example.cryptoo.dialog_image
import com.example.cryptoo.messagemodel
import com.example.cryptoo.modelclass
import com.example.cryptoo.swipetodelete
import com.google.android.play.integrity.internal.c
import com.google.android.play.integrity.internal.t
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class chatitemadapter(var c : Context ,var  list :  ArrayList<modelclass>) : RecyclerView.Adapter<chatitemadapter.viewholder>() {

    inner class viewholder(view : View) :  ViewHolder(view) {
        var b: ChatUserImageBinding = ChatUserImageBinding.bind(view)

//        init {
//            val la = LayoutInflater.from(c).inflate(R.layout.fragment_chat ,null)
//            var chatrv=  la.findViewById<RecyclerView>(R.id.chatrv)
//
//            var std =  object : swipetodelete(){
//                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
//                    val p = viewHolder.adapterPosition
//                    list.removeAt(p)
//
//                    chatrv.adapter?.notifyItemChanged(p)
//
//
//                }
//            }
//            var iteml = ItemTouchHelper(std)
//            iteml.attachToRecyclerView(chatrv)
//        }



//        it.attachToRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(c).inflate(R.layout.chat_user_image , parent , false))
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val i = list[position]
        Glide.with(c).load(i.image).into(holder.b.usimg)
        holder.b.usname.text = i.name



//        var senderhome = FirebaseAuth.getInstance().uid.toString() + i.uid
//      val firebase =  FirebaseDatabase.getInstance().reference.child("")
//           var d = FirebaseDatabase.getInstance()
//               d.reference.child("chats").child(senderhome).child("message")
//                   .addValueEventListener(object : ValueEventListener {
//                       override fun onDataChange(snapshot: DataSnapshot) {
//
//                           if(snapshot.exists()) {
//                               list.clear()
//                               for(i in  snapshot.children){
//                                   var a = i.child("timestamp")
//
//                               }

        holder.b.usimg.setOnClickListener {
            val intent = Intent(c , dialog_image::class.java)
            intent.putExtra( "imag",i.image.toString() )
            intent.putExtra("n" , i.name.toString())
            c.startActivity(intent)
//            val dialog = Dialog(c)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(R.layout.activity_dialog_image) // Create a new layout file "dialog_image.xml" for the dialog's content
//            dialog.setCancelable(true)
//
//            val dialogImage = dialog.findViewById<ImageView>(R.id.dialogImage)
//            dialogImage.setImageDrawable(holder.b.usimg.drawable)
//
//            val window = dialog.window
//            window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
//            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.setTitle(holder.b.usname.toString())
//            dialog.actionBar
//
//            dialog.setCanceledOnTouchOutside(true)
//
//
//            dialog.show()
        }
        holder.itemView.setOnClickListener {
            holder.b.container.setBackgroundResource(R.drawable.chat_item_bac_bg)
            val intent =  Intent(c , chatactivity::class.java)

            intent.putExtra("uid" , i.uid)
            intent.putExtra("username" , i.name.toString())
            intent.putExtra("pnumber" , i.number.toString())
            intent.putExtra("fcm" , i.fcmtoken.toString())
            intent.putExtra("userimage" , i.image.toString())


            c.startActivity(intent)
        }

    }



}