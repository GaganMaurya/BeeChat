package com.example.cryptoo.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cryptoo.R
import com.example.cryptoo.databinding.FragmentBottomsheetdisplayBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 * Use the [bottomsheetdisplay.newInstance] factory method to
 * create an instance of this fragment.
 */
class bottomsheetdisplay : BottomSheetDialogFragment() {
    lateinit var b :  FragmentBottomsheetdisplayBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b =  FragmentBottomsheetdisplayBinding.inflate(inflater, container, false)

        val sss = this.activity?.getSharedPreferences("helo1", Context.MODE_PRIVATE)
        val name =  sss?.getString("naam" , null)
        b.name.setText(name.toString())

        b.cancel.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        b.save.setOnClickListener {
            if (b.name.text!!.isNotEmpty()) {
                var dialog = ProgressDialog(requireContext())
                dialog.setCancelable(false)
                dialog.setMessage("Please wait...")
                dialog.show()
                var ref = FirebaseDatabase.getInstance().reference.child("User")
                    .child(FirebaseAuth.getInstance().uid.toString())
                ref.child("name").setValue(b.name.text.toString())

                b.name.setText("")
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please Enter Your Name", Toast.LENGTH_SHORT).show()
            }
        }

        return b.root
    }

}