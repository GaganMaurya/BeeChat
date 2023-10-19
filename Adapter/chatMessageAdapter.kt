package com.example.cryptoo.Adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cryptoo.R
import com.example.cryptoo.databinding.RecevieItemLayooutBinding
import com.example.cryptoo.databinding.SendItemLayooutBinding
import com.example.cryptoo.messagemodel
import com.google.android.play.integrity.internal.t
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class chatMessageAdapter(var c :  Context , var list :  ArrayList<messagemodel>) : Adapter<ViewHolder>() {

    var itemsent = 1
    var itemreceive = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType == itemsent)
            SenderViewHolder(LayoutInflater.from(c).inflate(R.layout.send_item_layoout , parent , false))
        else ReceiverViewHolder(LayoutInflater.from(c).inflate(R.layout.recevie_item_layoout , parent , false))
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

      var message = list[position]

        if(holder.itemViewType == itemsent){
            val viewholder = holder as SenderViewHolder
            viewholder.binding.sendmessage.text = message.message

            val ct =  message.timestamp.toString().toLong()
            val date = Date(ct)

            val timeformat =  SimpleDateFormat("hh:mm a" , Locale.getDefault())
            val time =  timeformat.format(date)
            viewholder.binding.timeformat.text = time


        }else {
            val viewholder = holder as ReceiverViewHolder
            viewholder.binding.receivemessage.text = message.message


            val ct =  message.timestamp.toString().toLong()
            val date = Date(ct)

            val timeformat =  SimpleDateFormat("hh:mm a" , Locale.getDefault())
            val time =  timeformat.format(date)
            viewholder.binding.timeformat.text = time
        }

//
//        val  l = list.size -1
//        var t = list[l].timestamp.toString()
////        val d = Date(lct)
////
////        val timeformat =  SimpleDateFormat("hh:mm a" , Locale.getDefault())
////        val t =  timeformat.format(d)
//



    }



  inner class ReceiverViewHolder(view: View) :  ViewHolder(view){
      val binding :  RecevieItemLayooutBinding  = RecevieItemLayooutBinding.bind(view)

  }
    inner class SenderViewHolder(view: View) :  ViewHolder(view){
        val binding :  SendItemLayooutBinding  = SendItemLayooutBinding.bind(view)

    }


    override fun getItemViewType(position: Int): Int {


        return  if(FirebaseAuth.getInstance().uid == list[position].senderid)itemsent else itemreceive
    }

}