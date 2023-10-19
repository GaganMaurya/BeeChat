package com.example.cryptoo.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.cryptoo.R
import com.example.cryptoo.databinding.FragmentStatusBinding


class Status() : Fragment() {
    lateinit var b : FragmentStatusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b  = FragmentStatusBinding.inflate(inflater, container, false)


        val shared = activity?.getSharedPreferences("helo", Context.MODE_PRIVATE)
        val encode = shared?.getString("encodeimage", "DEFAULT")
        if (encode != "DEFAULT") {
            val imagebytes = Base64.decode(encode, Base64.DEFAULT)
            val decodeImage = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.size)
            b.profilestatus.setImageBitmap(decodeImage)
        }


        b
        return  b.root
    }


}