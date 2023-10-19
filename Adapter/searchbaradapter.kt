package com.example.cryptoo.Adapter

import android.content.Context
import android.content.Intent
import android.icu.text.RelativeDateTimeFormatter
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cryptoo.ContactModel
import com.example.cryptoo.R
import com.example.cryptoo.databinding.CallItemLayoutBinding
import kotlinx.coroutines.withContext

class searchbaradapter(var c: Context, var list: List<ContactModel>) : RecyclerView.Adapter<searchbaradapter.viewholder>() {
    inner class viewholder(view : View) :  ViewHolder(view) {
        var b: CallItemLayoutBinding = CallItemLayoutBinding.bind(view)
    }
    fun setfilteredlit( mlist : List<ContactModel>) : searchbaradapter{
         this.list = mlist
        notifyDataSetChanged()

        return searchbaradapter(c , mlist)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(c).inflate(R.layout.call_item_layout , parent , false))
    }

    override fun getItemCount(): Int {
        return  list.size
    }


    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val i = list[position]
        holder.b.callername.text = i.name
        holder.b.phonenumber.text = i.pno
        Glide.with(c).load(i.pho).placeholder(R.drawable.profile).into(holder.b.callerimage)
        holder.b.invitebutton.setOnClickListener {
            val intent =  Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Download our application")
            intent.putExtra(Intent.EXTRA_TEXT, "link :- ")
            c.startActivity(Intent.createChooser(intent , "Share Via"))
        }
    }
}