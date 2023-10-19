package com.example.cryptoo.fragments

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoo.Adapter.chatitemadapter
import com.example.cryptoo.R
import com.example.cryptoo.databinding.FragmentChatBinding
import com.example.cryptoo.modelclass
import com.example.cryptoo.swipetodelete
import com.example.cryptoo.totaluserclass
import com.google.android.play.core.integrity.p
import com.google.android.play.integrity.internal.c
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Chat: Fragment() {
    lateinit var  b : FragmentChatBinding
    lateinit var  databaseReference:  FirebaseDatabase
    lateinit var userList: ArrayList<modelclass>
    


    lateinit var adapter: chatitemadapter

   var string :  String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b =  FragmentChatBinding.inflate(inflater, container, false)


        b.card.visibility =  View.GONE
        databaseReference =  FirebaseDatabase.getInstance()
        userList = ArrayList()





        b.chatrv.layoutManager =  LinearLayoutManager(requireContext())
        b.chatrv.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        adapter = chatitemadapter(requireContext() , userList)

        databaseReference.reference.child(FirebaseAuth.getInstance().uid.toString()+"list")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {


                    if (snapshot.exists()) {
                        val d = ProgressDialog(requireContext())
                        d.setMessage("")
                        d.show()

                        b.progressBar.visibility = View.GONE
                        userList.clear()
                        for (i in snapshot.children) {
                            val user = i.getValue(modelclass::class.java)
                            if (user?.uid != FirebaseAuth.getInstance().uid) {
                                userList.add(user!!)
                            }
                            string = user?.number.toString()

                            adapter = chatitemadapter(requireContext(), userList)
                            b.chatrv.adapter = adapter
                            adapter.notifyDataSetChanged()
                           d.dismiss()


                        }

                    }
                    else {
                        b.progressBar.visibility = View.GONE
                        b.card.visibility =  View.VISIBLE
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

        var std =  object : swipetodelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                  val p = viewHolder.adapterPosition
                 userList.removeAt(p)
                FirebaseDatabase.getInstance().reference.child(FirebaseAuth.getInstance().uid.toString()+"list")
                    .child(string!!).removeValue()
                Toast.makeText(requireContext(), "removed :- $string", Toast.LENGTH_SHORT).show()
                 adapter.notifyDataSetChanged()

            }
        }
        var iteml = ItemTouchHelper(std)
        iteml.attachToRecyclerView(b.chatrv)




        return b.root
    }


//




}