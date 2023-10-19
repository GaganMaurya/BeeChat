package com.example.cryptoo.Adapter

import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.provider.ContactsContract.Directory
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Adapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cryptoo.ContactModel
import com.example.cryptoo.MainActivity
import com.example.cryptoo.R
import com.example.cryptoo.chatactivity
import com.example.cryptoo.databinding.ChatUserImageBinding
import com.example.cryptoo.databinding.TotalUserListBinding
import com.example.cryptoo.messagemodel
import com.example.cryptoo.modelclass
import com.example.cryptoo.totaluserclass
import com.google.android.play.integrity.internal.c
import com.google.android.play.integrity.internal.f
import com.google.android.play.integrity.internal.t
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class totaluseradapter(var c : Context, var  list :  List<totaluserclass>) : RecyclerView.Adapter<totaluseradapter.viewholder>() {

    lateinit var storage: FirebaseStorage

    inner class viewholder(view: View) : ViewHolder(view) {
        var b: TotalUserListBinding = TotalUserListBinding.bind(view)
    }

    fun setfilteredlit(mlist: List<totaluserclass>): totaluseradapter {
        this.list = mlist
        notifyDataSetChanged()

        return totaluseradapter(c, mlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(c).inflate(R.layout.total_user_list, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        storage = FirebaseStorage.getInstance()

        val i = list[position]
        Glide.with(c).load(i.image).into(holder.b.usimg)
        holder.b.usname.text = i.name



        holder.b.usimg.setOnClickListener {
            val dialog = Dialog(c)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.activity_dialog_image) // Create a new layout file "dialog_image.xml" for the dialog's content
            dialog.setCancelable(true)

            val dialogImage = dialog.findViewById<ImageView>(R.id.dialogImage)
            dialogImage.setImageDrawable(holder.b.usimg.drawable)

            val window = dialog.window
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.setCanceledOnTouchOutside(true)


            dialog.show()
        }
        holder.b.addbutton.setOnClickListener {

            val d = ProgressDialog(c)
            d.setMessage("Adding account to chat")
            d.show()

//            val resourceId = i.image?.toInt() // Assuming imageUrl is a String representing the resource ID
//            val imageDrawable: Drawable? = ContextCompat.getDrawable(c, resourceId!!)
//            val bitmap: Bitmap? = imageDrawable?.toBitmap()
//
//            // Upload bitmap to Firebase Storage
//            if (bitmap != null) {
//                val baos = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//                val imageData: ByteArray = baos.toByteArray()


                            val user = modelclass(
                                i.uid.toString(),
                                i.name.toString(),
                                i.number.toString(),
                                i.image.toString(),
                                i.fcmtoken.toString(),
                            )
                            FirebaseDatabase.getInstance().reference.child(FirebaseAuth.getInstance().uid.toString()+"list")
                                .child(i.number.toString()).setValue(user)
                                .addOnSuccessListener {
                                    d.dismiss()
                                    Toast.makeText(c, "Added", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    d.dismiss()
                                    Toast.makeText(c, "Somting went wrong", Toast.LENGTH_SHORT).show()
                                }



                    }


                }


    }

